package com.sdt.commonservice;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
    final String LOGNAME = "CommonService";
    static final String boot_action="android.intent.action.BOOT_COMPLETED";
    static final String test_action = "android.intent.action.wsstest";
    MiscFile file;

    @Override
    public void onReceive(Context context, Intent intent) {

        String actionName = intent.getAction();
        String param;
        file = new MiscFile();
        Log.i(LOGNAME, "-->BootBroadcastReceiver,消息名字是:" + actionName);
        file.initData("-->BootBroadcastReceiver,消息名字是:"+actionName);

        param = intent.getStringExtra("test");
        if(param != null){
            Log.i(LOGNAME, "测试消息:" + param);
            file.initData("测试消息:"+param);
        }

        if (intent.getAction().equals(boot_action) || intent.getAction().equals(test_action)){
            Log.i(LOGNAME, "接收到了开机启动消息");
            MiscFile file = new MiscFile();
            file.initData("接收到了开机启动消息");
            Intent serviceIntent = new Intent(context, MainService.class);
            context.startService(serviceIntent);
        }
    }

}
