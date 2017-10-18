package com.eagle.androidlib.utils;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtil {
    /**
     * 打印Log的地址
     */
    public static String LOGPATH = null;
    /**
     * License的地址
     */
    public static String LICENSEPATH = null;
    /**
     * 人臉檢測的图片
     */
    public static String FACEDETECT = null;
    /**
     * 人脸注册的图片
     */
    public static String ADDFACEPATH = null;
    /**
     * 人脸识别的图片
     */
    public static  String FACEIDENTIFYPATH = null;
    /**
     * 初始化图片保存的路径
     */
    public static void initPICDir() {
        SystemUtil. LICENSEPATH= Environment.getExternalStorageDirectory() + "/Isss/MyLicense/";
        SystemUtil. LOGPATH = Environment.getExternalStorageDirectory() + "/Isss/LOG/";
        SystemUtil. FACEDETECT = Environment.getExternalStorageDirectory() + "/Isss/PIC/FaceDetect/";
        SystemUtil. ADDFACEPATH = Environment.getExternalStorageDirectory() + "/Isss/PIC/AddFace/";
        SystemUtil. FACEIDENTIFYPATH = Environment.getExternalStorageDirectory() + "/Isss/PIC/FaceIdentify/";
        //判断sd卡 创建临时图片存放文件夹
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            SystemUtil.makedir(SystemUtil.LOGPATH);
            SystemUtil.makedir(SystemUtil.LICENSEPATH);
            SystemUtil.makedir(SystemUtil.FACEDETECT);
            SystemUtil.makedir(SystemUtil.ADDFACEPATH);
            SystemUtil.makedir(SystemUtil.FACEIDENTIFYPATH);
        }
    }


//
//    /**
//     * 将图片移动到成功上传的路径下
//     */
//    public static void moveTmpPic2SUCCESSUPLOADPATH(List<String> imageList) {
//        if (imageList == null || imageList.size() == 0) return;
//        for (int i = 0; i < imageList.size(); i++) {
//            String picName = imageList.get(i);
//            String newName = SystemUtil.moveFile(picName, SystemUtil.SUCCESSUPLOADPATH);
//            if (newName != null) {
//                imageList.set(i, newName);
//            }
//        }
//    }



    /**
     * 创建文件夹
     *
     * @param dirPath
     */
    public static void makedir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
            Log.i("", dirPath + "创建成功");
        } else {
            Log.i("", dirPath + "已经存在，无需创建");
        }
    }

    /**
     * 移动文件位置，不修改文件名
     *
     * @param filePath
     * @param newFileDir
     * @return 返回移动后的文件全路径
     */
    public static String moveFile(String filePath, String newFileDir) {
        File file = new File(filePath);
        String newPath = newFileDir + file.getName();
        Log.i("", "移动文件" + filePath + "--->" + newPath);
        if (file.renameTo(new File(newPath))) {
            return newPath;
        } else {
            return null;
        }
    }

    /**
     * 移动文件位置，不修改文件名
     *
     * @param path
     * @param file
     * @return 返回移动后的文件全路径
     */
    public static String moveFileTOSuccess(String path, File file) {
        String newPath = path + file.getName();
        if (file.renameTo(new File(newPath))) {
            return newPath;
        } else {
            return null;
        }
    }



    /**
     * 将string转换成unicode
     *
     * @param string
     * @return
     */
    public static String string2UTF8(String string) {
        if (string == null) string = "无信息";
        String descStr = "";
        try {
            descStr = new String(java.net.URLEncoder.encode(string, "utf-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return descStr;
    }
    /**
     * 将unicode转换成string
     *
     * @param string
     * @return
     */
    public static String UTF82string(String string) {
        if (string == null) string = "无信息";
        String descStr = "";
        try {
            descStr = new String(java.net.URLDecoder.decode(string, "utf-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return descStr;
    }

    /**
     * 向图片文件中写入MODEL信息
     *
     * @param picPath
     * @param vlpNum
     * @param operID
     */
    public static void writePicExifMODEL(String picPath, String vlpNum, String operID, String vlpColor, String classno, int greenflage) {
        try {
            ExifInterface exif = new ExifInterface(picPath);
            String makeInfo = vlpNum + "," + operID + "," + vlpColor + "," + classno + "," + greenflage;   //车牌号+操作员ID+车牌颜色+班次+是否为绿通车
            Log.d("", "写入model信息：（车牌号+操作员ID+车牌颜色+班次+是否为绿通车）" + makeInfo+"----"+string2UTF8(makeInfo));
            exif.setAttribute(ExifInterface.TAG_MODEL, string2UTF8(makeInfo));
            exif.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, greenflage+"");//TAG_MODEL字段过长，无法再存入过多信息
            Log.d("", "写入TAG_APERTURE信息：（绿通标识）" + greenflage);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 向图片文件中写入MAKE信息
     *
     * @param picPath
     * @param goods
     */
    public static void writePicExifMake(String picPath, String goods, String autoVlpNum, String autoVlpColor) {
        try {
            ExifInterface exif = new ExifInterface(picPath);
            Log.d("", "写入make信息货物名称：（写入货物名称、自动识别车牌号、自动识别车牌颜色）" + goods);
            exif.setAttribute(ExifInterface.TAG_MAKE, string2UTF8(goods + "," + autoVlpNum + "," + autoVlpColor));//图片中写入货物名称、自动识别车牌号、自动识别车牌颜色
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, View v3, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            int[] lv3 = {0, 0};
            v.getLocationInWindow(l);
            v3.getLocationInWindow(lv3);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return true;
            } else if (event.getRawX() > lv3[0] && event.getRawX() < lv3[0] + v3.getWidth()
                    && event.getY() > lv3[1] && event.getY() < lv3[1] + v3.getHeight()) {
                return true;
            } else {
                return false;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public static boolean isChinese(String args) {
        byte[] bytes = args.getBytes();
        int i = bytes.length;//i为字节长度
        int j = args.length();//j为字符长度
        if (i == j) {
            return false;
        } else {
            return true;
        }
    }




    /**
     * Toast工具
     */
    private static Toast toast;
    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    /**
     * 判断是否为合法IP
     * @return the ip
     */
    public static boolean isboolIp(String ipAddress)
    {
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.find();
    }

}
