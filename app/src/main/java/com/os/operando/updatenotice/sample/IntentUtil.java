package com.os.operando.updatenotice.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class IntentUtil {

    private IntentUtil() {
    }

    /**
     * Google Playのアプリ詳細画面を開く
     *
     * @param context     Context
     * @param packageName Package Name
     */
    public static void openGooglePlayAppDetails(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}