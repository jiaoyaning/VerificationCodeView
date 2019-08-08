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
            public void onComplete(String content) {
                textView.setText(content);
            }
        });
    }

    public void clear(View view) {
        verificationcodeview.setEmpty();
    }
}
