package com.liujc.audiorecordlib.audio.common;

/**
 * 类名称：AudioRecorder
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/3/23 09:30
 * 描述：TODO
 * 最近修改时间：2017/3/23 09:30
 * 修改人：Modify by liujc
 */
public class AudioRecorder {
    long audioLength;
    String filePath;
    private long length;
    private String strLength;

    public AudioRecorder(long audioLength, String filePath) {
        super();
        this.audioLength = audioLength;
        this.filePath = filePath;
    }
    public AudioRecorder(long length, String strLength, String filePath) {
        this.length=length;
        this.strLength=strLength;
        this.filePath=filePath;
    }

    public float getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(long audioLength) {
        this.audioLength = audioLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getStrLength() {
        return strLength;
    }

    public void setStrLength(String strLength) {
        this.strLength = strLength;
    }
}
