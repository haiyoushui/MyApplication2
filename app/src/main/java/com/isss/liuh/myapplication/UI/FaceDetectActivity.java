package com.isss.liuh.myapplication.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eagle.androidlib.baseUI.BaseActivity;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.eagle.androidlib.utils.ToastManager;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.R;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.UTILS.JsonUtil;
import com.isss.liuh.myapplication.UTILS.PicUtil;
import com.isss.liuh.myapplication.UTILS.PubInfo;
import com.isss.liuh.myapplication.UTILS.utils;
import com.isss.liuh.myapplication.VO.FaceInfo;
import com.isss.liuh.myapplication.VideoDemo;
import com.isss.liuh.myapplication.util.FaceUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FaceDetectActivity extends BaseActivity {
    private final String TAG = "FaceDetectActivity";
    private final int UPDATEFACEINFO = 10001;


    @BindView(R.id.stationView)
    TextView stationView;
    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.faceinfo_age)
    TextView faceinfoAge;
    @BindView(R.id.faceinfo_sex)
    TextView faceinfoSex;
    @BindView(R.id.faceinfo_race)
    TextView faceinfoRace;
    @BindView(R.id.faceinfo_expression)
    TextView faceinfoExpression;
    @BindView(R.id.faceinfo_glasses)
    TextView faceinfoGlasses;
    @BindView(R.id.faceinfo_beauty)
    TextView faceinfoBeauty;
    @BindView(R.id.faceinfo_facetype)
    TextView faceinfoFacetype;

    @BindView(R.id.facedetect_img)
    ImageView facedetectImg;
    @BindView(R.id.linear_faceinfo)
    RelativeLayout linearFaceinfo;
    @BindView(R.id.linearLayout10)
    LinearLayout linearLayout10;
    @BindView(R.id.linearLayout9)
    LinearLayout linearLayout9;
    @BindView(R.id.linearLayout12)
    LinearLayout linearLayout12;
    @BindView(R.id.linearLayout5)
    LinearLayout linearLayout5;
    @BindView(R.id.linearLayout7)
    LinearLayout linearLayout7;
    @BindView(R.id.linearLayout6)
    LinearLayout linearLayout6;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    private Bitmap mImage = null;

    @Override
    public int getLayoutID() {
        return R.layout.activity_face_detect;
    }

    @Override
    public void initBundle() {
    }

    @Override
    public void initDataCreate() {
    }

    @Override
    public void initDataResume() {
        linearFaceinfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        TextView t = (TextView) findViewById(R.id.stationView);
        t.setText("人脸检测");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTextNull();
        linearLayout4.setBackgroundResource(R.drawable.facedetect_b);
        linearLayout5.setBackgroundResource(R.drawable.facedetect_c);
        linearLayout6.setBackgroundResource(R.drawable.facedetect_d);
        linearLayout7.setBackgroundResource(R.drawable.facedetect_b);
        linearLayout9.setBackgroundResource(R.drawable.facedetect_e);
        linearLayout10.setBackgroundResource(R.drawable.facedetect_e);
        linearLayout12.setBackgroundResource(R.drawable.facedetect_c);
    }

    @OnClick({R.id.title_back, R.id.facedetect_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finishActivity();
                break;
            case R.id.facedetect_img:
                showDdialo();
                break;
        }
    }

    /**
     * 弹出图片选择框
     */
    public void showDdialo() {
        final Dialog dialogPic = new Dialog(FaceDetectActivity.this, R.style.dialog);
// 性别选择的dialog，以及其上的控件
        View viewPic = getLayoutInflater().inflate(R.layout.dialog_chosepic, null);
// 设置dialog没有title
        dialogPic.setContentView(viewPic, new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT));
        Window window = dialogPic.getWindow();
// 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
// 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout picCamera = (LinearLayout) viewPic.findViewById(R.id.pic_camera);
        LinearLayout picPick = (LinearLayout) viewPic.findViewById(R.id.pic_pick);
        picCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPic.dismiss();
                Intent intent = new Intent();
                intent.setClass(FaceDetectActivity.this, VideoDemo.class);
                intent.putExtra("PicPath", SystemUtil.FACEDETECT);
                startActivityForResult(intent, UIPubInfo.INTENT_FACEDETECT_ACTIVITY);
            }
        });
        picPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPic.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, FaceUtil.REQUEST_PICTURE_CHOOSE);
            }
        });
