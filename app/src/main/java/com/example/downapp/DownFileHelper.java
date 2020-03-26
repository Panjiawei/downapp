package com.example.downapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2018/3/9.
 */

public class DownFileHelper {

    Handler handler;
    Context mContext;
    ProgressBarDialog pBar;
    NotificationManager mNotifyManager;
    Notification.Builder builder;

    public DownFileHelper(Context mContext, Handler handler) {
        this.handler = handler;
        this.mContext = mContext;
    }

    /**
     * 下载最新版本的apk
     *
     * @param path apk下载地址
     */
    public void downFile(final String path) {
//        pBar = new ProgressBarDialog(mContext);
//        pBar.setCancelable(false);
//        pBar.setTitle("正在下载...");
//        pBar.show();
        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap btm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);//可以换成你的app的logo
        if (Build.VERSION.SDK_INT >= 26) {

            //创建 通知通道  channelid和channelname是必须的（自己命名就好）
            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mNotifyManager.createNotificationChannel(channel);

            builder = new Notification.Builder(mContext, "1");
            //设置通知显示图标、文字等
            builder.setSmallIcon(R.mipmap.ic_launcher)//可以换成你的app的logo
                    .setLargeIcon(btm)
                    .setTicker("正在下载")
                    .setContentTitle("钱进家")
                    .setAutoCancel(true)
                    .build();
            mNotifyManager.notify(1, builder.build());

        } else {
            builder = new Notification.Builder(mContext);
            builder.setSmallIcon(R.mipmap.ic_launcher)//可以换成你的app的logo
                    .setLargeIcon(btm)
                    .setTicker("正在下载")
                    .setContentTitle("钱进家")
                    .setAutoCancel(true)
                    .build();
            mNotifyManager.notify(1, builder.build());
        }
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        int length = con.getContentLength();// 获取文件大小
                        InputStream is = con.getInputStream();
                        //pBar.setMax(length); // 设置进度条的总长度
                        FileOutputStream fileOutputStream = null;
                        if (is != null) {
                            //对apk进行保存
                            File file = new File(Environment.getExternalStorageDirectory()
                                    .getPath(), "qianjinjia.apk");
                            fileOutputStream = new FileOutputStream(file);
                            byte[] buf = new byte[6000];
                            int ch;
                            int process = 0;
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            // 设置精确到小数点后2位
                            numberFormat.setMaximumFractionDigits(2);
                            String result;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);
                                process += ch;
                                //更新进度条
                                Log.d("process", process + "");
                                Log.d("length", length + "");
                                result = numberFormat.format((float) process / (float) length * 100);
                                builder.setContentText("下载进度：" + result + "%");
                                builder.setProgress(length, process, false);
                                mNotifyManager.notify(1, builder.build());
                                //pBar.setProgress(process); // 实时更新进度了
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        //apk下载完成，使用Handler()通知安装apk
                        builder.setProgress(length, length, false);
                        builder.setContentText("已经下载完成");
                        mNotifyManager.notify(1, builder.build());
                        mNotifyManager.cancelAll();
                        handler.sendEmptyMessage(0);
                        // pBar.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();


    }



    /**
     * AndroidQ以上保存图片到公共目录
     *
     * @param imageName 图片名称
     * @param imageType 图片类型
     * @param relativePath 缓存路径
     */
    private  Uri insertImageFileIntoMediaStore (String imageName, String imageType,
                                                      String relativePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        if (TextUtils.isEmpty(relativePath)) {
            return null;
        }
        Uri insertUri = null;
        ContentResolver resolver = mContext.getContentResolver();
        //设置文件参数到ContentValues中
        ContentValues values = new ContentValues();
        //设置文件名
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        //设置文件描述，这里以文件名代替
        values.put(MediaStore.Images.Media.DESCRIPTION, imageName);
        //设置文件类型为image/*
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, "image/" + imageType);
        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
        //故该方法只可在Android10的手机上执行
        values.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath);
        //EXTERNAL_CONTENT_URI代表外部存储器
        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //insertUri表示文件保存的uri路径
        insertUri = resolver.insert(external, values);
        return insertUri;
    }

}
