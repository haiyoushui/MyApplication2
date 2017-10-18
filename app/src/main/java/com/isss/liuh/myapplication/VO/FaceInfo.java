package com.isss.liuh.myapplication.VO;

/**
 * Created by LiuH on 2017/10/13.
 */

public class FaceInfo {
    private int resultNum;//人脸个数
    private double faceLeft;//脸部距离左边框
    private double faceTop;//脸部距离上边框
    private double facewidth;//脸部宽
    private double faceheight;//脸部高度
    private double faceProbability;//人脸置信
    private double faceRotationAngle;//人脸框相对于竖直方向的顺时针旋转角，[-180,180]
    private double faceYaw;//三维旋转之左右旋转角[-90(左), 90(右)]
    private double facePitch;//三维旋转之俯仰角度[-90(上), 90(下)]
    private double faceRoll;//平面内旋转角[-180(逆时针), 180(顺时针)]
    private double faceLeftEyeX;//左眼中心X坐标
    private double faceLeftEyeY;//左眼中心Y坐标
    private double faceRightEyeX;//右眼中心X坐标
    private double faceRightEyeY;//右眼中心Y坐标
    private double faceNoseX;//鼻子X坐标
    private double faceNoseY;//鼻子Y坐标
    private double faceMouthX;//嘴X坐标
    private double faceMouthY;//嘴Y坐标
    private String FaceLandMark72;//其他72个关键点的坐标
    private double age;//年龄
    private double beauty;//美丑打分，范围0-100，越大表示越美。face_fields包含beauty时返回
    private String faceExpression;//表情，0，不笑；1，微笑；2，大笑。face_fields包含expression时返回
    private double faceExpressionProbablity;//表情置信度，范围0~1。face_fields包含expression时返回
    private String faceTypeString;//脸型square/triangle/oval/heart/round（未解析的）
    private String faceType;//脸型
    private String faceGender;//性别
    private String faceGlasses;//眼镜
    private String faceRace;//人种
    private String faceQualities;//人脸质量，人脸各部分遮挡的概率， [0, 1] （未解析的）

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    public double getFaceLeft() {
        return faceLeft;
    }

    public void setFaceLeft(double faceLeft) {
        this.faceLeft = faceLeft;
    }

    public double getFaceTop() {
        return faceTop;
    }

    public void setFaceTop(double faceTop) {
        this.faceTop = faceTop;
    }

    public double getFacewidth() {
        return facewidth;
    }

    public void setFacewidth(double facewidth) {
        this.facewidth = facewidth;
    }

    public double getFaceheight() {
        return faceheight;
    }

    public void setFaceheight(double faceheight) {
        this.faceheight = faceheight;
    }

    public double getFaceProbability() {
        return faceProbability;
    }

    public void setFaceProbability(double faceProbability) {
        this.faceProbability = faceProbability;
    }

    public double getFaceRotationAngle() {
        return faceRotationAngle;
    }

    public void setFaceRotationAngle(double faceRotationAngle) {
        this.faceRotationAngle = faceRotationAngle;
    }

    public double getFaceYaw() {
        return faceYaw;
    }

    public void setFaceYaw(double faceYaw) {
        this.faceYaw = faceYaw;
    }

    public double getFacePitch() {
        return facePitch;
    }

    public void setFacePitch(double facePitch) {
        this.facePitch = facePitch;
    }

    public double getFaceRoll() {
        return faceRoll;
    }

    public void setFaceRoll(double faceRoll) {
        this.faceRoll = faceRoll;
    }

    public double getFaceLeftEyeX() {
        return faceLeftEyeX;
    }

    public void setFaceLeftEyeX(double faceLeftEyeX) {
        this.faceLeftEyeX = faceLeftEyeX;
    }

    public double getFaceLeftEyeY() {
        return faceLeftEyeY;
    }

    public void setFaceLeftEyeY(double faceLeftEyeY) {
        this.faceLeftEyeY = faceLeftEyeY;
    }

    public double getFaceRightEyeX() {
        return faceRightEyeX;
    }

    public void setFaceRightEyeX(double faceRightEyeX) {
        this.faceRightEyeX = faceRightEyeX;
    }