// 设置显示位置
        dialogPic.onWindowAttributesChanged(wl);
        dialogPic.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String fileSrc = null;
        switch (requestCode) {
            case UIPubInfo.INTENT_FACEDETECT_ACTIVITY:
                fileSrc = data.getExtras().getString("picpathandname");
                setImageAndDetect(fileSrc);
                break;
            case FaceUtil.REQUEST_PICTURE_CHOOSE:

                if ("file".equals(data.getData().getScheme())) {
                    // 有些低版本机型返回的Uri模式为file
                    fileSrc = data.getData().getPath();
                } else {
                    // Uri模型为content
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(data.getData(), proj,
                            null, null, null);
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileSrc = cursor.getString(idx);
                    cursor.close();
                }
                if (SystemShare.getSYSISCUTPIC(FaceDetectActivity.this)) {
                    FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));  // 跳转到图片裁剪页面
                } else {
                    setImageAndDetect(fileSrc);//如果不剪切直接检测的话
                }

                break;
            case FaceUtil.REQUEST_CROP_IMAGE:
                // 获取返回数据
                try {
                    mImage = data.getParcelableExtra("data");
                    // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
                    if (null != mImage) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            int permissionCheck = ContextCompat.checkSelfPermission(FaceDetectActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(FaceDetectActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                                return;
                            }
                        }

                        FaceUtil.saveBitmapToFile(FaceDetectActivity.this, mImage);
                    }
                    // 获取图片保存路径
                    fileSrc = FaceUtil.getImagePath(FaceDetectActivity.this);
                    setImageAndDetect(fileSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通过百度API获取人脸信息
     *
     * @param fileSrc
     */
    private void setImageAndDetect(String fileSrc) {
        Logger.i(TAG, "百度人脸检测PicPath:" + fileSrc);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 1;
        mImage = BitmapFactory.decodeFile(fileSrc, options);
        setTextNull();
        facedetectImg.setImageBitmap(PicUtil.getOvalBitmap(mImage));
        RequestParams params = new RequestParams(PubInfo.URL_Baidu_Detect + "?access_token=" + SystemShare.getBAIDUTOKEN(FaceDetectActivity.this));
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("image", utils.fileToBase64(fileSrc));
        params.addBodyParameter("face_fields", "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities");
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                ToastManager.getInstance(FaceRApplacation.getContext()).show("人脸检测失败！");
                Logger.e(TAG, "人脸检测失败！");
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                Logger.i(TAG, "FaceDetectResult = " + result);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                msg.setData(bundle);
                msg.what = UPDATEFACEINFO;
                handler.sendMessage(msg);

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATEFACEINFO) {
                setFaceInfo(msg.getData().getString("result"));
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 将获取的百度检测人脸信息显示
     *
     * @param faceInfo
     */
    private void setFaceInfo(String faceInfo) {
        FaceInfo faceInfoO = JsonUtil.jsonToFaceInfo(faceInfo);
        Logger.d(TAG, "人脸信息" + faceInfoO.toString());
        if (faceInfoO.getResultNum() >= 1) {
            faceinfoAge.setText((int) faceInfoO.getAge() + "");
            faceinfoBeauty.setText((int) faceInfoO.getBeauty() + "");
            faceinfoSex.setText(faceInfoO.getFaceGender() + "");
            faceinfoRace.setText(faceInfoO.getFaceRace() + "");
            faceinfoExpression.setText(faceInfoO.getFaceExpression() + "");
            faceinfoGlasses.setText(faceInfoO.getFaceGlasses() + "");
            faceinfoFacetype.setText(faceInfoO.getFaceType() + "");
            linearFaceinfo.setVisibility(View.VISIBLE);
        }

    }
private void setTextNull(){
    faceinfoAge.setText( "");
    faceinfoBeauty.setText( "");
    faceinfoSex.setText( "");
    faceinfoRace.setText( "");
    faceinfoExpression.setText("");
    faceinfoGlasses.setText( "");
    faceinfoFacetype.setText("");
}
    /**
     * 关闭Activity
     */
    private void finishActivity() {
        recyleBitmap();
        this.finish();
    }

    private void recyleBitmap() {
        if (mImage != null && !mImage.isRecycled()) {
            // 回收并且置为null
            mImage.recycle();
            mImage = null;
        }
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
