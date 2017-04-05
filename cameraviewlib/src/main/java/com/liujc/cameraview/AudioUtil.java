package com.liujc.cameraview;

import android.content.Context;
import android.media.AudioManager;

/**
 * 类名称：AudioUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/1 9:22
 * 描述：音频处理管理器
 */
public class AudioUtil {
    public static void setAudioManage(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
    }
}
