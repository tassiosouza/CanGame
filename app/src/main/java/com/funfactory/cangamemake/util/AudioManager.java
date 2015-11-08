package com.funfactory.cangamemake.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;

public class AudioManager {

    private MediaRecorder mRecorder  = null;
    private boolean       mRecording = false;

    private MediaPlayer   mPlayer    = null;

    private File          mFile;

    public AudioManager() {
        mPlayer = new MediaPlayer();
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    public void gravar(Activity activity) {
        mRecording = true;
        mFile = getOutputAudioFile(activity);
        startRecording();
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            // Error
        }

        beep();
        mRecorder.start();
        mRecording = true;
    }

    public boolean isRecording() {
        return mRecording;
    }

    public boolean isPlaying() {

        boolean state = false;

        try {
            if (mPlayer != null) {
                state = mPlayer.isPlaying();
            }
        } catch (Exception e) {
            // Do Nothing
        }

        return state;
    }

    public String pararGravacao() {
        if (mRecording) {
            beep();
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            mRecording = false;

            Uri uri = Uri.fromFile(mFile);
            return uri.toString();
        } else {
            return "";
        }
    }

    public void executar(String filePath) {

        if (filePath != null) {

            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            } else {
                mPlayer.release();
                mPlayer = new MediaPlayer();
            }

            try {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();
                mPlayer.start();

            } catch (IOException e) {
                // Error
            }
        }
    }

    public void executar(String filePath, OnCompletionListener listener) {
        if (filePath != null) {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            } else {
                mPlayer.release();
                mPlayer = new MediaPlayer();
            }

            try {
                mPlayer.setDataSource(filePath);
                mPlayer.setOnCompletionListener(listener);
                mPlayer.prepare();
                mPlayer.start();

            } catch (IOException e) {
                // Error
            }
        }
    }

    public void pararExecAudio() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
	private File getOutputAudioFile(Activity activity) {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                activity.getPackageName());

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());

        File ff = new File(directory.getPath() + File.separator + "AUDIO_" + timeStamp + ".3gp");
        return ff;
    }

    private void beep() {
        ToneGenerator toneG = new ToneGenerator(android.media.AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

}
