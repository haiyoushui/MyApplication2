package com.isss.liuh.myapplication.UTILS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import com.eagle.androidlib.utils.Logger;

import java.io.ByteArrayOutputStream;

import static com.iflytek.cloud.VerifierResult.TAG;

/**
 * Created by LiuH on 2017/10/11.
 */

public class PicUtil {
    /**
     * 把图片byte流编程bitmap;单纯的转换拍照的图片数据流
     * @param data
     * @return
     */
    public static Bitmap byteToBitmap( Camera.Size  previewSize,byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
     //previewSize  获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高

        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray( baos.toByteArray(), 0,  baos.toByteArray().length, options);
        return bitmap;
//

    }
    /**

     * @param bitmap      原图
     * @param   edgeLengthX 希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLengthX,int edgeLengthY )
    {
        if(null == bitmap || edgeLengthX <= 0 ||edgeLengthY <=0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLengthX && heightOrg > edgeLengthY)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLengthX * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLengthX;
            int scaledHeight = widthOrg > heightOrg ? edgeLengthY : longerEdge;
            Bitmap scaledBitmap;

            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLengthX) / 2;
            int yTopLeft = (scaledHeight - edgeLengthY) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLengthX, edgeLengthY);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

    /**
     * 圆角矩形图
     * @param bitmap
     * @param lengthX  裁剪图片的宽
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int lengthX,int lengthY){
        if(lengthX == 0){
            lengthX = 300;
        }if(lengthY == 0){
            lengthY = 300;
        }
        float roundPx = 40;//圆角的大小
        bitmap = centerSquareScaleBitmap(bitmap,lengthX,lengthY);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 圆形图片
     * @param bitmap
     * @return
     */
    public static Bitmap getOvalBitmap(Bitmap bitmap){
        bitmap = centerSquareScaleBitmap(bitmap,250,250);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
