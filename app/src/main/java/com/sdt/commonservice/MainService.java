package com.sdt.commonservice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainService extends Service {
    final String LOGNAME = "CommonService";
    private static final String ID="channel_1";
    private static final String NAME="前台服务";
    private AssistServiceConnection mConnection;
    private final int PID = android.os.Process.myPid();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(LOGNAME, "服务创建了: 时间|" + getCurTime());
        MiscFile file = new MiscFile();
        //file.initData("服务创建了: 时间|" + getCurTime());

        //显示通知
        //setForeground();

        //不显示通知
        misc_setForeground();
        //file.initData("特殊处理，隐藏显示通知");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(LOGNAME, "服务开始了" + getCurTime());
        MiscFile file = new MiscFile();
        //file.initData("服务开始了" + getCurTime());

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
        //file.initData("服务停止了" + getCurTime());

        Intent intent = new Intent();
        intent.setAction("android.intent.action.wsstest");
        intent.putExtra("test", "wsstest test");
        sendBroadcast(intent);
        //file.initData("发送消息" + "wsstest");

        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
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
        //file.initData("发通知了");

        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel (ID,NAME,NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Notification notification=new Notification.Builder (this,ID)
                .setContentTitle ("收到一条重要通知")
                .setContentText ("安全认证客户端运行中")
                .setSmallIcon (R.mipmap.ic_launcher)
                .setLargeIcon (BitmapFactory.decodeResource (getResources (),R.mipmap.ic_launcher))
                .build ();
        startForeground (PID,notification);
    }

    public void misc_setForeground()
    {
        if (null == mConnection)
        {
            mConnection = new AssistServiceConnection();
        }
        this.bindService(new Intent(this, AssistService.class), mConnection, Service.BIND_AUTO_CREATE);
    }

    private class AssistServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(LOGNAME, "服务创建连接"+LOGNAME);

            //sdk>18会在通知栏显示service正在运行，首先启动主服务，然后使用同一个ID号启动辅助服务，停止辅助服务，可以去掉通知
            Service assistService = ((AssistService.LocalBinder) iBinder).getService();
            startForeground(PID, getNotification());
            assistService.startForeground(PID, getNotification());
            assistService.stopForeground(true);

            unbindService(mConnection);
            mConnection = null;
            Log.i(LOGNAME, "启动前台进程");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(LOGNAME, "服务失去连接"+ LOGNAME);
        }
    }

    public Notification getNotification()
    {
        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel (ID,NAME,NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Notification notification=new Notification.Builder (this,ID)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentTitle ("收到一条重要通知(测试)")
                .setContentText ("安全认证客户端运行中(测试)")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon (BitmapFactory.decodeResource (getResources (),R.mipmap.ic_launcher))
                .build ();
        return notification;
    }

}
