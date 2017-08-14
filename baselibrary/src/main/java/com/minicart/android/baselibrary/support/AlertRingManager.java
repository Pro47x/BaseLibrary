package com.minicart.android.baselibrary.support;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * @类名：AlertRingUtil
 * @描述：
 * @创建人：54506
 * @创建时间：2016/8/29 16:57
 * @版本：
 */
public class AlertRingManager implements MediaPlayer.OnErrorListener, Closeable {
    private static final String TAG = AlertRingManager.class.getSimpleName();
    private static final float BEEP_VOLUME = 1f;
    private static final long VIBRATE_DURATION = 500L;

    private AudioManager am;

    private MediaPlayer mediaPlayer;
    private Activity activity;
    private final int resourcesID;
    private boolean playBeep;
    private boolean vibrate;

//    private AlertRingManager(Context context) {
//        mContext = context;
//        realUri = Uri.parse("android.resource://" + getContext().getApplicationContext().getPackageName() + "/" + R.raw.notification);
//        timeUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.over_the_horizon);
//        msgUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.new_msg);
//    }

    public AlertRingManager(Activity activity, int resourcesID) {
        this.activity = activity;
        this.mediaPlayer = null;
        this.resourcesID = resourcesID;
        updatePrefs();
    }

    private static boolean shouldBeep(Context activity) {
        boolean shouldPlayBeep = true;
        // See if sound settings overrides this
        AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            shouldPlayBeep = false;
        }
        return shouldPlayBeep;
    }

    public synchronized void updatePrefs() {
        playBeep = shouldBeep(activity);
        vibrate = true;
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer(activity);
        }
    }

    public synchronized void playBeepSoundAndVibrate() {
        setMaxVolume();
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private MediaPlayer buildMediaPlayer(Context activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor file = activity.getResources().openRawResourceFd(resourcesID);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            } finally {
                file.close();
            }
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer.release();
            return null;
        }
    }

    /**
     * 调整为最大音量
     */
    private void setMaxVolume() {
        if (am == null) {
            am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);//改成最大音量
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            // we are finished, so put up an appropriate requestError toast if required and finish
            activity.finish();
        } else {
            // possibly media player requestError, so release and recreate
            close();
            updatePrefs();
        }
        return true;
    }

    @Override
    public synchronized void close() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setLooping(boolean looping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }
}
