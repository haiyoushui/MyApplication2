package com.isss.liuh.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eagle.androidlib.baseUI.BaseActivity;
import com.isss.liuh.myapplication.R;
import com.isss.liuh.myapplication.UTILS.FaceGNActivity;
import com.isss.liuh.myapplication.Widget.CircleMenuLayout;
import com.isss.liuh.myapplication.XunfeiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "注册 ", "识别", "检测",
            "身份", "附加1" };
    private int[] mItemImgs = new int[] { R.drawable.main_faceadd,
            R.drawable.mai_faceidenti, R.drawable.main_facedetect,
            R.drawable.main_faceshenfen, R.drawable.main_faceremark};

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
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
        circleChose();
    }

    private void circleChose(){
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener()
        {      Intent intent = new Intent();

            @Override
            public void itemClick(View view, int pos)
            {
                if(pos == 0){
                    intent.setClass(MainActivity.this,FaceAddActivity.class);
                    startActivity(intent);
                }else if(pos == 1){
                    intent.setClass(MainActivity.this,FaceIdentifyAcitivity.class);
                    startActivity(intent);
                }else if(pos == 2){
                    intent.setClass(MainActivity.this,FaceDetectActivity.class);
                    startActivity(intent);
                }else if(pos == 3){
                    intent.setClass(MainActivity.this,IDCardActivity.class);
                    startActivity(intent);
                }else if(pos == 4){
            intent.setClass(MainActivity.this,FaceGNActivity.class);
            startActivity(intent);
        }
            }

            @Override
            public void itemCenterClick(View view)
            {
                Toast.makeText(MainActivity.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
