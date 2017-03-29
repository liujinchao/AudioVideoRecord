package com.liujc.audiorecordlib.audio.waveAudioRecord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.liujc.audiorecordlib.R;
import com.liujc.audiorecordlib.audio.common.AudioRecorder;
import com.liujc.audiorecordlib.audio.waveAudioRecord.view.RecordVoiceButton;

public class WaveAudioRecordActivity extends AppCompatActivity {

    /**
     * 语音列表
     */
    private ListView listView;
    /**
     * 开始录音
     */
    private VoiceAdapter adapter;
    private RecordVoiceButton mBtRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record_wave);

        mBtRec = (RecordVoiceButton) findViewById(R.id.button_rec);
        listView = (ListView) findViewById(R.id.lv);
        adapter=new VoiceAdapter(this);
        listView.setAdapter(adapter);
        mBtRec.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                adapter.add(new AudioRecorder(length, strLength, filePath) )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ;
            }
        });
    }

}
