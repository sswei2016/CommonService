package com.sdt.commonservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class StartAPK {

    MiscFile file;
    String LOGNAME = "CommonService";

    public void run(Context context)
    {
        String packageName="com.sdt.sdtas";
        PackageManager packageManager = context.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(packageName);
        context.startActivity(it);
    }

    public void accessOtherAPK(Activity activity)
    {
        String packageName="com.sdt.sdtas";
       // Context con =  activity.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
    }


    public int getPath(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> mAllPackages = packageManager.getInstalledPackages(0);
        for(int i = 0; i < ((List) mAllPackages).size(); i ++){
            PackageInfo packageInfo = mAllPackages.get(i);
            String apkPath = packageInfo.applicationInfo.sourceDir;
            String apkName = (String) packageInfo.applicationInfo.loadLabel(packageManager);
            if(apkName.equals("test")){
                Log.i(LOGNAME, "找到sdtas");
                return 0;
            }
        }
        return -1;
    }


}
