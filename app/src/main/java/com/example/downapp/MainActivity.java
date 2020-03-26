package com.example.downapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    PermissionHelper permissionHelper;
    //url为app下载链接，请替换成自己的app下载lur
    String url = "https://ali-fir-pro-binary.jappstore.com/1354718ca71086bb181505bcc6300ba5ca37ae41.apk?auth_key=1585215887-0-0-ce55531e38490a704f6942641137117a";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    new InstallApk(MainActivity.this)
                            .installApk(new File(Environment.getExternalStorageDirectory(), "qianjinjia.apk"));
                    break;
            }

        }
    };

    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHelper = new PermissionHelper(this);

        permissionsCheckAndDownload();

    }


    private void permissionsCheckAndDownload() {
        if (Build.VERSION.SDK_INT >= 23) {
            permissionsCheck();
        } else {
            new DownFileHelper(MainActivity.this, handler)
                    .downFile(url);
        }

    }

    private void permissionsCheck() {
        if (!permissionHelper.checkPermissionAllGranted(mPermissionList)) {
            permissionHelper.requestPermissionAllGranted(mPermissionList, 1);
        } else {
            new DownFileHelper(MainActivity.this, handler)
                    .downFile(url);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new DownFileHelper(MainActivity.this, handler)
                            .downFile(url);

                } else {
                    //不给权限处理
                }
                break;
//            case 2:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    new InstallApk(MainActivity.this)
//                            .installApk(new File(Environment.getExternalStorageDirectory(), "qianjinjia.apk"));
//                } else {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                    startActivityForResult(intent, 10012);
//                }
//                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("resultCode", resultCode + "");
            switch (requestCode) {
                case 10012:
                    Log.d("resultCode", resultCode + "");
                    if (Build.VERSION.SDK_INT >= 26) {
                        boolean b = getPackageManager().canRequestPackageInstalls();
                        Log.d("getPackageManager", b + "");
                        if (b) {
                            new InstallApk(MainActivity.this)
                                    .installApk(new File(Environment.getExternalStorageDirectory(), "qianjinjia.apk"));
                        } else {
                            final AButtonDialog aButton = new AButtonDialog(MainActivity.this);
                            aButton.setStyle("您未打开未知来源\n权限不能及时更新", "知道了");
                            aButton.setClicklistener(new AButtonDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {

                                }
                            });
                            aButton.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                        aButton.dismiss();
                                        Process.killProcess(Process.myPid());   //获取PID
                                        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                                    }
                                    return false;
                                }
                            });
                            aButton.show();
                        }
                    }

                    break;

                default:
                    break;
            }
        }



}
