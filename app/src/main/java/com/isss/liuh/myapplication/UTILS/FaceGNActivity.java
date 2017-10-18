package com.isss.liuh.myapplication.UTILS;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dk.view.patheffect.PathTextView;
import com.isss.liuh.myapplication.R;

import static android.R.attr.duration;
import static android.R.attr.radius;
import static android.R.attr.shadowColor;

public class FaceGNActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_gn);
        setText();
    }
    private void setText() {
        PathTextView mPathTextView = (PathTextView) findViewById(R.id.path);
        mPathTextView.setPaintType(PathTextView.Type.MULTIPLY);
        mPathTextView.setTextColor(R.color.white);
        mPathTextView.setTextSize(18);
        mPathTextView.setTextWeight(3);
        mPathTextView.setDuration(duration);
        mPathTextView.setShadow(radius, 4, 4, shadowColor);
        mPathTextView.init("Hello World");
        mPathTextView.showContextMenu();
        System.out.println(mPathTextView.isShown()+"######################");
    }
}
