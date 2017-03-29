package com.liujc.audiorecordlib.video;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.liujc.audiorecordlib.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.liujc.audiorecordlib.video.CommonUtils.SIZE_1;
import static com.liujc.audiorecordlib.video.CommonUtils.SIZE_2;
import static com.liujc.audiorecordlib.video.CommonUtils.getScreenHeight;
import static com.liujc.audiorecordlib.video.CommonUtils.getScreenWidth;

public class VideoRecordActivity extends AppCompatActivity implements SurfaceHolder.Callback,View.OnClickListener {

    SurfaceView cameraShowView;
    ImageView videoFlashLight;
    Chronometer videoTime;
    ImageView swicthCamera;
    ImageView recordButton;

    MediaRecorder recorder;

    SurfaceHolder surfaceHolder;

    Camera camera;

    OrientationEventListener orientationEventListener;

    File videoFile;

    int rotationRecord = 90;

    int rotationFlag = 90;

    int flashType;

    int frontRotate;

    int frontOri;

    int cameraType = 0;

    int cameraFlag = 1; //1为后置

    boolean flagRecord = false;//是否正在录像


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        initView();
    }
    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (flagRecord) {
                endRecord();
                if (camera != null && cameraType == 0) {
                    //关闭后置摄像头闪光灯
                    camera.lock();
                    FlashLogic(camera.getParameters(), 0, true);
                    camera.unlock();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (flagRecord) {
            //如果是录制中的就完成录制
            onPause();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.video_flash_light) {
            clickFlash();
        } else if (i == R.id.swicth_camera) {
            switchCamera();
        } else if (i == R.id.record_button) {
            clickRecord();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        initCamera(cameraType, false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        endRecord();
        releaseCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        endRecordUI();
    }

    private void initView() {
        cameraShowView = find(R.id.camera_show_view);
        videoFlashLight = find(R.id.video_flash_light);
        videoFlashLight.setOnClickListener(this);
        videoTime = find(R.id.video_time);
        swicthCamera = find(R.id.swicth_camera);
        swicthCamera.setOnClickListener(this);
        recordButton = find(R.id.record_button);
        recordButton.setOnClickListener(this);
        doStartSize();
        SurfaceHolder holder = cameraShowView.getHolder();
        holder.addCallback(this);
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rotationUIListener();
    }


    /**
     * 开始录制时候的状态
     */
    private void startRecordUI() {
        swicthCamera.setVisibility(View.GONE); // 旋转摄像头关闭
        videoFlashLight.setVisibility(View.GONE); //闪光灯关闭
        recordButton.setImageResource(R.drawable.stop_record);   //录制按钮变成待停止
    }

    /**
     * 停止录制时候的状态
     */
    private void endRecordUI() {
        swicthCamera.setVisibility(View.VISIBLE); // 旋转摄像头关闭
        videoFlashLight.setVisibility(View.VISIBLE); //闪光灯关闭
        recordButton.setImageResource(R.drawable.record);   //录制按钮变成待停止
    }

    /**
     * 录制按键
     */
    private void clickRecord() {
        if (!flagRecord) {
            if (startRecord()) {
                startRecordUI();
                videoTime.setBase(SystemClock.elapsedRealtime());
                videoTime.start();
                videoTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        //最大录制时长
                        if (chronometer.getText().equals("00:61")) {
                            if (flagRecord) {
                                endRecord();
                            }
                        }
                    }
                });
            }
        } else {
            endRecord();
        }
    }

    /**
     * 旋转界面UI
     */
    private void rotationUIListener() {
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (!flagRecord) {
                    if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                        // 竖屏拍摄
                        if (rotationFlag != 0) {
                            //旋转logo
                            rotationAnimation(rotationFlag, 0);
                            //这是竖屏视频需要的角度
                            rotationRecord = 90;
                            //这是记录当前角度的flag
                            rotationFlag = 0;
                        }
                    } else if (((rotation >= 230) && (rotation <= 310))) {
                        // 横屏拍摄
                        if (rotationFlag != 90) {
                            //旋转logo
                            rotationAnimation(rotationFlag, 90);
                            //这是正横屏视频需要的角度
                            rotationRecord = 0;
                            //这是记录当前角度的flag
                            rotationFlag = 90;
                        }
                    } else if (rotation > 30 && rotation < 95) {
                        // 反横屏拍摄
                        if (rotationFlag != 270) {
                            //旋转logo
                            rotationAnimation(rotationFlag, 270);
                            //这是反横屏视频需要的角度
                            rotationRecord = 180;
                            //这是记录当前角度的flag
                            rotationFlag = 270;
                        }
                    }
                }
            }
        };
        orientationEventListener.enable();
    }


    private void rotationAnimation(int from, int to) {
        ValueAnimator progressAnimator = ValueAnimator.ofInt(from, to);
        progressAnimator.setDuration(300);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentAngle = (int) animation.getAnimatedValue();
                videoFlashLight.setRotation(currentAngle);
                videoTime.setRotation(currentAngle);
                swicthCamera.setRotation(currentAngle);
            }
        });
        progressAnimator.start();
    }

    /**
     * 因为录制改分辨率的比例可能和屏幕比例一直，所以需要调整比例显示
     */
    private void doStartSize() {
        int screenWidth = getScreenWidth(this);
        int screenHeight = getScreenHeight(this);
        CommonUtils.setViewSize(cameraShowView, screenWidth * SIZE_1 / SIZE_2, screenHeight);
    }

    /**
     * 初始化相机
     *
     * @param type    前后的类型
     * @param flashDo 赏光灯是否工作
     */
    private void initCamera(int type, boolean flashDo) {

        if (camera != null) {
            //如果已经初始化过，就先释放
            releaseCamera();
        }

        try {
            camera = Camera.open(type);
            if (camera == null) {
                showCameraPermission();
                return;
            }
            camera.lock();

            //Point screen = new Point(getScreenWidth(this), getScreenHeight(this));
            //现在不用获取最高的显示效果
            //Point show = getBestCameraShow(camera.getParameters(), screen);

            Camera.Parameters parameters = camera.getParameters();
            if (type == 0) {
                //基本是都支持这个比例
                parameters.setPreviewSize(SIZE_1, SIZE_2);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
                camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
            }
            camera.setParameters(parameters);
            FlashLogic(camera.getParameters(), flashType, flashDo);
            if (cameraType == 1) {
                frontCameraRotate();
                camera.setDisplayOrientation(frontRotate);
            } else {
                camera.setDisplayOrientation(90);
            }
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.unlock();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
    }

    private boolean startRecord() {

        //懒人模式，根据闪光灯和摄像头前后重新初始化一遍，开期闪光灯工作模式
        initCamera(cameraType, true);

        if (recorder == null) {
            recorder = new MediaRecorder();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || camera == null || recorder == null) {
            camera = null;
            recorder = null;
            //还是没权限啊
            showCameraPermission();
            return false;
        }

        try {

            recorder.setCamera(camera);
            // 这两项需要放在setOutputFormat之前
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // Set output file format，输出格式
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //必须在setEncoder之前
            recorder.setVideoFrameRate(15);  //帧数  一分钟帧，15帧就够了
            recorder.setVideoSize(SIZE_1, SIZE_2);//这个大小就够了

            // 这两项需要放在setOutputFormat之后
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

            recorder.setVideoEncodingBitRate(3 * SIZE_1 * SIZE_2);//第一个数字越大，清晰度就越高，考虑文件大小的缘故，就调整为1
            int frontRotation;
            if (rotationRecord == 180) {
                //反向的前置
                frontRotation = 180;
            } else {
                //正向的前置
                frontRotation = (rotationRecord == 0) ? 270 - frontOri : frontOri; //录制下来的视屏选择角度，此处为前置
            }
            recorder.setOrientationHint((cameraType == 1) ? frontRotation : rotationRecord);
            //把摄像头的画面给它
            recorder.setPreviewDisplay(surfaceHolder.getSurface());
            //创建好视频文件用来保存
            videoDir();
            if (videoFile != null) {
                //设置创建好的输入路径
                recorder.setOutputFile(videoFile.getPath());
                recorder.prepare();
                recorder.start();
                //不能旋转啦
                orientationEventListener.disable();
                flagRecord = true;
            }
        } catch (Exception e) {
            //一般没有录制权限或者录制参数出现问题都走这里
            e.printStackTrace();
            //还是没权限啊
            recorder.reset();
            recorder.release();
            recorder = null;
            showCameraPermission();
            FileUtils.deleteFile(videoFile.getPath());
            return false;
        }
        return true;

    }

    private void endRecord() {
        //反正多次进入，比如surface的destroy和界面onPause
        if (!flagRecord) {
            return;
        }
        flagRecord = false;
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                orientationEventListener.enable();
                recorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoTime.stop();
        videoTime.setBase(SystemClock.elapsedRealtime());
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(PlayActivity.DATA, videoFile.getAbsolutePath());
        startActivityForResult(intent, 2222);
    }

    public void clickFlash() {
        if (camera == null) {
            return;
        }
        camera.lock();
        Camera.Parameters p = camera.getParameters();
        if (flashType == 0) {
            FlashLogic(p, 1, false);
        } else {
            FlashLogic(p, 0, false);

        }
        camera.unlock();
    }

    /**
     * 释放摄像头资源
     */
    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 闪光灯逻辑
     *
     * @param p    相机参数
     * @param type 打开还是关闭
     * @param isOn 是否启动
     */
    private void FlashLogic(Camera.Parameters p, int type, boolean isOn) {
        flashType = type;
        if (type == 0) {
            if (isOn) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
            }
            videoFlashLight.setImageResource(R.drawable.flash_off);
        } else {
            if (isOn) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
            }
            videoFlashLight.setImageResource(R.drawable.flash);
        }
        if (cameraFlag == 0) {
            videoFlashLight.setVisibility(View.GONE);
        } else {
            videoFlashLight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数0或者1;

        try {
            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                if (cameraFlag == 1) {
                    //后置到前置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                        frontCameraRotate();//前置旋转摄像头度数
                        switchCameraLogic(i, 0, frontRotate);
                        break;
                    }
                } else {
                    //前置到后置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                        switchCameraLogic(i, 1, 90);
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /***
     * 处理摄像头切换逻辑
     *
     * @param i           哪一个，前置还是后置
     * @param flag        切换后的标志
     * @param orientation 旋转的角度
     */
    private void switchCameraLogic(int i, int flag, int orientation) {
        if (camera != null) {
            camera.lock();
        }
        endRecordUI();
        releaseCamera();
        camera = Camera.open(i);//打开当前选中的摄像头
        try {
            camera.setDisplayOrientation(orientation);
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cameraFlag = flag;
        FlashLogic(camera.getParameters(), 0, false);
        camera.startPreview();
        cameraType = i;
        camera.unlock();
    }

    /**
     * 旋转前置摄像头为正的
     */
    private void frontCameraRotate() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(1, info);
        int degrees = getDisplayRotation(this);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        frontOri = info.orientation;
        frontRotate = result;
    }

    /**
     * 获取旋转角度
     */
    private int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }


    private void showCameraPermission() {
        Toast.makeText(this, "您没有开启相机权限或者录音权限", Toast.LENGTH_SHORT).show();
    }

    public String videoDir() {
        File sampleDir = new File(FileUtils.getAppPath());
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            videoFile = File.createTempFile("recording", ".mp4", vecordDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取和屏幕比例最相近的，质量最高的显示
     */
    private Point getBestCameraShow(Camera.Parameters parameters, Point screenResolution) {
        float tmpSize;
        float minDiffSize = 100f;
        float scale = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
            tmpSize = Math.abs(((float) s.height / (float) s.width) - scale);
            if (tmpSize < minDiffSize) {
                minDiffSize = tmpSize;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }


}
