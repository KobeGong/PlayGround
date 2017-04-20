package com.baidu.rxandroidtaste;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class CameraActivity extends AppCompatActivity implements Camera.PreviewCallback {

    private static final String TAG = "CameraActivity";

    @Bind(R.id.cameraViewHolder)
    FrameLayout cameraViewHolder;
    private Handler cameraHandler;
    private CameraPreview cameraPreview;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        CameraThread cameraThread = new CameraThread("cameraThread");
        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());
        camera = getCameraInstance();
        camera.setDisplayOrientation(90);
        cameraPreview = new CameraPreview(this, camera);
        cameraViewHolder.addView(cameraPreview);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d("gonggaofeng", "data.length = " + data.length);
    }

    private Camera.Size closestSize;

    private Camera getCameraInstance() {
        int cameraId = findFontCameraId();
        camera = Camera.open(cameraId);
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        closestSize = supportedPreviewSizes.get(0);
        for (int i = 0; i < 3; i++) {
            ByteBuffer bb = ByteBuffer.allocateDirect(closestSize.width * closestSize.height
                    * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8);
            camera.addCallbackBuffer(bb.array());
        }
        camera.setPreviewCallbackWithBuffer(this);
        return camera;
    }

    private int findFontCameraId() {
        int size = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < size; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_FRONT) {
                return i;
            }
        }
        return 0;
    }

    private static class CameraThread extends HandlerThread {
        CameraThread(String name) {
            super(name);
        }
    }

    /**
     * A basic Camera preview class
     */
    public static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;
        private SurfaceTexture surfaceTexture;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            int[] texName = new int[1];
            GLES20.glGenTextures(1, texName, 0);
            surfaceTexture = new SurfaceTexture(texName[0]);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());

            }
        }
    }
}
