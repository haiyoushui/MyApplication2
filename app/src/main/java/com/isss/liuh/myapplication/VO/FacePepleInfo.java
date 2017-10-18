package com.isss.liuh.myapplication.VO;

/**
 * Created by LiuH on 2017/10/17.
 */

public class FacePepleInfo {
    private String uid;
    private double scores;//结果数组，数组元素为匹配得分，top n。得分[0,100.0]
    private String groupid;
    private String uname;
    private String uinfo;
    private String time;
    private String IMEI;
    private String sex;
    private String faceliveness;//ext_fields特殊返回信息，多个用逗号分隔，取值固定: 目前支持 faceliveness(活体检测)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getScores() {
        return scores;
    }

    public void setScores(double scores) {
        this.scores = scores;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUinfo() {
        return uinfo;
    }

    public void setUinfo(String uinfo) {
        this.uinfo = uinfo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFaceliveness() {
        return faceliveness;
    }

    public void setFaceliveness(String faceliveness) {
        this.faceliveness = faceliveness;
    }
}
