package com.liujc.cameraview;

/**
 * 类名称：CameraFocusListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/4/1 9:23
 * 描述：TODO
 */
public interface CameraFocusListener {
    void onFocusBegin(float x, float y);
    void onFocusEnd();
}
