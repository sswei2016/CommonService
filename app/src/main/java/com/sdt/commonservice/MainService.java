package com.sdt.commonservice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainService extends Service {
    final String LOGNAME = "CommonService";
    private static final String TAG="MyService";
    private static final String ID="channel_1";
    private static final String NAME="前台服务";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOGNAME, "服务创建了: 时间|" + getCurTime());
        MiscFile file = new MiscFile();
        file.initData("服务创建了: 时间|" + getCurTime());
        if(Build.VERSION.SDK_INT>=26){
            setForeground();
        }else{

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(LOGNAME, "服务开始了" + getCurTime());
        MiscFile file = new MiscFile();
        file.initData("服务开始了" + getCurTime());

        CommandExecution exec = new CommandExecution();
        exec.execStartNAShell(false);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i(LOGNAME, "服务停止了" + getCurTime());
        MiscFile file = new MiscFile();
        file.initData("服务停止了" + getCurTime());

        Intent intent = new Intent();
        intent.setAction("android.intent.action.wsstest");
        intent.putExtra("test", "wsstest test");
        sendBroadcast(intent);
        file.initData("发送消息" + "wsstest");

        super.onDestroy();
    }

    public String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    //@TargetApi (26)
    private void setForeground(){
        MiscFile file = new MiscFile();
        file.initData("发通知了");

        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel (ID,NAME,NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Notification notification=new Notification.Builder (this,ID)
                .setContentTitle ("收到一条重要通知")
                .setContentText ("这是重要通知")
                .setSmallIcon (R.mipmap.ic_launcher)
                .setLargeIcon (BitmapFactory.decodeResource (getResources (),R.mipmap.ic_launcher))
                .build ();
        startForeground (1,notification);
    }

}
