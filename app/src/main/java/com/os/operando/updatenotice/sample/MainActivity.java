package com.os.operando.updatenotice.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        RemoteConfig.init(this);
        RemoteConfig.updateConfig(this, task -> {
        });

        RemoteConfig.getUpdateNoticeConfig(this)
                .filter(value -> !TextUtils.isEmpty(value))
                .flatMap(UpdateNotice::parseJson)
                .filter(value -> getSharedPreferences("update_notice", Context.MODE_PRIVATE).getInt("showed_update_notice_version", 0) < value.updateApplicationVersion)
                .filter(value -> BuildConfig.VERSION_CODE < value.updateApplicationVersion)
                .ifPresent(this::showUpdateDialog);
    }

    private void showUpdateDialog(UpdateNotice updateNotice) {
        new AlertDialog.Builder(this)
                .setTitle(updateNotice.updateTitle)
                .setMessage(updateNotice.updateMessage)
                .setPositiveButton("アップデートする", (dialogInterface, i) -> {
                    IntentUtil.openGooglePlayAppDetails(this, getPackageName());
                })
                .setNegativeButton("閉じる", (dialogInterface, i) -> {

                })
                .show();

        getSharedPreferences("update_notice", Context.MODE_PRIVATE)
                .edit()
                .putInt("showed_update_notice_version", updateNotice.updateApplicationVersion)
                .apply();
    }
}