package com.liujc.audiorecordlib.audio.common;

import android.os.Environment;

/**
 * 类名称：RecordUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/3/23 11:25
 * 描述：TODO
 * 最近修改时间：2017/3/23 11:25
 * 修改人：Modify by liujc
 */
public class RecordUtil {
    public static String getSDPath(){
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return "";
        }
    }
    public static String getAudioPath(){
        return getSDPath() +"/AudioVideoRecord/audio";
    }
}
