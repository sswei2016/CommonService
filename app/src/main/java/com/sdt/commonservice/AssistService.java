package com.sdt.commonservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AssistService extends Service {
    String LOGNAME = "CommonService";

    public class LocalBinder extends Binder {
        public AssistService getService() {
            return AssistService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOGNAME, "AssistService: onBind()");
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(LOGNAME, "AssistService: onDestroy()");
    }
}

/*
*思路是：一个最后保留的MyService，一个辅助消除通知的AssistService, 利用MyService去绑定AssistService，
* 在关联函数onServiceConnected()中实现两个Service调用startForeground变为前台服务，
* 注意一定要使用一样的Notification ID，然后AssistService取消前台效果stopForeground从而删除通知。
* */