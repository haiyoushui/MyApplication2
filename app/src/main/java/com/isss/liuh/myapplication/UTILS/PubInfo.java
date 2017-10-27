package com.isss.liuh.myapplication.UTILS;

import java.util.Date;

/**
 * Created by LiuH on 2017/10/11.
 */

public class PubInfo {
    /**
     * 百度appkey
     */
    public static final String Baidu_APIKey = "5hG2XFuNWNg0VkPxSYUOIzR3";
    /**
     * 百度SecretKey
     */
    public static final String Baidu_SecretKey = "9C4G7BjXdaVaXH1SY27QQObzEWz3xw3m";

    /**
     * LinkFaceappkey
     */
    public static final String LinkFace_APIKey = "5d9bca009efa4770b67969483f3569f1";
    /**
     * LinkFaceSecretKey
     */
    public static final String LinkFace_SecretKey = "e84729e6884f4a46a6112d8e20a064b3";


    /**
     * 获取百度百度的access_token的URL
     */
    public static final String getAccess_token_baidu_url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + Baidu_APIKey+"&client_secret=" + Baidu_SecretKey;
    /**
     * 获取百度百度的access_token的定时器间隔时间20天(30天过期，提前10天更新）
     */
    public static final long getAccess_token_baidu_timer = 20 * 24 * 60 * 60 * 1000;


    /**
     * 百度人脸检测
     */
    public static final String URL_Baidu_Detect = "https://aip.baidubce.com/rest/2.0/face/v1/detect";
    /**
     * 百度人脸添加
     */
    public static final String URL_Baidu_Add = "https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add";
    public static final String URL_Baidu_Updata= "https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/update";
    /**
     * 百度人脸识别
     */
    public static final String URL_Baidu_Identify = "https://aip.baidubce.com/rest/2.0/face/v2/identify";
    public static final String BaiduFace_group_id = "ISSS_2017_T";//百度人脸库的组名称
    /**
     * 百度身份证信息扫描
     */

    public static final String BaiduFace_IDCard_url= "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
    /**
     * LinkFace 检测身份证信息与照片是否同一人
     */
    public static final String URL_LinkFace_Verification = "https://cloudapi.linkface.cn/identity/liveness_idnumber_verification";
    private static  String URL_Server_IP = "http://weixinliuhao.tunnel.qydev.com/WeChatRobot/";
    /**
     * 后台搜索身份证信息
     */
    public static final String URL__Service_IDCard_Search = URL_Server_IP+"IDCard/search/";
    /**
     * 后台接收添加人脸库的信息
     */
    public static final String URL_Service_AddFace = URL_Server_IP+"FaceRC/addFaceInfo/";
    /**
     * 后台接收添加人脸库的图片
     */
    public static final String URL_Service_AddFacePic = URL_Server_IP+"FaceRC/addFaceFile/";

}
