package com.isss.liuh.myapplication.NET;

import com.eagle.androidlib.utils.AppUtil;
import com.eagle.androidlib.utils.DateUtil;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.UTILS.JsonUtil;
import com.isss.liuh.myapplication.UTILS.PubInfo;
import com.isss.liuh.myapplication.UTILS.utils;
import com.isss.liuh.myapplication.VO.FacePepleInfo;
import android.location.Location;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuH on 2017/10/13.
 */

public class HeadUtil {
    /**
     * 百度添加人脸，或替换人脸
     * @param fileSrc
     * @param uid
     * @param user_info
     * @param isReplace
     * @return
     */
    public static  RequestParams addFace(String fileSrc, String uid, String user_info, boolean isReplace) {
        HashMap<Integer,String> filePathMap = new HashMap<>();
        filePathMap.put(0,fileSrc);
        return addFace(filePathMap,  uid,  user_info,  isReplace);
    }
    public static RequestParams addFace(HashMap<Integer,String> fileSrc, String uid, String user_info, boolean isReplace){
        RequestParams params;
        if(isReplace){
            params = new RequestParams( PubInfo.URL_Baidu_Updata + "?access_token=" + SystemShare.getBAIDUTOKEN(FaceRApplacation.getContext()));
            params.addBodyParameter("action_type", "replace");
        }else {
            params = new RequestParams( PubInfo.URL_Baidu_Add + "?access_token=" + SystemShare.getBAIDUTOKEN(FaceRApplacation.getContext()));
            params.addBodyParameter("action_type", "append");
        }

        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("uid", uid);//用户id（由数字、字母、下划线组成），长度限制128B
        params.addBodyParameter("user_info", user_info);//用户资料，长度限制256B
        params.addBodyParameter("group_id", PubInfo.BaiduFace_group_id);//用户组id，标识一组用户（由数字、字母、下划线组成），长度限制128B。如果需要将一个uid注册到多个group下，group_id需要用多个逗号分隔，每个group_id长度限制为48个英文字符
        String picFileString = null;
        for(int i=0;i<fileSrc.size();i++){
            if(picFileString == null){
                picFileString  = utils.fileToBase64(fileSrc.get(i));
            }else {
                picFileString  = picFileString+","+utils.fileToBase64(fileSrc.get(i));
            }

        }
        params.addBodyParameter("image", picFileString);
        if(isReplace){
            params.addBodyParameter("action_type", "replace");
        }else {
            params.addBodyParameter("action_type", "append");
        }
        params.setMultipart(true);
        return params;
    }

    /**
     * 百度人脸检测，获得年龄表情种族的信息
     * @param fileSrc
     * @return
     */
    public static RequestParams detectFace(String fileSrc){
        RequestParams params = new RequestParams(PubInfo.URL_Baidu_Detect + "?access_token=" + SystemShare.getBAIDUTOKEN(FaceRApplacation.getContext()));
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("image", utils.fileToBase64(fileSrc));
        params.addBodyParameter("face_fields", "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities");
        params.setMultipart(true);
        return params;
    }
    /**
     * 百度人脸识别请求参数
     * @param fileSrc
     * @param userTopNum
     * @return
     */
    public static RequestParams identifyFace(String fileSrc,String userTopNum){
        RequestParams params = new RequestParams(com.isss.liuh.myapplication.UTILS.PubInfo.URL_Baidu_Identify + "?access_token=" + SystemShare.getBAIDUTOKEN(FaceRApplacation.getContext()));
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("user_top_num", userTopNum);//返回个数
        params.addBodyParameter("ext_fields", "faceliveness");//是否为活体
        params.addBodyParameter("group_id", PubInfo.BaiduFace_group_id);//用户组id，标识一组用户（由数字、字母、下划线组成），长度限制128B。如果需要将一个uid注册到多个group下，group_id需要用多个逗号分隔，每个group_id长度限制为48个英文字符
        params.addBodyParameter("image", utils.fileToBase64(fileSrc));
        return params;
    }
    public static RequestParams verification(String fileSrc,String uid,String uname){
        RequestParams params = new RequestParams(PubInfo.URL_LinkFace_Verification);
        File file = new File(fileSrc);
        params.addHeader("Content-Type", "multipart/form-data");
        params.addBodyParameter("api_id", PubInfo.LinkFace_APIKey);
        params.addBodyParameter("api_secret", PubInfo.LinkFace_SecretKey);
        params.addBodyParameter("id_number", uid);
        params.addBodyParameter("name", uname);
        params.addBodyParameter("selfie_file",file);
         return params;
    }

    /**
     * 向后台发送身份证号，查询信息
     * @param idcardid
     * @return
     */
    public static RequestParams IDCardIdSearch(String idcardid){
        RequestParams params = new RequestParams(PubInfo.URL__Service_IDCard_Search+ idcardid);
        params.addHeader("IMEI",PubInfo.IMEI); // 为当前请求添加一个头,设备编号
//        Location location = AppUtil.getLocation(FaceRApplacation.getContext());
//        params.addHeader("location","经度："+location.getLongitude()+"纬度："+location.getLatitude()); // 为当前请求添加一个头,设备编号
        return params;
    }

    public static  RequestParams sendPepleInfoToService(FacePepleInfo facePepleInfo){
        RequestParams params = new RequestParams(PubInfo.URL_Service_AddFace);
        params.addHeader("Content-Type", "application/json;charset=UTF-8");
        params.addHeader("IMEI",PubInfo.IMEI); // 为当前请求添加一个头,设备编号
//        Location location = AppUtil.getLocation(FaceRApplacation.getContext());
//        params.addHeader("location","经度："+location.getLongitude()+"纬度："+location.getLatitude()); // 为当前请求添加一个头,设备编号
        params.addBodyParameter("pepleinfo", JsonUtil.faceIngo2Json(facePepleInfo).toString());
        return params;
    }
    public static RequestParams getLicense(){
        RequestParams params = new RequestParams(PubInfo.URL_Service_License);
        params.addHeader("Content-Type", "application/json;charset=UTF-8");
        params.addHeader("IMEI",PubInfo.IMEI); // 为当前请求添加一个头,设备编号
//        Location location = AppUtil.getLocation(FaceRApplacation.getContext());
//        params.addHeader("location","经度："+location.getLongitude()+"纬度："+location.getLatitude()); // 为当前请求添加一个头,设备编号

        params.addBodyParameter("SysVersion",AppUtil.getAndroidSysVersion());
        params.addBodyParameter("PhoneModel",AppUtil.getPhoneModel());
        params.addBodyParameter("Time", DateUtil.getCurrentDateTimeStr());
        params.addBodyParameter("Version", AppUtil.getVersionName(FaceRApplacation.getContext()));
        params.addBodyParameter("Remark", "");

        return params;
    }
}
