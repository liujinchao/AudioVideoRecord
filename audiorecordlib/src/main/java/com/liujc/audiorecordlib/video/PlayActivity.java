package com.liujc.audiorecordlib.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.liujc.audiorecordlib.R;

public class PlayActivity extends AppCompatActivity {

    public final static String DATA = "URL";

    PlayView playView;
    Button playBtn;
    RelativeLayout activityPlay;

    private long playPostion = -1;
    private long duration = -1;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record_play);

        uri = getIntent().getStringExtra(DATA);

        initView();
        playView.setVideoURI(Uri.parse(uri));

        playView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playView.seekTo(1);
                startVideo();
            }
        });

        playView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //获取视频资源的宽度
                int videoWidth = mp.getVideoWidth();
                //获取视频资源的高度
                int videoHeight = mp.getVideoHeight();
                playView.setSizeH(videoHeight);
                playView.setSizeW(videoWidth);
                playView.requestLayout();
                duration = mp.getDuration();
                play();
            }
        });

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (!isScreenOn) {
            pauseVideo();
        }
    }

    private void initView() {
        playView = find(R.id.playView);
        playBtn = find(R.id.playBtn);
        activityPlay = find(R.id.activity_play);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
    }
    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (playPostion > 0) {
            pauseVideo();
        }
        playView.seekTo((int) ((playPostion > 0 && playPostion < duration) ? playPostion : 1));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playView.pause();
        playPostion = playView.getCurrentPosition();
        pauseVideo();

    }

    @Override
    public void onBackPressed() {
        FileUtils.deleteFile(uri);
        finish();
    }


    private void pauseVideo() {
        playView.pause();
        playBtn.setText("播放");
    }

    private void startVideo() {
        playView.start();
        playBtn.setText("停止");
    }

    /**
     * 播放
     */
    private void play() {
        if (playView.isPlaying()) {
            pauseVideo();
        } else {
            if (playView.getCurrentPosition() == playView.getDuration()) {
                playView.seekTo(0);
            }
            startVideo();
        }
    }


}
