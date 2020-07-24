package com.sdt.commonservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btn_sendBootMsg;
    Button btn_sendTestMsg;
    Button btn_runna;
    Button btn_runnash;
    Button btn_runAPK;
    Button btn_accessshare;
    Button btn_stopservice;
    BootBroadcastReceiver mReceive;
    Button btn_runservice;
    String LOGNAME = "CommonService";
    MiscFile file;

    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean rootRet = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new MiscFile();
        file.initData("mainactivity启动了");

        //获取文件权限
        PermissionUtils.isGrantExternalRW(this, 1);

        //检查是否能拿到root权限
        RootAuth rootAuth = new RootAuth();
        rootRet = rootAuth.getRootAhth();
        if(rootRet == true){
            file = new MiscFile();
            file.initData("commmonservice获取了root权限");
        }

        //获取系统版本
        file.initData("版本：" + Build.VERSION.SDK_INT);

        //String filePath = "/data/data/com.sdt.commonservice/";
        //String fileName = "shtest.sh";
        //file.newFile(filePath,fileName,"echo '我是sh文件'");
        //String shName="cd /data/data/com.sdt.commonservice && chmod 777 * && ./shtest.sh";

        //注册广播接收器
        mReceive = new BootBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.wsstest");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(mReceive, intentFilter);
        Log.i(LOGNAME, "注册消息接收过滤器");
        file.initData("注册消息接收过滤器");

        btn_sendBootMsg = (Button)findViewById(R.id.btn_send);
        btn_sendBootMsg.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.BOOT_COMPLETED");
                intent.putExtra("test", "BOOT_COMPLETED test");
                sendBroadcast(intent);
                Log.i(LOGNAME, "发送消息BOOT_COMPLETED");
                file.initData("发送消息BOOT_COMPLETED");
            }
        });

        btn_sendTestMsg = (Button)findViewById(R.id.btn_sendTest);
        btn_sendTestMsg.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.wsstest");
                intent.putExtra("test", "wsstest test");
                sendBroadcast(intent);
                Log.i(LOGNAME, "发送消息wsstest");
                file.initData("发送消息wsstest");
            }
        });

        btn_runna = (Button)findViewById(R.id.btn_runNA);
        btn_runna.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                CommandExecution exec = new CommandExecution();
                exec.execStartNAShell(true);
            }
        });


        btn_runnash = (Button)findViewById(R.id.btn_runNASh);
        btn_runnash.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                CommandExecution exec = new CommandExecution();
                exec.execStartNAShell(false);
            }
        });

        btn_runAPK = (Button)findViewById(R.id.btn_runAPK);
        btn_runAPK.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                StartAPK start = new StartAPK();
                start.run(getApplicationContext());
                Log.i(LOGNAME, "启动APK完毕");
                file.initData("启动APK完毕");
            }
        });

        btn_accessshare = (Button)findViewById(R.id.btn_accessShare);
        btn_accessshare.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                byte[] buff = new byte[2048];
                file.readFile(buff, "/data/data/com.sdt.shareuid/","test.sh");
                String shName = "cd /data/data/com.sdt.shareuid && chmod 777 * && ./test.sh";
                CommandExecution comm = new CommandExecution();
                int ret = comm.execCommand(shName, false);
                Log.i(LOGNAME, buff.toString());
                Log.i(LOGNAME, "访问shareuid完毕");
                file.initData("访问shareuid完毕");
            }
        });

        btn_runservice = (Button)findViewById(R.id.btn_runService);
        btn_runservice.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(MainActivity.this, MainService.class);
                startForegroundService(serviceIntent);

            }
        });

        btn_stopservice = (Button)findViewById(R.id.btn_stopService);
        btn_stopservice.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(MainActivity.this, MainService.class);

            }
        });


    }

    /*
    * onRequestPermissionResult是对activity.requestPermissions（）函数的回调，根据请求码做具体处理。如果没有授权，自然要授权后再能使用。只需要咋入口activity中申请权限即可，其它子activity会自动被授权。此授权会在一开始打开一个请求权限页面，点击允许就会获取权限
//对获取权限处理的结果*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)){
                        Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "buxing", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        MiscFile file = new MiscFile();
        file.initData("Activity停止了");
        super.onDestroy();
    }
}