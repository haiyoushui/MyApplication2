package com.isss.liuh.myapplication.UI;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.dk.view.patheffect.PathTextView;
import com.eagle.androidlib.baseUI.BaseActivity;
import com.eagle.androidlib.utils.Logger;
import com.eagle.androidlib.utils.SystemUtil;
import com.eagle.androidlib.utils.ToastManager;
import com.isss.liuh.myapplication.FaceRApplacation;
import com.isss.liuh.myapplication.NET.HeadUtil;
import com.isss.liuh.myapplication.R;
import com.isss.liuh.myapplication.Share.SystemShare;
import com.isss.liuh.myapplication.UTILS.JsonUtil;
import com.isss.liuh.myapplication.VO.FacePepleInfo;
import com.isss.liuh.myapplication.VideoDemo;
import com.isss.liuh.myapplication.util.FaceUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FaceIDCardActivity extends BaseActivity {
    private final int IDCARD = 10001;
    private final int ADDFACE = 10002;
    private final int IDCARDSEARCH = 10003;
    private final String TAG = "FaceIDCardActivity";
    @BindView(R.id.title_back)
    ImageButton titleBack;
    @BindView(R.id.stationView)
    TextView stationView;
    @BindView(R.id.login_user_input)
    TextView loginUserInput;
    @BindView(R.id.iccard_name)
    EditText iccardName;
    @BindView(R.id.login_password_input)
    TextView loginPasswordInput;
    @BindView(R.id.idcard_id)
    EditText idcardId;
    @BindView(R.id.signin_button)
    Button signinButton;
    @BindView(R.id.idcard_faceimage)
    ImageView idcardFaceimage;
    @BindView(R.id.idcard_address)
    EditText idcardAddress;
    @BindView(R.id.faceidentify_male)
    RadioButton faceidentifyMale;
    @BindView(R.id.faceidentify_female)
    RadioButton faceidentifyFemale;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;


    private String fileSrc;
    private Bitmap mImage = null;
    private FacePepleInfo facePepleInfo = new FacePepleInfo();
    private String filePath = SystemUtil.ADDFACEPATH;
    private Activity getContext() {
        return FaceIDCardActivity.this;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_face_idcard;
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
        ButterKnife.bind(this);
        stationView.setText(getResources().getString(R.string.title_faceidcard));
        initAccessTokenWithAkSk();
        radioButtonCheak();
    }

    @OnClick({R.id.title_back, R.id.idcard_faceimage, R.id.signin_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finishActivity();
                break;
            case R.id.idcard_faceimage:
                showDdialo();
                break;
            case R.id.signin_button:
                if("".equals(idcardId.getText().toString()) || "".equals(iccardName.getText().toString())){
                    ToastManager.getInstance(getContext()).show("填写完整信息！");
                }else if(idcardId.getText().toString() == facePepleInfo.getUid() || idcardId.getText().toString().equals(facePepleInfo.getUid()) ){
                    facePepleInfo.setUname(iccardName.getText().toString());
                    facePepleInfo.setAddress(idcardAddress.getText().toString());
                    addFaceHttp(fileSrc, false);
                }else {
                    ToastManager.getInstance(getContext()).show("正在重新查询信息！");
                    //身份证号查询信息
                    IDCardIdSearch();
                }

                break;
        }
    }

    public void radioButtonCheak() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.faceidentify_female:
                        facePepleInfo.setGender("女");
                        break;
                    case R.id.faceidentify_male:
                        facePepleInfo.setGender("男");
                        break;

                }
            }
        });


    }

    public void showDdialo() {
        final Dialog dialogPic = new Dialog(getContext(), R.style.dialog);
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
                intent.setClass(getContext(), VideoDemo.class);
                intent.putExtra("PicPath", filePath);
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
                if (SystemShare.getSYSISCUTPIC(getContext())) {
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
                            int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getContext(), new String[]{Manifest.permission.CALL_PHONE}, 100);
                                return;
                            }
                        }
                        FaceUtil.saveBitmapToFile(getContext(), mImage);
                    }
                    // 获取图片保存路径
                    fileSrc = FaceUtil.getImagePath(getContext());
                    setImageToView(fileSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

        facePepleInfo.setUname(iccardName.getText().toString());
        facePepleInfo.setUinfo("身份证扫描入录");
        facePepleInfo.setAddress(idcardAddress.getText().toString());
        String userInfo = JsonUtil.faceIngo2Json(facePepleInfo).toString();
        RequestParams params = HeadUtil.addFace(fileSrc, idcardId.getText().toString(), userInfo, isReplace);
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
                msg.what = ADDFACE;
                handler.sendMessage(msg);

            }
        });

    }

    private void initAccessTokenWithAkSk() {
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(SystemShare.getBAIDUTOKEN(getContext()));
        accessToken.setExpiresIn(SystemShare.getBAIDUEXPIRESIN(getContext()));
//        accessToken.setLic();
        OCR.getInstance().initWithToken(getApplicationContext(), accessToken);
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(100);
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null && result.getDirection() > 0) {
                    Logger.i(TAG,"身份证号查询"+result.toString());
                    if(result.getIdNumber() != null)facePepleInfo.setIDCardId(result.getIdNumber().toString());
                    if(result.getIdNumber() != null)facePepleInfo.setUid(result.getIdNumber().toString());
                    if(result.getEthnic() != null)facePepleInfo.setEthnic(result.getEthnic().toString());
                    if(result.getGender() != null)facePepleInfo.setGender(result.getGender().toString());
                    if(result.getAddress() != null)facePepleInfo.setAddress(result.getAddress().toString());
                    if(result.getName() != null)facePepleInfo.setUname(result.getName().toString());
                    handler.sendEmptyMessage(IDCARD);

                }
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Logger.e(TAG, ""+error.toString());
                ToastManager.getInstance(getContext()).show("自动识别失败！");
            }
        });
    }
    private void IDCardIdSearch() {
        RequestParams params = HeadUtil.IDCardIdSearch(idcardId.getText().toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }
            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String result) {
                Logger.i(TAG, "身份证号查询结果" + result);
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    facePepleInfo.setIDCardAddress(jsonObject.getString("address"));
                    facePepleInfo.setBirthday(jsonObject.getString("birthday"));
                    facePepleInfo.setGender(jsonObject.getString("gender"));
                    facePepleInfo.setIDCardId(jsonObject.getString("IDCardId"));
                    facePepleInfo.setUid(jsonObject.getString("IDCardId"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(IDCARDSEARCH);

            }
        });

    }
    /**
     * 向后台发送人脸信息
     */
    private void sendInfoToService() {
        RequestParams params = HeadUtil.sendPepleInfoToService(facePepleInfo);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }
            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }
            @Override
            public void onFinished() {
            }
            @Override
            public void onSuccess(String result) {
                fileSrc = SystemUtil.moveFileToAddFaceSucees(fileSrc,facePepleInfo.getUid());
                clearAll();
            }
        });
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ADDFACE) {
                ToastManager.getInstance(getContext()).show("操作成功！");
                sendInfoToService();
            } else if (msg.what == IDCARD || msg.what == IDCARDSEARCH) {
                //身份证扫描结果显示
                idcardId.setText(facePepleInfo.getIDCardId());
                iccardName.setText(facePepleInfo.getUname());
                if(facePepleInfo.getIDCardAddress() == null||"".equals(facePepleInfo.getIDCardAddress())){
                    idcardAddress.setText(facePepleInfo.getAddress());
                }else{
                    idcardAddress.setText(facePepleInfo.getIDCardAddress());
                }

                if(facePepleInfo.getGender() == "男" || "男".equals(facePepleInfo.getGender())){
                    faceidentifyMale.setChecked(true);
                }else if (facePepleInfo.getGender() == "女" || "女".equals(facePepleInfo.getGender())){
                    faceidentifyFemale.setChecked(true);
                }
            }else if(msg.what == IDCARDSEARCH) {
                ToastManager.getInstance(getContext()).show("查询成功！");
            }
            super.handleMessage(msg);
        }
    };


    private void setImageToView(String fileSrc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 1;
        idcardFaceimage.setImageBitmap(BitmapFactory.decodeFile(fileSrc, options));
        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, fileSrc);
    }
    /**
     * 判断输入是否为空或是否符合规定
     * @return
     */
    private boolean isInLow() {
        if (idcardId.getText() == null || "".equals(idcardId.getText().toString())) {
            ToastManager.getInstance(FaceRApplacation.getContext()).show("请输入人物身份证号！");
            return false;
        } else if (iccardName.getText().toString() == null || "".equals(iccardName.getText().toString())) {
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
    private void clearAll(){
        iccardName.setText("");
        idcardId.setText("");
        idcardAddress.setText("");
        fileSrc = null;
        idcardFaceimage.setImageResource(R.drawable.no_photo2);
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
