package com.isss.liuh.myapplication.TimerTask;

import com.eagle.androidlib.utils.DateUtil;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.ToastManager;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.UTILS.PubInfo;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * 获取百度AccessToken 的定时器
 * 因为此Token每隔30过期，所以每隔10天检查一次是否需要更新
 * 此定时器在Applacation中开启
 * Created by LiuH on 2017/10/12.
 */

public class GetAccessTokenTimerTask extends TimerTask {
    @Override
    public void run() {
        getAccessToken();
    }
    public   void getAccessToken(){
        long expreseIn = SystemShare.getBAIDUEXPIRESIN(FaceRApplacation.getContext());//百度token过期周期
        long saveTime = DateUtil.parseDateTime(SystemShare.getBAIDUTBAIDUSAVETIME(FaceRApplacation.getContext())).getTime();//上一次更新的时间
        if(saveTime+expreseIn > DateUtil.getCurrentDate().getTime()){
            return;
        }

        RequestParams params = new RequestParams(PubInfo.getAccess_token_baidu_url);
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
                ToastManager.getInstance(FaceRApplacation.getContext()).show("获取百度AccessToken失败！");
                Logger.e(TAG,"获取百度AccessToken失败！");
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onSuccess(String result) {
                Logger.i(TAG,"AccessTokenBaidu = "+result);
                json(result);
            }
        });
    }
    private void json(String jsonResult){
        try{
            JSONObject jsonObject = new JSONObject(jsonResult);
            String access_token = jsonObject.getString("access_token");
            String session_key = jsonObject.getString("session_key");
            String refresh_token = jsonObject.getString("refresh_token");
            long expires_in = jsonObject.getInt("expires_in");

            SystemShare.setKeyBAIDUTOKEN(FaceRApplacation.getContext(),access_token,expires_in);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
