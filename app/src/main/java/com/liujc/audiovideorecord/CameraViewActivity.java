package com.liujc.audiovideorecord;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.liujc.cameraview.CheckPermissionsUtil;
import com.liujc.cameraview.FileUtil;
import com.liujc.cameraview.JCameraView;


public class CameraViewActivity extends AppCompatActivity {
    private JCameraView mJCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //Andriod6.0 running permission
        CheckPermissionsUtil checkPermissionsUtil = new CheckPermissionsUtil(this);
        checkPermissionsUtil.requestAllPermission(this);
        mJCameraView = (JCameraView) findViewById(R.id.cameraview);
        //设置视频保存路径（如果不设置默认为Environment.getExternalStorageDirectory().getPath()）
        mJCameraView.setAutoFoucs(false);
//        mJCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath());
        mJCameraView.setCameraViewListener(new JCameraView.CameraViewListener() {
            @Override
            public void quit() {
                CameraViewActivity.this.finish();
            }
            @Override
            public void captureSuccess(Bitmap bitmap) {
                FileUtil.saveBitmap(bitmap);
                Toast.makeText(CameraViewActivity.this, "获取到照片Bitmap:" + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void recordSuccess(String url) {
                Toast.makeText(CameraViewActivity.this, "获取到视频路径:" + url, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mJCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJCameraView.onPause();
    }
}
