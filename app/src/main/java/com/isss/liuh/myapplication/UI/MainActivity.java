package com.isss.liuh.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eagle.androidlib.baseUI.BaseActivity;
import com.isss.liuh.myapplication.R;
import com.isss.liuh.myapplication.Widget.CircleMenuLayout;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private CircleMenuLayout mCircleMenuLayout;


    private int[] mItemImgs = new int[] { R.drawable.main_faceadd,
            R.drawable.mai_faceidenti, R.drawable.main_facedetect,
            R.drawable.main_faceshenfen, R.drawable.main_faceremark};
    private String[] mItemTexts = new String[] { };
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
        mItemTexts = new String[] { this.getString(R.string.title_facedadd), this.getString(R.string.main_titletext), this.getString(R.string.title_facedetect),
                this.getString(R.string.title_faceidcheack), this.getString(R.string.title_faceidcard) };
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
                    intent.setClass(MainActivity.this,IDCheakActivity.class);
                    startActivity(intent);
                }else if(pos == 4){
            intent.setClass(MainActivity.this,FaceIDCardActivity.class);
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
