package com.isss.liuh.myapplication.SERVICE;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.isss.liuh.myapplication.UTILS.PubInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by LiuH on 2017/10/26.
 */

public class SendPicToServiceServer extends Service{
    private static final String TAG = "ImageSender";
    private int uploadPicTimer = 30 * 1000;//上传图片的时间间隔


    private static Thread uploda_thread;


    @Override
    public void onCreate() {
        Log.v(TAG, "ServiceDemo onCreate");
        // 初始化图片存储对象
        instance().beginSend();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Logger.i(TAG, "启动services");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "ServiceDemo onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }



    public static SendPicToServiceServer instance() {
        return m_instance;
    }
    private final static SendPicToServiceServer m_instance = new SendPicToServiceServer();

    /**
     * 开始发送数据
     */
    public void beginSend() {
        uploda_thread = new Thread(null, sendDataRunnable, "senddata");
        uploda_thread.start();
    }


    private Runnable sendDataRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.i(TAG,"开启上传图片线程");
            uploadPic();
        }
    };


    private void uploadPic() {
        while (true) {
            int count = 0;
            File file = new File(SystemUtil.ADDFACESUCCESSPATH);
            File[] filesPic = file.listFiles();
            try {
                if (filesPic.length == 0) {
                    Thread.sleep(uploadPicTimer);
                } else {
                    while (count < filesPic.length) {
                        Logger.i(TAG, "循环图片"+filesPic[count].getName());
                        uploadPic(filesPic[count]);
                        while (isSendImage) {
                            Thread.sleep(1000);
                        }
                        count++;
                    }

                }
                try{
                    Thread.sleep(500);
                }catch (Exception e){//防止已上传成功的图片还未转移文件夹完成，又被算成未上传的图片，给转移图片留点时间
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





    private volatile boolean isSendImage = false;//防止多线程从寄存器中取

    private File fileSuc;

    private void  uploadPic(File file) {
        isSendImage = true;
        fileSuc = file;
        // 请求参数
        Logger.i(TAG, "开始发送图片"+file.getName());
        String url = PubInfo.URL_Service_AddFacePic;
        if (url == null) {
            isSendImage = false;
            return;
        }

        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addHeader("Content-Type", "multipart/form-data");
        params.addBodyParameter("multipartFile", file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                Logger.e(TAG, fileSuc.getName()+"发送图片CancelledException"+arg0);
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Logger.e(TAG, fileSuc.getName()+"发送图片Error"+arg0);
            }

            @Override
            public void onFinished() {
                isSendImage = false;
                Logger.i(TAG, fileSuc.getName()+"结束发送图片");
            }

            @Override
            public void onSuccess(String result) {
                Logger.i(TAG, fileSuc.getName()+"发送成功"+ result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getBoolean("success")){
                        fileSuc.delete();
                    }else{
                        SystemUtil.moveFile(fileSuc.getAbsolutePath(),SystemUtil.FACERRORPICPATH);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
