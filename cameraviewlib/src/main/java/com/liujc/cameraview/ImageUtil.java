package com.liujc.cameraview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 类名称：ImageUtil
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/1 9:32
 * 描述：TODO
 */
public class ImageUtil {
    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotateBitmap;
    }
}
