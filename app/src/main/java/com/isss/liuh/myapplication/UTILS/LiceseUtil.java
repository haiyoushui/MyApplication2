package com.isss.liuh.myapplication.UTILS;

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
    public static int IsAllow(){
        AllowLicense allowLicense = JsonUtil.JsonToAllowLicense();
        if(AppUtil.getIMEI(FaceRApplacation.getContext())!=allowLicense.getIMEI() && AppUtil.getIMEI(FaceRApplacation.getContext()).equals(allowLicense.getIMEI())){
            Logger.e("设备号与请求设备号不匹配！");
            return 2;
        }else if(isInAllowTime(allowLicense.getStartTime(),allowLicense.getEndTime())){
            Logger.e("使用权限已过期！");
            return 3;
        }else if(allowLicense.getIsAllow() != 1){
            Logger.e("此设备没有使用权限！");
            return 3;
        }else {
            return 1;
        }
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


