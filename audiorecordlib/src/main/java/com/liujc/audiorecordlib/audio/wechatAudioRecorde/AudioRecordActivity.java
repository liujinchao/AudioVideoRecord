package com.liujc.audiorecordlib.audio.wechatAudioRecorde;

import java.util.ArrayList;
import java.util.List;

import com.liujc.audiorecordlib.R;
import com.liujc.audiorecordlib.audio.common.AudioRecorder;
import com.liujc.audiorecordlib.audio.wechatAudioRecorde.view.AudioRecordButton;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * TODO: 1.录音时，如果有正在播放的音频，需要暂停 2.音频对话框长度需要根据录音时间长短变化
 */

public class AudioRecordActivity extends Activity {
	private AudioRecordButton btnRecord;
	private ListView voiceList;
	private ArrayAdapter<AudioRecorder> mAdapter;
	private List<AudioRecorder> mDatas = new ArrayList<AudioRecorder>();

	private AnimationDrawable animation;
	private View voiceAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_record_wechat);
		voiceList = (ListView) findViewById(R.id.voiceList);
		btnRecord = (AudioRecordButton) findViewById(R.id.btnRecord);
		btnRecord
				.setAudioRecordFinishListener(new MyAudioRecordFinishListener());

		mAdapter = new VoiceListAdapter(this, mDatas);
		voiceList.setAdapter(mAdapter);
		voiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 播放动画
				if (animation != null) {
					voiceAnim
							.setBackgroundResource(R.drawable.icon_voice_ripple);
					voiceAnim = null;
				}
				voiceAnim = view.findViewById(R.id.voiceAnim);
				voiceAnim.setBackgroundResource(R.drawable.anim_play_audio);
				animation = (AnimationDrawable) voiceAnim.getBackground();
				animation.start();
				// 播放音频
				MediaManager.playSound(mDatas.get(position).getFilePath(),
						new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								voiceAnim.setBackgroundResource(R.drawable.icon_voice_ripple);
							}
						});

			}
		});
	}

	class MyAudioRecordFinishListener implements AudioRecordButton.AudioRecordFinishListener {

		@Override
		public void onFinish(float second, String filePath) {
			// TODO Auto-generated method stub
			AudioRecorder audioRecorder = new AudioRecorder((long) second, filePath);
			mDatas.add(audioRecorder);
			mAdapter.notifyDataSetChanged();
			voiceList.setSelection(mDatas.size() - 1);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaManager.release();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MediaManager.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MediaManager.resume();
	}

}
