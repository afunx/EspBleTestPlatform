package com.esp.espbletestplatform;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.esp.log.InitLogger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private String[] getManifextPermissions() {
        // all permissions in AndroidManifext.xml
        // for android don't let me get them dynamically, it is ugly to code like this
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    private void requestAuthorities() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // permissions0 means permissions require requesting
            final List<String> permissions0 = new ArrayList<>();

            for (String permission : getManifextPermissions()) {
                if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissions0.add(permission);
                }
            }

            if (!permissions0.isEmpty()) {
                String[] permissions1 = new String[permissions0.size()];
                for (int i = 0; i < permissions0.size(); i++) {
                    permissions1[i] = permissions0.get(i);
                }
                // request permission one by one
                ActivityCompat.requestPermissions(this,
                        permissions1,
                        REQUEST_CODE
                );
            } else {
                permissionsPermited();
            }
        } else {
            permissionsPermited();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            boolean isForbidden = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    isForbidden = true;
                    break;
                }
            }
            if (isForbidden) {
                permissionsDenied();
            } else {
                permissionsPermited();
            }
        }
    }

    private void permissionsPermited() {
        new Thread() {
            @Override
            public void run() {
                InitLogger.init();
            }
        }.start();

        // goto DevicesActivity
        Intent intent = new Intent(this, DevicesActivity.class);
        startActivity(intent);
        finish();
    }

    private void permissionsDenied() {
        Toast.makeText(this, "At least one permission is denied", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAuthorities();
    }

}
