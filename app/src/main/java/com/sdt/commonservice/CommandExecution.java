package com.sdt.commonservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecution {

    final String LOGNAME = "CommonService";

    public final static String COMMAND_SU       = "su";
    public final static String COMMAND_SH       = "sh";
    public final static String COMMAND_EXIT     = "exit\n";
    public final static String COMMAND_LINE_END = "\n";

    /**
     * Command执行结果
     * @author Mountain
     *
     */
    public  class CommandResult {
        public int result = -1;
        public String errorMsg;
        public String successMsg;
    }


    public void execStartNAShell(boolean isRoot)
    {
        MiscFile file = new MiscFile();
        String shName = "cd /data/data/com.sdt.sdtas/files/sdtas/lib && chmod 777 * && ./start.sh";
        CommandExecution comm = new CommandExecution();
        int ret = comm.execCommand(shName, isRoot);
        Log.i(LOGNAME, "拉NA完毕_sh" + ret);
        file.initData("拉NA完毕_sh" + ret);

    }


    /**
     * 执行命令—单条
     * @param command
     * @param isRoot
     * @return
     */
    public  int execCommand(String command, boolean isRoot) {
        String[] commands = {command};
       return execCommand(commands, isRoot);
    }

    /**
     * 执行命令-多条
     * @param commands
     * @param isRoot
     * @return
     */
    public int execCommand(String[] commands, boolean isRoot) {
        CommandResult commandResult = new CommandResult();
        if (commands == null || commands.length == 0) return -1;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command != null) {
                    os.write(command.getBytes());
                    os.writeBytes(COMMAND_LINE_END);
                    os.flush();
                }
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();
            commandResult.result = process.waitFor();
            if (commandResult.result == 0) {
                successMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                while ((s = successResult.readLine()) != null) successMsg.append(s);
                commandResult.successMsg = successMsg.toString();
                if (os != null) os.close();
                if (successResult != null) successResult.close();
                if (process != null) process.destroy();

                Log.i(LOGNAME, "执行command命令成功"+commandResult.successMsg);
                MiscFile file = new MiscFile();
                file.initData("执行command命令成功"+commandResult.successMsg);
                return 0;
            }else {
                //获取错误信息
                errorMsg = new StringBuilder();
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = errorResult.readLine()) != null) errorMsg.append(s);
                commandResult.errorMsg = errorMsg.toString();
                Log.i(LOGNAME, commandResult.result + " | " + " | " + commandResult.errorMsg);
                if (os != null) os.close();
                if (errorResult != null) errorResult.close();
                if (process != null) process.destroy();

                MiscFile file = new MiscFile();
                file.initData("执行command命令失败"+commandResult.result + " | "+" | " + commandResult.errorMsg);
                return -1;
            }
        } catch (IOException e) {
            String errmsg = e.getMessage();
            if (errmsg != null) {
                Log.i(LOGNAME, errmsg);
                MiscFile file = new MiscFile();
                file.initData("执行command失败:"+errmsg);
            } else {
                e.printStackTrace();
            }
            return -1;
        } catch (Exception e) {
            String errmsg = e.getMessage();
            if (errmsg != null) {
                Log.i(LOGNAME, errmsg);
                MiscFile file = new MiscFile();
                file.initData("执行command失败:"+errmsg);
            } else {
                e.printStackTrace();
            }
            return -1;
        }
    }
}
