package com.google.android.gms.proxy;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * Forwards ContentProvider queries from {@code app.revanced.*} authorities
 * to their {@code com.google.*} counterparts in the main MicroG installation.
 */
public class ProxyContentProvider extends ContentProvider {

    private static Uri rewriteUri(Uri uri) {
        if (uri == null || uri.getAuthority() == null) {
            return uri;
        }
        String targetAuthority = uri.getAuthority().replace("app.revanced", "com.google");
        return uri.buildUpon().authority(targetAuthority).build();
    }

    private ContentResolver resolver() {
        Context context = getContext();
        return context != null ? context.getContentResolver() : null;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ContentResolver cr = resolver();
        return cr != null ? cr.query(rewriteUri(uri), projection, selection, selectionArgs, sortOrder) : null;
    }

    @Override
    public String getType(Uri uri) {
        ContentResolver cr = resolver();
        return cr != null ? cr.getType(rewriteUri(uri)) : null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ContentResolver cr = resolver();
        return cr != null ? cr.insert(rewriteUri(uri), values) : null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        ContentResolver cr = resolver();
        return cr != null ? cr.delete(rewriteUri(uri), selection, selectionArgs) : 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ContentResolver cr = resolver();
        return cr != null ? cr.update(rewriteUri(uri), values, selection, selectionArgs) : 0;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        ContentResolver cr = resolver();
        if (cr == null) {
            return null;
        }
        try {
            return cr.call(Uri.parse("content://com.google.android.gsf.gservices"), method, arg, extras);
        } catch (Exception e) {
            return null;
        }
    }
}
