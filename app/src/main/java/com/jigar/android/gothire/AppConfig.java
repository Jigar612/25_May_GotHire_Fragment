package com.jigar.android.gothire;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by COMP11 on 11-Jan-18.
 */

public class AppConfig extends MultiDexApplication {
        @Override
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
            MultiDex.install(this);
        }

        //  wait, ahiya search karu ,ok
}
