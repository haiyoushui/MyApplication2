package com.isss.liuh.myapplication.UTILS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;

import com.eagle.androidlib.utils.AppUtil;
import com.eagle.androidlib.utils.DateUtil;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.util.FaceUtil;

import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by LiuH on 2017/10/11.
 */

public class SaveFileToSD {
    public static String savPicRegPic( Camera.Size  cameras, byte[] data,String filePath){
        String fileName = AppUtil.getIMEI(FaceRApplacation.getContext())+"_"+DateUtil.getCurrentDateStrYYYYMMDDHHMMSS()+".jpeg";
        savePicToSDCard(cameras,data, filePath,fileName );
        return filePath+fileName;
    }

    /**
     * 保存图片
     * @param data
     * @throws IOException
     */
    private static void savePicToSDCard( Camera.Size  cameras,byte[] data,String savePath,String fileName)  {
        try {
            Bitmap bitmap = PicUtil.byteToBitmap(cameras,data);
            File fileFolder = new File(savePath, fileName);
            FileOutputStream outputStream = new FileOutputStream(fileFolder); // 文件输出流
            bitmap = FaceUtil.rotateImage(-90, bitmap);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close(); // 关闭输出流
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
