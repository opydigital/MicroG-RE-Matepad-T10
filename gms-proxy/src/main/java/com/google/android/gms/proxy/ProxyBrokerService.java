package com.google.android.gms.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Lightweight Binder proxy service that forwards {@code app.revanced.android.gms.*}
 * service bindings to the main MicroG installation ({@code com.google.android.gms}).
 */
public class ProxyBrokerService extends Service {
    private static final String TAG = "MorpheGmsProxy";
    private static final String TARGET_PACKAGE = "com.google.android.gms";
    private static final long BIND_TIMEOUT_SECONDS = 5L;

    private final List<ServiceConnection> activeConnections = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            return null;
        }
        Intent targetIntent = new Intent(intent);
        String action = intent.getAction();
        if (action != null) {
            targetIntent.setAction(action.replace("app.revanced.", "com.google."));
        }
        targetIntent.setPackage(TARGET_PACKAGE);
        targetIntent.setComponent(null);

        final CountDownLatch latch = new CountDownLatch(1);
        final IBinder[] remoteHolder = new IBinder[1];

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                remoteHolder[0] = service;
                latch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                remoteHolder[0] = null;
            }
        };

        boolean bound = bindService(targetIntent, connection, Context.BIND_AUTO_CREATE);
        if (!bound) {
            Log.w(TAG, "Failed to bind to target service: " + targetIntent.getAction());
            latch.countDown();
            return null;
        }

        synchronized (activeConnections) {
            activeConnections.add(connection);
        }

        return new Binder() {
            @Override
            protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                try {
                    if (!latch.await(BIND_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                        throw new RemoteException("Timed out waiting for " + TARGET_PACKAGE);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RemoteException("Interrupted waiting for " + TARGET_PACKAGE);
                }
                IBinder remote = remoteHolder[0];
                if (remote == null) {
                    throw new RemoteException("Target service disconnected: " + targetIntent.getAction());
                }
                return remote.transact(code, data, reply, flags);
            }
        };
    }

    @Override
    public void onDestroy() {
        synchronized (activeConnections) {
            for (ServiceConnection conn : activeConnections) {
                try {
                    unbindService(conn);
                } catch (IllegalArgumentException ignored) {
                    // Already unbound
                }
            }
            activeConnections.clear();
        }
        super.onDestroy();
    }
}
