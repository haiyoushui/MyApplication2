package com.isss.liuh.myapplication.Share;

import android.content.Context;
import android.content.SharedPreferences;

import com.eagle.androidlib.utils.DateUtil;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.TimerTask.GetAccessTokenTimerTask;

/**
 * 保存一些与本程序运行有关的数据
 * Created by LiuH on 2017/10/12.
 */

public class SystemShare {
    private static final String SP_APP = "SYSTEMSHARES";
    private static final String BAIDUTOKEN = "baiduaccesstoken";//百度Token
    private static final String BAIDUEXPIRESIN = "baiduexpiresin";//百度Token周期
    private static final String BAIDUSAVETIME= "baidusavetime";//百度Token上次保存时间

    private static final String SYSISCUTPIC = "iscutpic";//系统设置，是否打开图片剪切功能

    public static void setKeyBAIDUTOKEN(Context context, String baiduToken,long expiresIn) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).edit();
        editor.putString(BAIDUTOKEN, baiduToken);
        editor.putLong(BAIDUEXPIRESIN, expiresIn);
        editor.putString(BAIDUSAVETIME, DateUtil.getCurrentDateTimeStr());
        editor.commit();
    }

    public static String getBAIDUTOKEN(Context context){
        String accessToken = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).getString(BAIDUTOKEN,"");
        long expreseIn = SystemShare.getBAIDUEXPIRESIN(FaceRApplacation.getContext());//百度token过期周期
        long saveTime = DateUtil.parseDateTime(SystemShare.getBAIDUTBAIDUSAVETIME(FaceRApplacation.getContext())).getTime();//上一次更新的时间
        if (accessToken == "" || "".equals(accessToken)||saveTime+expreseIn < DateUtil.getCurrentDate().getTime()) {
            GetAccessTokenTimerTask getAccessTokenTimerTask = new GetAccessTokenTimerTask();
            getAccessTokenTimerTask.getAccessToken();
        }
        return accessToken;
    }
    public static Long getBAIDUEXPIRESIN(Context context){
        return context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).getLong(BAIDUEXPIRESIN,2592000);
    }
    public static String getBAIDUTBAIDUSAVETIME(Context context){
        return context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).getString(BAIDUSAVETIME,"2000-1-1 00:00:00");
    }

    public static void setKeySYSISCUTPIC(Context context,boolean iscutpic){
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).edit();
        editor.putBoolean(SYSISCUTPIC, iscutpic);
        editor.commit();
    }
    public static boolean getSYSISCUTPIC(Context context){
        return context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE).getBoolean(SYSISCUTPIC,true);
    }
}
