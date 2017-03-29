package com.liujc.audiorecordlib.audio.wechatAudioRecorde;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

public class MediaManager {
	private static MediaPlayer mediaPlayer;
	private static boolean isPause;

	public static void playSound(String filePath,
			OnCompletionListener onCompletionListener) {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mediaPlayer.reset();
					return false;
				}
			});
		} else {
			mediaPlayer.reset();
		}
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(onCompletionListener);
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}
	
	public static void resume(){
		if (mediaPlayer != null && isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}
	
	public static void release(){
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

}
