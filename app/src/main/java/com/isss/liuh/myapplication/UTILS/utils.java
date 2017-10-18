package com.isss.liuh.myapplication.UTILS;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by LiuH on 2017/10/13.
 */

public class utils {
    public static String fileToBase64(String picPath){
           return Base64.encodeToString(File2Bytes(picPath), Base64.DEFAULT);
    }
    private static byte[] File2Bytes(String  picPath) {
        File file = new File(picPath);
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1;) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
