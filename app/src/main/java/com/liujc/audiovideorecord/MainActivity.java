package com.liujc.audiovideorecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liujc.audiorecordlib.audio.waveAudioRecord.WaveAudioRecordActivity;
import com.liujc.audiorecordlib.audio.wechatAudioRecorde.AudioRecordActivity;
import com.liujc.audiorecordlib.video.VideoRecordActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        find(R.id.wechat_audio_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToTarget(AudioRecordActivity.class);
            }
        });
        find(R.id.audio_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToTarget(WaveAudioRecordActivity.class);
            }
        });
        find(R.id.video_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToTarget(VideoRecordActivity.class);
            }
        });
        find(R.id.ijk_video_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToTarget(IjkPlayerActivity.class);
            }
        });
        find(R.id.camera_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToTarget(CameraViewActivity.class);
            }
        });
    }
    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
    }
    private void jumpToTarget(Class target){
        Intent intent = new Intent(MainActivity.this,target);
        startActivity(intent);
    }
}
