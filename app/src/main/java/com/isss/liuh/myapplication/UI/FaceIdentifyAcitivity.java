package com.isss.liuh.myapplication.UI;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.eagle.androidlib.baseUI.BaseActivity;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.eagle.androidlib.utils.ToastManager;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.NET.HeadUtil;
import com.isss.liuh.myapplication.R;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.UTILS.JsonUtil;
import com.isss.liuh.myapplication.UTILS.PicUtil;
import com.isss.liuh.myapplication.VO.FacePepleInfo;
import com.isss.liuh.myapplication.VideoDemo;
import com.isss.liuh.myapplication.util.FaceUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaceIdentifyAcitivity extends BaseActivity {
    private final String TAG = "FaceIdentifyAcitivity";
    private final int FACEIDENTIFY = 10001;
    @BindView(R.id.face_image)
    ImageView faceImage;

    private Bitmap mImage = null;
    private String fileSrc = null;

    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.stationView)
    TextView stationView;

    @BindView(R.id.faceidentify_name)
    TextView faceidentifyName;
    @BindView(R.id.faceidentify_id)
    TextView faceidentifyId;
    @BindView(R.id.faceidentify_info)
    TextView faceidentifyInfo;
    @BindView(R.id.faceidentify_sex)
    TextView faceidentifySex;
    @BindView(R.id.faceidentify_remark)
    TextView faceidentifyRemark;

    @Override
    public int getLayoutID() {
        return R.layout.activity_face_identify_acitivity;
    }

    @Override
    public void initBundle() {

    }

    @Override
    public void initDataCreate() {

    }

    @Override
    public void initDataResume() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {


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

    }

    @OnClick({R.id.title_back, R.id.faceidentify_remark, R.id.face_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finishActivity();
                break;
            case R.id.faceidentify_remark:
                break;
            case R.id.face_image:
                showDdialo();
                break;

        }
    }

    public void showDdialo() {
        final Dialog dialogPic = new Dialog(FaceIdentifyAcitivity.this, R.style.dialog);
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
                intent.setClass(FaceIdentifyAcitivity.this, VideoDemo.class);
                intent.putExtra("PicPath", SystemUtil.FACEIDENTIFYPATH);
                startActivityForResult(intent, UIPubInfo.INTENT_FACIDENTIFY_ACTIVITY);
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
        switch (requestCode) {
            case UIPubInfo.INTENT_FACIDENTIFY_ACTIVITY:
                fileSrc = data.getExtras().getString("picpathandname");
                setImageToView(fileSrc);
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
                if (SystemShare.getSYSISCUTPIC(FaceIdentifyAcitivity.this)) {
                    FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));  // 跳转到图片裁剪页面
                } else {
                    setImageToView(fileSrc);//如果不剪切直接检测的话
                }
                break;
            case FaceUtil.REQUEST_CROP_IMAGE:
                // 获取返回数据
                try {
                    mImage = data.getParcelableExtra("data");
                    // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
                    if (null != mImage) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            int permissionCheck = ContextCompat.checkSelfPermission(FaceIdentifyAcitivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(FaceIdentifyAcitivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                                return;
                            }
                        }
                        FaceUtil.saveBitmapToFile(FaceIdentifyAcitivity.this, mImage);
                    }
                    // 获取图片保存路径
                    fileSrc = FaceUtil.getImagePath(FaceIdentifyAcitivity.this);
                    setImageToView(fileSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 向百度人脸库识别
     *
     * @param fileSrc
     */
    private void FaceIdentifyHttp(String fileSrc) {

        RequestParams params = HeadUtil.identifyFace(fileSrc, "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                ToastManager.getInstance(FaceRApplacation.getContext()).show("人脸识别失败！");

            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                Logger.i(TAG, "人脸识别结果" + result);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                msg.setData(bundle);
                msg.what = FACEIDENTIFY;
                handler.sendMessage(msg);

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FACEIDENTIFY) {
                FacePepleInfo facePepleInfo = JsonUtil.faceInfo2FacePeple(msg.getData().getString("result"));
                if(facePepleInfo.getScores()<70){
                    faceidentifyRemark.setText("未查到匹配对象！");
                    faceidentifyRemark.setTextColor(getResources().getColor(R.color.faceiden_1));
                    return;
                }
                faceidentifyName.setText(facePepleInfo.getUname());
                faceidentifySex.setText(facePepleInfo.getSex());
                faceidentifyId.setText(facePepleInfo.getUid());
                faceidentifyInfo.setText(facePepleInfo.getUinfo());
                faceidentifyRemark.setTextColor(getResources().getColor(R.color.white));
            }
            super.handleMessage(msg);
        }
    };


    private void setImageToView(String fileSrc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 1;
        mImage = PicUtil.getOvalBitmap(BitmapFactory.decodeFile(fileSrc, options));
        Drawable drawable = new BitmapDrawable(mImage);
        faceImage.setBackground(drawable);
        FaceIdentifyHttp(fileSrc);
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