    public double getFaceRightEyeY() {
        return faceRightEyeY;
    }

    public void setFaceRightEyeY(double faceRightEyeY) {
        this.faceRightEyeY = faceRightEyeY;
    }

    public double getFaceNoseX() {
        return faceNoseX;
    }

    public void setFaceNoseX(double faceNoseX) {
        this.faceNoseX = faceNoseX;
    }

    public double getFaceNoseY() {
        return faceNoseY;
    }

    public void setFaceNoseY(double faceNoseY) {
        this.faceNoseY = faceNoseY;
    }

    public double getFaceMouthX() {
        return faceMouthX;
    }

    public void setFaceMouthX(double faceMouthX) {
        this.faceMouthX = faceMouthX;
    }

    public double getFaceMouthY() {
        return faceMouthY;
    }

    public void setFaceMouthY(double faceMouthY) {
        this.faceMouthY = faceMouthY;
    }

    public String getFaceLandMark72() {
        return FaceLandMark72;
    }

    public void setFaceLandMark72(String faceLandMark72) {
        FaceLandMark72 = faceLandMark72;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getBeauty() {
        return beauty;
    }

    public void setBeauty(double beauty) {
        this.beauty = beauty;
    }

    public String getFaceExpression() {
        return faceExpression;
    }

    public void setFaceExpression(String faceExpression) {
        this.faceExpression = faceExpression;
    }

    public double getFaceExpressionProbablity() {
        return faceExpressionProbablity;
    }

    public void setFaceExpressionProbablity(double faceExpressionProbablity) {
        this.faceExpressionProbablity = faceExpressionProbablity;
    }

    public String getFaceTypeString() {
        return faceTypeString;
    }

    public void setFaceTypeString(String faceTypeString) {
        this.faceTypeString = faceTypeString;
    }

    public String getFaceType() {
        return faceType;
    }

    public void setFaceType(String faceType) {
        this.faceType = faceType;
    }

    public String getFaceGender() {
        return faceGender;
    }

    public void setFaceGender(String faceGender) {
        this.faceGender = faceGender;
    }

    public String getFaceGlasses() {
        return faceGlasses;
    }

    public void setFaceGlasses(String faceGlasses) {
        this.faceGlasses = faceGlasses;
    }

    public String getFaceRace() {
        return faceRace;
    }

    public void setFaceRace(String faceRace) {
        this.faceRace = faceRace;
    }

    public String getFaceQualities() {
        return faceQualities;
    }

    public void setFaceQualities(String faceQualities) {
        this.faceQualities = faceQualities;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "resultNum=" + resultNum +
                ", faceLeft=" + faceLeft +
                ", faceTop=" + faceTop +
                ", facewidth=" + facewidth +
                ", faceheight=" + faceheight +
                ", faceProbability=" + faceProbability +
                ", faceRotationAngle=" + faceRotationAngle +
                ", faceYaw=" + faceYaw +
                ", facePitch=" + facePitch +
                ", faceRoll=" + faceRoll +
                ", faceLeftEyeX=" + faceLeftEyeX +
                ", faceLeftEyeY=" + faceLeftEyeY +
                ", faceRightEyeX=" + faceRightEyeX +
                ", faceRightEyeY=" + faceRightEyeY +
                ", faceNoseX=" + faceNoseX +
                ", faceNoseY=" + faceNoseY +
                ", faceMouthX=" + faceMouthX +
                ", faceMouthY=" + faceMouthY +
                ", FaceLandMark72='" + FaceLandMark72 + '\'' +
                ", age=" + age +
                ", beauty=" + beauty +
                ", faceExpression='" + faceExpression + '\'' +
                ", faceExpressionProbablity=" + faceExpressionProbablity +
                ", faceTypeString='" + faceTypeString + '\'' +
                ", faceType='" + faceType + '\'' +
                ", faceGender='" + faceGender + '\'' +
                ", faceGlasses='" + faceGlasses + '\'' +
                ", faceRace='" + faceRace + '\'' +
                ", faceQualities='" + faceQualities + '\'' +
                '}';
    }
}
