package com.isss.liuh.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.eagle.androidlib.utils.DateUtil;
import com.eagle.androidlib.utils.SystemUtil;
import com.iflytek.cloud.SpeechUtility;
import com.isss.liuh.myapplication.SERVICE.SendPicToServiceServer;
import com.isss.liuh.myapplication.TimerTask.GetAccessTokenTimerTask;
import com.isss.liuh.myapplication.UTILS.PubInfo;

import org.xutils.x;

import java.util.Timer;


public class FaceRApplacation extends Application {
	private static Context context;
	public static Context getContext() {
		return context;
	}
	@Override
	public void onCreate() {
		context = this;
		// 初始化Xutil3
		x.Ext.init(this);
		// 设置是否输出debug
		x.Ext.setDebug(true);

		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角“,”分隔。
		// 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符
		// 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
		
		SpeechUtility.createUtility(FaceRApplacation.this, "appid=" + getString(R.string.app_id));
		// 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
		// Setting.setShowLog(false);
//		Fresco.initialize(getApplicationContext());
		super.onCreate();
		SystemUtil.initPICDir();
		//开启定时获取AccessToken的定时器
		GetAccessTokenTimerTask timerA = new GetAccessTokenTimerTask();
		Timer timerGetAcceToken = new Timer();
		timerGetAcceToken.schedule(timerA,
				DateUtil.parseDateTime("2016-11-16 09:00:00"),
				PubInfo.getAccess_token_baidu_timer);
		//开启发送图片服务
		startService(new Intent(getContext(),SendPicToServiceServer.class));

	}

}
