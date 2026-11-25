package com.google.android.gms.proxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Forwards GCM/C2DM registration broadcasts from {@code com.google.*}
 * to {@code app.revanced.android.gms}.
 */
public class ProxyBroadcastReceiver extends BroadcastReceiver {
    private static final String TARGET_PACKAGE = "app.revanced.android.gms";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        Intent targetIntent = new Intent(intent);
        String action = intent.getAction();
        if (action != null) {
            targetIntent.setAction(action.replace("com.google.", "app.revanced."));
        }
        targetIntent.setPackage(TARGET_PACKAGE);
        targetIntent.setComponent(null);
        try {
            context.sendBroadcast(targetIntent);
        } catch (Exception ignored) {
            // Best-effort forward
        }
    }
}
