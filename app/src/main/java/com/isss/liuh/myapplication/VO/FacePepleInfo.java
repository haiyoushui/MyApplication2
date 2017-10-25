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
    private String gender;
    private String faceliveness;//ext_fields特殊返回信息，多个用逗号分隔，取值固定: 目前支持 faceliveness(活体检测)
    private String address;
    private String birthday;
    private String IDCardId;
    private String IDCardAddress;
    private String ethnic;//民族

    public String getIDCardAddress() {
        return IDCardAddress;
    }

    public void setIDCardAddress(String IDCardAddress) {
        this.IDCardAddress = IDCardAddress;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getIDCardId() {
        return IDCardId;
    }

    public void setIDCardId(String IDCardId) {
        this.IDCardId = IDCardId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFaceliveness() {
        return faceliveness;
    }

    public void setFaceliveness(String faceliveness) {
        this.faceliveness = faceliveness;
    }
}
