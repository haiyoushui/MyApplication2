package com.isss.liuh.myapplication.UI;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.isss.liuh.myapplication.VideoDemo;
import com.isss.liuh.myapplication.util.FaceUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FaceAddActivity extends BaseActivity implements View.OnLongClickListener{
    private final String TAG = "FaceAddActivity";
    private final int ADDSUCCESSFUL = 10001;
    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.stationView)
    TextView stationView;
    @BindView(R.id.edit_faceadd_uid)
    EditText editFaceaddUid;
    @BindView(R.id.edit_faceadd_name)
    EditText editFaceaddName;
    @BindView(R.id.edit_facedadd_uinfo)
    EditText editFacedaddUinfo;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.face_add)
    Button faceAdd;
    @BindView(R.id.face_replace)
    Button faceReplace;
    @BindView(R.id.faceidentify_male)
    RadioButton faceidentifyMale;
    @BindView(R.id.faceidentify_female)
    RadioButton faceidentifyFemale;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.faceadd_faceimage0)
    ImageView faceaddFaceimage0;
    @BindView(R.id.faceadd_faceimage1)
    ImageView faceaddFaceimage1;
    @BindView(R.id.faceadd_faceimage2)
    ImageView faceaddFaceimage2;

    private Bitmap mImage = null;
    private String fileSrc = null;
    private String sex = "男";
    private int ImageFlag = 0;
    private HashMap<Integer,String> filePathMap = new HashMap<>();
    @Override
    public int getLayoutID() {
        return R.layout.activity_face_add;
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
        radioButtonCheak();
        stationView.setText("人脸添加");
        faceaddFaceimage0.setOnLongClickListener(this);
        faceaddFaceimage1.setOnLongClickListener(this);
        faceaddFaceimage2.setOnLongClickListener(this);
    }

    public void radioButtonCheak() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.faceidentify_female:
                        sex = "女";
                        break;
                    case R.id.faceidentify_male:
                        sex = "男";
                        break;

                }

            }
        });


    }


    /**
     * 弹出图片选择框
     */
    public void showDdialo() {
        final Dialog dialogPic = new Dialog(FaceAddActivity.this, R.style.dialog);
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
                intent.setClass(FaceAddActivity.this, VideoDemo.class);
                intent.putExtra("PicPath", SystemUtil.ADDFACEPATH);
                startActivityForResult(intent, UIPubInfo.INTENT_FACEADD_ACTIVITY);
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
            case UIPubInfo.INTENT_FACEADD_ACTIVITY:
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
                if (SystemShare.getSYSISCUTPIC(FaceAddActivity.this)) {
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
                            int permissionCheck = ContextCompat.checkSelfPermission(FaceAddActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(FaceAddActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                                return;
                            }
                        }
                        FaceUtil.saveBitmapToFile(FaceAddActivity.this, mImage);
                    }
                    // 获取图片保存路径
                    fileSrc = FaceUtil.getImagePath(FaceAddActivity.this);
                    setImageToView(fileSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageToView(String fileSrc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 1;
        mImage = PicUtil.getRoundedCornerBitmap(BitmapFactory.decodeFile(fileSrc, options), 300, 400);
        if(ImageFlag == 0){
            faceaddFaceimage0.setImageBitmap(mImage);
            filePathMap.put(0,fileSrc);
        }else if(ImageFlag == 1){
            faceaddFaceimage1.setImageBitmap(mImage);
            filePathMap.put(1,fileSrc);
        }else{
            faceaddFaceimage2.setImageBitmap(mImage);
            filePathMap.put(2,fileSrc);
        }
        ToastManager.getInstance(FaceAddActivity.this).show("长按删除此图！");
    }

    /**
     * 向百度人脸库添加人脸
     *
     * @param fileSrc
     */
    private void addFaceHttp(String fileSrc, boolean isReplace) {
        if (!isInLow()) {
            return;
        }
        String userInfo = JsonUtil.faceIngo2Json(editFaceaddName.getText().toString(), editFacedaddUinfo.getText().toString(), sex).toString();
        RequestParams params = HeadUtil.addFace(filePathMap, editFaceaddUid.getText().toString(), userInfo, isReplace);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                ToastManager.getInstance(FaceRApplacation.getContext()).show("人脸注册失败！");

            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                Logger.i(TAG, "添加人脸返回结果" + result);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                msg.setData(bundle);
                msg.what = ADDSUCCESSFUL;
                handler.sendMessage(msg);

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ADDSUCCESSFUL) {
                ToastManager.getInstance(FaceAddActivity.this).show("操作成功！");
            }
            super.handleMessage(msg);
        }
    };

    private boolean isInLow() {
        if (editFaceaddUid.getText() == null || "".equals(editFaceaddUid.getText().toString())) {
            ToastManager.getInstance(FaceRApplacation.getContext()).show("请输入人物身份证号！");
            return false;
        } else if (editFaceaddName.getText().toString() == null || "".equals(editFaceaddName.getText().toString())) {
            ToastManager.getInstance(FaceRApplacation.getContext()).show("请输入人物姓名！");
            return false;
        } else if (fileSrc == null) {
            ToastManager.getInstance(FaceRApplacation.getContext()).show("请选择人物图片！");
            showDdialo();
            return false;
        } else {
            return true;
        }

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


    @OnClick({R.id.title_back, R.id.faceadd_faceimage0, R.id.faceadd_faceimage1, R.id.faceadd_faceimage2, R.id.face_add, R.id.face_replace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finishActivity();
                break;
            case R.id.faceadd_faceimage0:
                ImageFlag = 0;
                showDdialo();
                break;
            case R.id.faceadd_faceimage1:
                ImageFlag = 1;
                showDdialo();
                break;
            case R.id.faceadd_faceimage2:
                ImageFlag = 2;
                showDdialo();
                break;
            case R.id.face_add:
                addFaceHttp(fileSrc, false);
                break;
            case R.id.face_replace:
                addFaceHttp(fileSrc, true);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.faceadd_faceimage0:
                showDialogDeletPicture(0);
                break;
            case R.id.faceadd_faceimage1:
                showDialogDeletPicture(1);
                break;
            case R.id.faceadd_faceimage2:
                showDialogDeletPicture(2);
                break;
        }
            return false;
        }
    /**
     * 长按弹出删除dialog
     */

    public void showDialogDeletPicture(int mImageFlag) {
        ImageFlag = mImageFlag;
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
        builder.setTitle("提示"); // 设置标题
        builder.setMessage("是否删除此图？"); // 设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 关闭dialog
                deletPict();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void  deletPict(){
        if(ImageFlag == 0){
            faceaddFaceimage0.setImageResource(R.drawable.no_photo2);
            filePathMap.remove(0);
        }else if(ImageFlag == 1){
            faceaddFaceimage1.setImageResource(R.drawable.no_photo2);
            filePathMap.remove(1);
        }else {
            faceaddFaceimage2.setImageResource(R.drawable.no_photo2);
            filePathMap.remove(2);
        }
    }
}
