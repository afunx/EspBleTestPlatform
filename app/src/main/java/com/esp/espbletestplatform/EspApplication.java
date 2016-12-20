package com.esp.espbletestplatform;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.esp.log.InitLogger;

/**
 * Created by afunx on 20/12/2016.
 */

public class EspApplication extends Application {

    private static final int REQUEST_CODE = 1;

    private static EspApplication sInstance = new EspApplication();

    public static EspApplication getsInstance() {
        return sInstance;
    }

    public static String getEspRootSDPath() {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().toString() + "/EspBleTestPlatform";
        }
        return path;
    }

}
