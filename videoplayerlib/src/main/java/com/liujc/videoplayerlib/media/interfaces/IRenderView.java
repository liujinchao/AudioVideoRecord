/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liujc.videoplayerlib.media.interfaces;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IRenderView {
    /**
     * 视频显示模式：View 是视频画面能显示的控件
     * 0 -> 填满View一边，另一边按比例缩小，整个视频显示在View里面
     * 1 -> 整个填满View，可能有一边会被裁剪一部分画面
     * 2 -> 根据原始视频大小来测量，不会超出View范围
     * 3 -> 按View的原始测量值来设定
     * 4 -> 按16：9的比例来处理，不会超出View范围
     * 5 -> 按4：3的比例来处理，不会超出View范围
     */
    int AR_ASPECT_FIT_PARENT = 0; // without clip
    int AR_ASPECT_FILL_PARENT = 1; // may clip
    int AR_ASPECT_WRAP_CONTENT = 2;
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4;
    int AR_4_3_FIT_PARENT = 5;

    // add, 设置 Matrix，来做缩放操作
    void setTransform(Matrix transform);
    // add, 获取 Matrix，来做缩放操作
    Matrix getTransform();
    // add, 获取截图
    Bitmap getVideoScreenshot();

    /**获取外层界面*/
    View getView();

    /**
     * 是否需要等待重置大小
     */
    boolean shouldWaitForResize();

    /**
     * 设置视频界面大小
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频裁剪方式
     */
    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * 设置视频旋转角度
     */
    void setVideoRotation(int degree);

    /**
     * 设置视频裁剪方式
     */
    void setAspectRatio(int aspectRatio);

    /**
     * 添加视频渲染回调
     */
    void addRenderCallback(@NonNull IRenderCallback callback);

    /**
     * 移除视频渲染回调
     */
    void removeRenderCallback(@NonNull IRenderCallback callback);

    interface ISurfaceHolder {

        /**
         * surface界面绑定到mediaplay上
         */
        void bindToMediaPlayer(IMediaPlayer mp);

        /**
         * 获取渲染的view
         */
        @NonNull
        IRenderView getRenderView();

        /**
         * 获取渲染使用的具体view surface
         */
        @Nullable
        SurfaceHolder getSurfaceHolder();

        /**
         * 打开surface界面
         */
        @Nullable
        Surface openSurface();

        /**
         * 获取渲染使用的具体view texture
         */
        @Nullable
        SurfaceTexture getSurfaceTexture();
    }

    interface IRenderCallback {
        /**
         * 创建surface界面大小
         *
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height);

        /**
         * surface界面大小改变监听
         *
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int width, int height);

        /**
         * 界面回收
         */
        void onSurfaceDestroyed(@NonNull ISurfaceHolder holder);
    }
}
