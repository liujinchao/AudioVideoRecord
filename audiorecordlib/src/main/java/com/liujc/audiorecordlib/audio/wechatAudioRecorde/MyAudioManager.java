package com.liujc.audiorecordlib.audio.wechatAudioRecorde;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class MyAudioManager {

	private MediaRecorder mediaRecorder;
	private String dir;
	private String currentFilePath;

	private static MyAudioManager audioInstance; // 单例

	public boolean isPrepared = false;

	private MyAudioManager(String dir) {
		this.dir = dir;
	}

	public interface AudioStateChangeListener {
		void wellPrepared();
	}

	public AudioStateChangeListener audioStateChangeListener;

	public void setOnAudioStateChangeListener(AudioStateChangeListener listener) {
		audioStateChangeListener = listener;
	}

	public static MyAudioManager getInstance(String dir) {
		if (audioInstance == null) {
			synchronized (MyAudioManager.class) {
				if (audioInstance == null) {
					audioInstance = new MyAudioManager(dir);
				}
			}
		}
		return audioInstance;
	}

	public void prepareAudio() {
		try {
			isPrepared = false;
			File fileDir = new File(dir);
			if (!fileDir.exists())
				fileDir.mkdirs();
			String fileName = generateFileName();
			File file = new File(fileDir, fileName);

			currentFilePath = file.getAbsolutePath();
			mediaRecorder = new MediaRecorder();
			// 设置输出文件
			mediaRecorder.setOutputFile(file.getAbsolutePath());
			// 设置音频源
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置音频格式
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			// 设置音频编码
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mediaRecorder.prepare();
			mediaRecorder.start();
			// 准备结束
			isPrepared = true;
			//
			if (audioStateChangeListener != null) {
				audioStateChangeListener.wellPrepared();
			}
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 随机生成文件名称
	 *
	 * @return
	 */
	private String generateFileName() {
		return UUID.randomUUID().toString() + ".amr";
	}

	public int getVoiceLevel(int maxLevel) {
		if (isPrepared) {
			try {
				// 振幅范围mediaRecorder.getMaxAmplitude():1-32767
				return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
			} catch (Exception e) {
			}
		}
		return 1;
	}

	public void release() {
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;

	}

	public void cancel() {
		release();
		if (currentFilePath != null) {
			File file = new File(currentFilePath);
			file.delete();
			currentFilePath = null;
		}
	}

	public String getCurrentPath() {
		return currentFilePath;
	}
}
