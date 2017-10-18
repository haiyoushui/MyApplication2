package com.eagle.androidlib.utils;

import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Log统一管理类
 */
public class Logger {
    private static File file;
    private Logger() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "ISSSFaceRegi";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug) Log.v(TAG, msg);}

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug){
            writeLog("【I|"+tag+"】["+DateUtil.getCurrentDateTimeStr()+"] >||"+msg);
            Log.i(tag, msg);
            }
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            writeLog("【D|"+tag+"】["+DateUtil.getCurrentDateTimeStr()+"] >||"+msg);
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            writeLog("【E|"+tag+"】["+DateUtil.getCurrentDateTimeStr()+"] >||"+msg);
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            writeLog("【V|"+tag+"】["+DateUtil.getCurrentDateTimeStr()+"] >||"+msg);
            Log.v(tag, msg);
    }
    public static void creatLog() {
        try {
            file = new File(SystemUtil.LOGPATH + DateUtil.getCurrentDateStr()+"-Log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeLog(String log){
        try{
            creatLog();
            Writer  mWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            mWriter.write(log);
            mWriter.write("\n");
            mWriter.flush();

        }catch (Exception e){
            e.printStackTrace();
        }
}


}