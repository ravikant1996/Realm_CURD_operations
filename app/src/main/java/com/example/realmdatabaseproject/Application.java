package com.example.realmdatabaseproject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);

    }
}
