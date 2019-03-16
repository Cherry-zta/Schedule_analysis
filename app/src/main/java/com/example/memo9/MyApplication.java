package com.example.memo9;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *  Created by zxy 2019/3/14
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);
    }
}
