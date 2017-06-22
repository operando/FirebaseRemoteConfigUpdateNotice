package com.os.operando.updatenotice.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.annimon.stream.Optional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.List;

public class RemoteConfig {

    private static final String TAG = RemoteConfig.class.getSimpleName();

    public static void init(Context context) {
        getRemoteConfig(context)
                .ifPresent(firebaseRemoteConfig -> {
                    FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                            .setDeveloperModeEnabled(BuildConfig.DEBUG)
                            .build();
                    firebaseRemoteConfig.setConfigSettings(configSettings);
                });
    }

    public static void updateConfig(Context context, @Nullable OnCompleteListener<Void> onCompleteListener) {
        getRemoteConfig(context)
                .ifPresent(firebaseRemoteConfig -> {
                    long cacheExpiration = firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled() ? 0 : 600;
                    firebaseRemoteConfig.fetch(cacheExpiration)
                            .addOnCompleteListener(task -> {
                                Log.d(TAG, "fetch complete.");
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "fetch successful.");
                                    firebaseRemoteConfig.activateFetched();
                                }
                                Optional.ofNullable(onCompleteListener).ifPresent(l -> l.onComplete(task));
                            })
                            .addOnFailureListener(command -> Log.w(TAG, "fetch fail.", command));
                });
    }

    public static Optional<String> getUpdateNoticeConfig(Context context) {
        return getRemoteConfig(context)
                .map(firebaseRemoteConfig -> firebaseRemoteConfig.getString("android_update_notice_config"));
    }

    private static Optional<FirebaseRemoteConfig> getRemoteConfig(Context context) {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(context);
        if (firebaseApps == null || firebaseApps.isEmpty()) {
            Log.w(TAG, "FirebaseApp.getApps is empty.");
            // No firebase apps.
            // FirebaseRemoteConfig uses the default FirebaseApp, so if no FirebaseApp has been initialized yet, this method will throw an IllegalStateException.
            // https://firebase.google.com/docs/reference/android/com/google/firebase/remoteconfig/FirebaseRemoteConfig.html#getInstance()
            // http://stackoverflow.com/questions/37346363/java-lang-illegalstateexception-firebaseapp-with-name-default/37355454
            return Optional.empty();
        }
        return Optional.ofNullable(FirebaseRemoteConfig.getInstance());
    }
}