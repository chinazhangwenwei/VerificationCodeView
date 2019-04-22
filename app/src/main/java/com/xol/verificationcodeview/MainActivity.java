package com.xol.verificationcodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xol.codelibrary.VerificationView;

public class MainActivity extends AppCompatActivity {

    private VerificationView verificationView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificationView = findViewById(R.id.code_view);
        verificationView.setCodeSuccess(new VerificationView.CodeSuccess() {
            @Override
            public void codeSuccess(String codes) {
                Log.d(TAG, "codeSuccess: " + codes);
            }
        });
    }
}
