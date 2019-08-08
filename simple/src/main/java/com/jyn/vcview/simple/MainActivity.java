package com.jyn.vcview.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jyn.vcview.VerificationCodeView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private VerificationCodeView verificationcodeview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificationcodeview = findViewById(R.id.verificationcodeview);
        textView = findViewById(R.id.text);
        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {
                textView.setText("输入中：" + content);
            }

            @Override
            public void onComplete(View view, String content) {
                textView.setText("输入结束：" + content);
            }
        });
    }

    public void clear(View view) {
        verificationcodeview.setEmpty();
    }
}
