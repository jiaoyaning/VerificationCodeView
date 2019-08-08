package com.jyn.vcview.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jyn.vcview.VerificationCodeView;

public class MainActivity extends AppCompatActivity implements VerificationCodeView.OnCodeFinishListener {

    private TextView textView, textView2;
    private VerificationCodeView verificationcodeview, verificationcodeview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificationcodeview = findViewById(R.id.verificationcodeview);
        verificationcodeview2 = findViewById(R.id.verificationcodeview2);

        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.text2);
        verificationcodeview.setOnCodeFinishListener(this);
        verificationcodeview2.setOnCodeFinishListener(this);
    }

    public void clear(View view) {
        verificationcodeview.setEmpty();
        verificationcodeview2.setEmpty();
    }

    @Override
    public void onTextChange(View view, String content) {
        if (view == verificationcodeview) {
            textView.setText("输入中：" + content);
        } else if (view == verificationcodeview2) {
            textView2.setText("输入中：" + content);
        }
    }

    @Override
    public void onComplete(View view, String content) {
        if (view == verificationcodeview) {
            textView.setText("输入结束：" + content);
        } else if (view == verificationcodeview2) {
            textView2.setText("输入结束：" + content);
        }
    }
}
