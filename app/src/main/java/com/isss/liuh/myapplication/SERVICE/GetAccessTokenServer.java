package com.isss.liuh.myapplication.SERVICE;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.eagle.androidlib.utils.Logger;

import static android.content.ContentValues.TAG;

/**
 * 定期获取Access_Token
 * 30天过期
 * Created by LiuH on 2017/10/12.
 */

public class GetAccessTokenServer  extends Service {

    @Override
    public void onCreate() {
        Logger.i(TAG, "ServiceDemo onCreate");
        // 初始化图片存储对象
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Logger.i(TAG, "启动services");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "ServiceDemo onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
