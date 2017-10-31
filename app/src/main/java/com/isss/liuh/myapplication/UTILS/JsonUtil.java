package com.isss.liuh.myapplication.UTILS;



import com.eagle.androidlib.utils.AppUtil;
import com.eagle.androidlib.utils.DateUtil;
import com.eagle.androidlib.utils.Logger;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.VO.AllowLicense;
import com.isss.liuh.myapplication.VO.FaceInfo;
import com.isss.liuh.myapplication.VO.FacePepleInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by LiuH on 2017/10/13.
 */

public class JsonUtil {
    /**
     * 解析百度人脸检测获取的人脸信息
     * @param json
     * @return
     */
    public static FaceInfo jsonToFaceInfo(String json){
        FaceInfo faceInfo = new FaceInfo();
        try{
            JSONObject jsonObject = new JSONObject(json);
            faceInfo.setResultNum(jsonObject.getInt("result_num"));
            JSONArray jsonArrayRsult = jsonObject.getJSONArray("result");

            JSONObject jsonResult0 = jsonArrayRsult.getJSONObject(0);
            faceInfo.setFaceLeft(jsonResult0.getJSONObject("location").getDouble("left"));
            faceInfo.setFaceTop(jsonResult0.getJSONObject("location").getDouble("top"));
            faceInfo.setFacewidth(jsonResult0.getJSONObject("location").getDouble("width"));
            faceInfo.setFaceheight(jsonResult0.getJSONObject("location").getDouble("height"));

            faceInfo.setFaceProbability(jsonResult0.getDouble("face_probability"));
            faceInfo.setFaceRotationAngle(jsonResult0.getDouble("rotation_angle"));
            faceInfo.setFaceYaw(jsonResult0.getDouble("yaw"));
            faceInfo.setFacePitch(jsonResult0.getDouble("pitch"));
            faceInfo.setFaceRoll(jsonResult0.getDouble("roll"));

            faceInfo.setFaceLeftEyeX(jsonResult0.getJSONArray("landmark").getJSONObject(0).getDouble("x"));
            faceInfo.setFaceLeftEyeY(jsonResult0.getJSONArray("landmark").getJSONObject(0).getDouble("y"));
            faceInfo.setFaceRightEyeX(jsonResult0.getJSONArray("landmark").getJSONObject(1).getDouble("x"));
            faceInfo.setFaceRightEyeY(jsonResult0.getJSONArray("landmark").getJSONObject(1).getDouble("y"));
            faceInfo.setFaceNoseX(jsonResult0.getJSONArray("landmark").getJSONObject(2).getDouble("x"));
            faceInfo.setFaceNoseY(jsonResult0.getJSONArray("landmark").getJSONObject(2).getDouble("y"));
            faceInfo.setFaceMouthX(jsonResult0.getJSONArray("landmark").getJSONObject(3).getDouble("x"));
            faceInfo.setFaceMouthY(jsonResult0.getJSONArray("landmark").getJSONObject(3).getDouble("y"));

            faceInfo.setFaceLandMark72(jsonResult0.getString("landmark"));
            faceInfo.setAge(jsonResult0.getDouble("age"));
            faceInfo.setBeauty(jsonResult0.getDouble("beauty"));
            faceInfo.setFaceTypeString(jsonResult0.getString("faceshape"));
            faceInfo.setFaceType(faceType(jsonResult0.getString("faceshape")));
            faceInfo.setFaceExpressionProbablity(jsonResult0.getDouble("expression_probablity"));
            if(0==jsonResult0.getInt("expression")){
                faceInfo.setFaceExpression("不笑");
            }else if(1==jsonResult0.getInt("expression")){
                faceInfo.setFaceExpression("微笑");
            }else {
                faceInfo.setFaceExpression("大笑");
            }

            if("male".equals(jsonResult0.getString("gender"))||"male"==jsonResult0.getString("gender")){
                faceInfo.setFaceGender("男");
            }else {
                faceInfo.setFaceGender("女");
            }


            if("1".equals(jsonResult0.getString("glasses"))||"1"==jsonResult0.getString("glasses")){
                faceInfo.setFaceGlasses("普通眼镜");
            }else if("0".equals(jsonResult0.getString("glasses"))||"0"==jsonResult0.getString("glasses")){
                faceInfo.setFaceGlasses("墨镜");
            }else {
                faceInfo.setFaceGlasses("无");
            }


            if("white".equals(jsonResult0.getString("race"))||"white"==jsonResult0.getString("race")){
                faceInfo.setFaceRace("白种人");
            }else if("black".equals(jsonResult0.getString("race"))||"black"==jsonResult0.getString("race")){
                faceInfo.setFaceRace("黑种人");
            }else if("arabs".equals(jsonResult0.getString("race"))||"arabs"==jsonResult0.getString("race")){
                faceInfo.setFaceRace("阿拉伯人");
            }else {
                faceInfo.setFaceRace("黄种人");
            }


            faceInfo.setFaceQualities(jsonResult0.getString("qualities"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return faceInfo;
    }

    /**
     * 判断人脸类型
     * @param faceType
     * @return
     */
    private static String faceType(String faceType){
        String faceShap = null;
        double probability = 0;
        try{
            JSONArray json = new JSONArray(faceType);
            for(int i=0;i<json.length();i++){
                if(json.getJSONObject(i).getDouble("probability") > probability){
                    faceShap = json.getJSONObject(i).getString("type");
                    probability = json.getJSONObject(i).getDouble("probability");
                }
            }
            Logger.e("faceShap", "人脸形状！"+faceShap);
            switch (faceShap){
                case "square":
                    faceShap = "方形脸";
                    break;
                case "triangle":
                    faceShap = "三角脸";
                    break;
                case "oval":
                    faceShap = "椭圆脸";
                    break;
                case "heart":
                    faceShap = "瓜子脸";
                    break;
                case "round":
                    faceShap = "圆脸";
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return faceShap;
    }

    /**
     * 将注册的人脸信息封装成JSON
     */
    public static JSONObject faceIngo2Json(String uname,String uinfo,String sex){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("uname",uname);
            jsonObject.put("sex",sex);
//            jsonObject.put("birthday".)
            jsonObject.put("uinfo",uinfo);
            jsonObject.put("time", DateUtil.getCurrentDateStrYYYYMMDDHHMMSS());
            jsonObject.put("IMEI", AppUtil.getIMEI(FaceRApplacation.getContext()));
        }catch (Exception e){
            e.printStackTrace();
        }
    return jsonObject;
    }
    /**
     * 将注册的人脸信息封装成JSON，发给己方后台
     */
    public static JSONObject faceIngo2Json(FacePepleInfo facePepleInfo){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("groupid",PubInfo.BaiduFace_group_id);//百度人脸库中groupID
            jsonObject.put("uname",facePepleInfo.getUname()+"");
            jsonObject.put("gender",facePepleInfo.getGender()+"");
            jsonObject.put("birthday",facePepleInfo.getBirthday()+"");
            jsonObject.put("uinfo",facePepleInfo.getUinfo()+"");
            jsonObject.put("address",facePepleInfo.getAddress()+"");
            jsonObject.put("idcardaddress",facePepleInfo.getIDCardAddress()+"");
            jsonObject.put("ethnic",facePepleInfo.getEthnic()+"");
            jsonObject.put("idcardid",facePepleInfo.getIDCardId()+"");
            jsonObject.put("uid",facePepleInfo.getUid()+"");
            jsonObject.put("time", DateUtil.getCurrentDateStrYYYYMMDDHHMMSS()+"");
            jsonObject.put("IMEI", AppUtil.getIMEI(FaceRApplacation.getContext()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 将注册的人脸信息封装成JSON 当做user_info上传给百度
     */
    public static JSONObject faceIngo2JsonBaidu(FacePepleInfo facePepleInfo){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("uname",facePepleInfo.getUname()+"");
            jsonObject.put("gender",facePepleInfo.getGender()+"");
            jsonObject.put("birthday",facePepleInfo.getBirthday()+"");
            jsonObject.put("uinfo",facePepleInfo.getUinfo()+"");
            jsonObject.put("address",facePepleInfo.getAddress()+"");
            jsonObject.put("idcardaddress",facePepleInfo.getIDCardAddress()+"");
            jsonObject.put("time", DateUtil.getCurrentDateStrYYYYMMDDHHMMSS()+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 将人脸识别的返回结果解析
     */
    public static FacePepleInfo faceInfo2FacePeple(String jsonStr){
        FacePepleInfo facePepleInfo = new FacePepleInfo();
      try {
          JSONObject jsonObject = new JSONObject(jsonStr);
          JSONObject jsonResult = jsonObject.getJSONArray("result").getJSONObject(0);
          facePepleInfo.setUid(jsonResult.getString("uid"));
          facePepleInfo.setScores(jsonResult.getJSONArray("scores").getDouble(0));
          facePepleInfo.setGroupid(jsonResult.getString("group_id"));
          JSONObject jsonUserInfo = new JSONObject(jsonResult.getString("user_info"));
          facePepleInfo.setUname(jsonUserInfo.getString("uname"));
          facePepleInfo.setUinfo(jsonUserInfo.getString("uinfo"));
          facePepleInfo.setAddress(jsonUserInfo.getString("address"));
          facePepleInfo.setIDCardAddress(jsonUserInfo.getString("idcardaddress"));
          facePepleInfo.setBirthday(jsonUserInfo.getString("birthday"));
          facePepleInfo.setTime(jsonUserInfo.getString("time"));
          facePepleInfo.setGender(jsonUserInfo.getString("gender"));
          facePepleInfo.setFaceliveness(jsonObject.getJSONArray("ext_info").getJSONObject(0).getString("faceliveness"));
      }catch (Exception e){
          e.printStackTrace();
      }
      return facePepleInfo;
    }
    public static AllowLicense JsonToAllowLicense(){

        AllowLicense allowLicense = new AllowLicense();
        try{
            JSONObject jsonObject = new JSONObject(SystemShare.getLicense(FaceRApplacation.getContext()));
            allowLicense.setIsAllow(jsonObject.getInt("isAllow"));
            allowLicense.setStartTime(jsonObject.getString("startTime"));
            allowLicense.setEndTime(jsonObject.getString("endTime"));
            allowLicense.setFunction1(jsonObject.getInt("function1"));
            allowLicense.setFunction2(jsonObject.getInt("function2"));
            allowLicense.setFunction3(jsonObject.getInt("function3"));
            allowLicense.setFunction4(jsonObject.getInt("function4"));
            allowLicense.setFunction5(jsonObject.getInt("function5"));
            allowLicense.setFunction6(jsonObject.getInt("function6"));
            allowLicense.setFunction7(jsonObject.getInt("function7"));
            allowLicense.setFunction8(jsonObject.getInt("function8"));
            allowLicense.setFunction9(jsonObject.getInt("function9"));
            allowLicense.setRemark(jsonObject.getString("remark"));
            allowLicense.setIMEI(jsonObject.getString("imei"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return allowLicense;
    }

}
