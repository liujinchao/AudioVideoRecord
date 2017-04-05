package com.liujc.cameraview;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 类名称：FileUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/1 9:31
 * 描述：TODO
 */
public class FileUtil {
    private static final  String TAG = "FileUtil";
    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = "CameraView";

    public static String getSDPath(){
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return "";
        }
    }
    public static String getVideoPath(){
        storagePath = initPath() +"/video";
        File f = new File(storagePath);
        if(!f.exists()){
            f.mkdirs();
        }
        return storagePath;
    }
    public static String getPicPath(){
            storagePath = initPath() +"/pic";
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdirs();
            }
        return storagePath;
    }

    private static String initPath(){
        return getSDPath()+"/" + DST_FOLDER_NAME;
    }

    public static void saveBitmap(Bitmap b){

        String path = getPicPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap success");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:fail");
            e.printStackTrace();
        }

    }

    public static boolean isExternalStorageWritable(){
        String state=Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }
}
