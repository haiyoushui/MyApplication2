package com.isss.liuh.myapplication;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.eagle.androidlib.utils.Logger;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.util.Accelerometer;
import com.isss.liuh.myapplication.UTILS.PicUtil;
import com.isss.liuh.myapplication.UTILS.SaveFileToSD;
import com.isss.liuh.myapplication.util.FaceRect;
import com.isss.liuh.myapplication.util.FaceUtil;
import com.isss.liuh.myapplication.util.ParseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.isss.liuh.myapplication.UTILS.SaveFileToSD.savPicRegPic;


/**
 * 离线视频流检测示例
 * 该业务仅支持离线人脸检测SDK，请开发者前往<a href="http://www.xfyun.cn/">讯飞语音云</a>SDK下载界面，下载对应离线SDK
 */
public class VideoDemo extends Activity {
	private final static String TAG = VideoDemo.class.getSimpleName();
	private ImageButton titleBack;
	private SurfaceView mPreviewSurface;
	private SurfaceView mFaceSurface;
	private Camera mCamera;
	private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
	// Camera nv21格式预览帧的尺寸，默认设置640*480//后期因为需要全屏顾设置成640*360
	private int PREVIEW_WIDTH = 640;
	private int PREVIEW_HEIGHT = 360;
	// 预览帧数据存储数组和缓存数组
	private byte[] nv21;
	private byte[] buffer;
	// 缩放矩阵
	private Matrix mScaleMatrix = new Matrix();
	// 加速度感应器，用于获取手机的朝向
	private Accelerometer mAcc;
	// FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
	private FaceDetector mFaceDetector;
	private boolean mStopTrack;
	private Toast mToast;
	private long mLastClickTime;
	private int isAlign = 1;//是否开启人脸聚焦
	private boolean hasTakePhoto = false;
	private String picpathandname = null;
	private String PicPath = null;
	private ImageView mScanHorizontalLineImageView;
	private ImageView mScanVerticalLineImageView;
	private View mPreviewView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_demo);
		mScanHorizontalLineImageView = (ImageView) findViewById(R.id.scanHorizontalLineImageView);
		mScanVerticalLineImageView = (ImageView) findViewById(R.id.scanVerticalLineImageView);
		mPreviewView = findViewById(R.id.previewView);
		//得到当前界面的装饰视图
		View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
		//设置系统UI元素的可见性
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

		initUI();
		PicPath = getIntent().getStringExtra("PicPath");
		nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
		buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
		mAcc = new Accelerometer(VideoDemo.this);
		mFaceDetector = FaceDetector.createDetector(VideoDemo.this, null);
	}


	private Callback mPreviewCallback = new Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			closeCamera();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			openCamera();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
								   int height) {
			//实现自动对焦
			mCamera.autoFocus(new Camera.AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if(success){
						initCamera();//实现相机的参数初始化
						camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
					}
				}

			});
			mScaleMatrix.setScale(width/(float)PREVIEW_HEIGHT, height/(float)PREVIEW_WIDTH);
		}
	};

	private void setSurfaceSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = metrics.widthPixels;
		int height = (int) (width * PREVIEW_WIDTH / (float)PREVIEW_HEIGHT);
		RelativeLayout.LayoutParams params = new LayoutParams(width, height);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		mPreviewSurface.setLayoutParams(params);
		mFaceSurface.setLayoutParams(params);
	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("deprecation")
	private void initUI() {
		mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
		mFaceSurface = (SurfaceView) findViewById(R.id.sfv_face);
		titleBack = (ImageButton)findViewById(R.id.title_back);
		mPreviewSurface.getHolder().addCallback(mPreviewCallback);
		mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mFaceSurface.setZOrderOnTop(true);
		mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		// 点击SurfaceView，切换摄相头
		mFaceSurface.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 只有一个摄相头，不支持切换
				if (Camera.getNumberOfCameras() == 1) {
					showTip("只有后置摄像头，不能切换");
					return;
				}
				closeCamera();
				if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
					mCameraId = CameraInfo.CAMERA_FACING_BACK;
				} else {
					mCameraId = CameraInfo.CAMERA_FACING_FRONT;
				}
				hasTakePhoto =false;
				openCamera();
			}
		});
		titleBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finishActivity(false);
			}
		});
		// 长按SurfaceView 500ms后松开，摄相头聚集
		mFaceSurface.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mLastClickTime = System.currentTimeMillis();
						break;
					case MotionEvent.ACTION_UP:
						if (System.currentTimeMillis() - mLastClickTime > 500) {
							mCamera.autoFocus(null);
							return true;
						}
						break;

					default:
						break;
				}
				return false;
			}
		});



		setSurfaceSize();
		mToast = Toast.makeText(VideoDemo.this, "", Toast.LENGTH_SHORT);
	}

	private void openCamera() {
		if (null != mCamera) {
			return;
		}

		if (!checkCameraPermission()) {
			showTip("摄像头权限未打开，请打开后再试");
			mStopTrack = true;
			return;
		}

		// 只有一个摄相头，打开后置
		if (Camera.getNumberOfCameras() == 1) {
			mCameraId = CameraInfo.CAMERA_FACING_BACK;
		}

		try {
			mCamera = Camera.open(mCameraId);
			if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
				showTip("前置摄像头已开启，点击可切换");
			} else {
				showTip("后置摄像头已开启，点击可切换");
			}
		} catch (Exception e) {
			e.printStackTrace();
			closeCamera();
			return;
		}

		initCamera();
		// 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
		mCamera.setDisplayOrientation(90);
		mCamera.setPreviewCallback(new PreviewCallback() {

			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				System.arraycopy(data, 0, nv21, 0, data.length);

			}
		});

		try {
			mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(mFaceDetector == null) {
			/**
			 * 离线视频流检测功能需要单独下载支持离线人脸的SDK
			 * 请开发者前往语音云官网下载对应SDK
			 */
			// 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
			showTip( "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化" );
		}
	}
	private Camera.Parameters params;
	//相机参数的初始化设置
	private void initCamera()
	{
		params = mCamera.getParameters();
		params.setPreviewFormat(ImageFormat.NV21);
		params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
		mCamera.setParameters(params);
		mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

	}
	private void closeCamera() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	private boolean checkCameraPermission() {
		int status = checkPermission(permission.CAMERA, Process.myPid(), Process.myUid());
		if (PackageManager.PERMISSION_GRANTED == status) {
			return true;
		}

		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mAcc) {
			mAcc.start();
		}

		mStopTrack = false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!mStopTrack) {
					if (null == nv21) {
						continue;
					}

					synchronized (nv21) {
						System.arraycopy(nv21, 0, buffer, 0, nv21.length);
					}

					// 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
					int direction = Accelerometer.getDirection();
					boolean frontCamera = (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId);
					// 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
					// 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
					if (frontCamera) {
						// SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
						direction = (4 - direction)%4;
					}

					if(mFaceDetector == null) {
						/**
						 * 离线视频流检测功能需要单独下载支持离线人脸的SDK
						 * 请开发者前往语音云官网下载对应SDK
						 */
						// 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
						showTip( "创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化" );
						break;
					}

					String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
					Log.d(TAG, "result:"+result);
					try{
						JSONObject jsonObject = new JSONObject(result);
						JSONArray jsonA1= jsonObject.getJSONArray("face");
						if(jsonA1.length() >=1){
							JSONObject position = jsonA1.getJSONObject(0).getJSONObject("position");
							//left是距离下边框的top是距离左边框的距离

							if( !hasTakePhoto && position.getInt("left") >20 && position.getInt("top")>20){
								picpathandname = savPicRegPic(mCamera.getParameters().getPreviewSize(),nv21,PicPath);
								hasTakePhoto = true;
								handler.sendEmptyMessage(10001);//取图片成功，关闭acticity.
							}
						}


					}catch (Exception w){
						w.printStackTrace();
					}

					FaceRect[] faces = ParseResult.parseResult(result);

					Canvas canvas = mFaceSurface.getHolder().lockCanvas();
					if (null == canvas) {
						continue;
					}

					canvas.drawColor(0, PorterDuff.Mode.CLEAR);
					canvas.setMatrix(mScaleMatrix);

					if( faces == null || faces.length <=0 ) {
						mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
						continue;
					}

					if (null != faces && frontCamera == (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
						for (FaceRect face: faces) {
							face.bound = FaceUtil.RotateDeg90(face.bound, PREVIEW_WIDTH, PREVIEW_HEIGHT);
							if (face.point != null) {
								for (int i = 0; i < face.point.length; i++) {
									face.point[i] = FaceUtil.RotateDeg90(face.point[i], PREVIEW_WIDTH, PREVIEW_HEIGHT);
								}
							}
							FaceUtil.drawFaceRect(canvas, face, PREVIEW_WIDTH, PREVIEW_HEIGHT,
									frontCamera, false);
						}
					} else {
						Log.d(TAG, "faces:0");
					}

					mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
				}
			}
		}).start();
	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 10001){
				finishActivity(true);
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onPause() {
		super.onPause();
		closeCamera();
		if (null != mAcc) {
			mAcc.stop();
		}
		mStopTrack = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if( null != mFaceDetector ){
			// 销毁对象
			mFaceDetector.destroy();
		}
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		int[] location = new int[2];

		// getLocationInWindow方法要在onWindowFocusChanged方法里面调用
		// 个人理解是onCreate时，View尚未被绘制，因此无法获得具体的坐标点
		mPreviewView.getLocationInWindow(location);

		// 模拟的mPreviewView的左右上下坐标坐标
		int left = mPreviewView.getLeft();
		int right = mPreviewView.getRight();
		int top = mPreviewView.getTop();
		int bottom = mPreviewView.getBottom();

		// 从上到下的平移动画
		Animation verticalAnimation = new TranslateAnimation(left, left, top, bottom);
		verticalAnimation.setDuration(6000); // 动画持续时间
		verticalAnimation.setRepeatCount(Animation.INFINITE); // 无限循环

		// 播放动画
		mScanHorizontalLineImageView.setAnimation(verticalAnimation);

		// 从左到右的平移动画
		Animation horizontalAnimation = new TranslateAnimation(left, left, bottom, top);
		horizontalAnimation.setDuration(6000); // 动画持续时间
		horizontalAnimation.setRepeatCount(Animation.INFINITE); // 无限循环

		//播放动画
		mScanVerticalLineImageView.setAnimation(horizontalAnimation);
		horizontalAnimation.startNow();
		verticalAnimation.startNow();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finishActivity(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void finishActivity(boolean ok){
		closeCamera();
		Intent intent = new Intent();
		if(ok){
			intent.putExtra("picpathandname", picpathandname);

			setResult(RESULT_OK, intent);
		}else {
			setResult(10, intent);
		}
		this.finish();
	}
}
