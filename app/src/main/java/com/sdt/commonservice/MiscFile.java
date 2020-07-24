package com.sdt.commonservice;

import android.util.Log;

import java.io.RandomAccessFile;

public class MiscFile {

    final String LOGNAME = "CommonService";
    Process p;

    public void initData(String info) {
        String filePath = "/data/data/com.sdt.commonservice/";
        String fileName = "CommonService.txt";
        writeTxtToFile(info, filePath, fileName);
    }

    public void newFile(String filePath, String fileName, String info) {
        writeTxtToFile(info, filePath, fileName);
    }

    public void readFile(byte[]  buff, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        try {
            java.io.File file = new java.io.File(strFilePath);
            if (!file.exists()) {
                Log.i(LOGNAME, "此文件不存在:" + strFilePath);
            }else {
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(0);
                raf.read(buff);
                raf.close();
            }
        } catch (Exception e) {
            Log.i(LOGNAME, "读文件错误:" + e);
        }
    }

    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath+fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            java.io.File file = new java.io.File(strFilePath);
            if (!file.exists()) {
                Log.i(LOGNAME, "创建文件:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try {
                p = Runtime.getRuntime().exec("chmod 777 " +  file );
                int status = p.waitFor();
                if (status == 0) {
                    //chmod succeed
                    Log.i(LOGNAME, "chmod成功");
                } else {
                    //chmod failed
                    Log.i(LOGNAME, "chmod失败");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();

        } catch (Exception e) {
            Log.i(LOGNAME, "Error on write File:" + e);
        }
    }
    // 生成文件
    public java.io.File makeFilePath(String filePath, String fileName) {
        java.io.File file = null;

        makeRootDirectory(filePath);
        try {
            file = new java.io.File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
    // 生成文件夹
    public  void makeRootDirectory(String filePath) {

        java.io.File file = null;
        try {
            file = new java.io.File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i(LOGNAME, "error:"+e+"");
        }
    }
}
