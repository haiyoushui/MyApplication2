package com.isss.liuh.myapplication.UTILS;

import android.content.Context;

import com.eagle.androidlib.utils.AppUtil;
import com.eagle.androidlib.utils.DateUtil;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.NET.HeadUtil;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.VO.AllowLicense;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuH on 2017/10/27.
 */

public class LiceseUtil {

    public static  void getLiceseAndSave(){

            RequestParams params = HeadUtil.getLicense( );
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onCancelled(CancelledException arg0) {
                }
                @Override
                public void onError(Throwable arg0, boolean arg1) {
                }
                @Override
                public void onFinished() {
                }
                @Override
                public void onSuccess(String result) {
                    try {
                        Logger.i("获取权限"+result);
                        JSONObject jsonObject = new JSONObject(result);
                        SystemShare.setKeyLicenseC(FaceRApplacation.getContext(),jsonObject.getString("data"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
    public static  Map<String,Integer> IsAllow(Context context){
        Map<String,Integer> allow = new HashMap<>();
        AllowLicense allowLicense = JsonUtil.JsonToAllowLicense();
        if(AppUtil.getIMEI(context)!=allowLicense.getIMEI() && !AppUtil.getIMEI(context).equals(allowLicense.getIMEI())){
            Logger.e("设备号与请求设备号不匹配！"+AppUtil.getIMEI(context)+"!="+allowLicense.getIMEI());
            allow.put("main",2);
        }else if(!isInAllowTime(allowLicense.getStartTime(),allowLicense.getEndTime())){
            Logger.e("使用权限已过期！"+allowLicense.getStartTime()+"——"+allowLicense.getEndTime());
            allow.put("main",3);
        }else if(allowLicense.getIsAllow() != 1){
            Logger.e("此设备没有使用权限！");
            allow.put("main",4);
        }else {
            allow.put("main",1);
        }
        allow.put("func1",allowLicense.getFunction1());
        allow.put("func2",allowLicense.getFunction2());
        allow.put("func3",allowLicense.getFunction3());
        allow.put("func4",allowLicense.getFunction4());
        allow.put("func5",allowLicense.getFunction5());
        allow.put("func6",allowLicense.getFunction6());
        allow.put("func7",allowLicense.getFunction7());
        allow.put("func8",allowLicense.getFunction8());
        allow.put("func9",allowLicense.getFunction9());
        return allow;
    }
    private static boolean isInAllowTime(String startTime,String endTime){
        Date date = new Date();
        if( DateUtil.parseDateTime(startTime).getTime() <= date.getTime() && DateUtil.parseDateTime(endTime).getTime() >= date.getTime()){
            return true;
        }else {
            return false;
        }
    }
    }


