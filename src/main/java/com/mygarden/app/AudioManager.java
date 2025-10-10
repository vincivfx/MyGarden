package com.mygarden.app;

import java.util.prefs.Preferences;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class AudioManager {
    private static AudioManager instance;
    private MediaPlayer mediaPlayer;
    private Preferences prefs;

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final String SFX_VOLUME_KEY = "sfxVolume";

    private AudioManager() {
        prefs = Preferences.userNodeForPackage(AudioManager.class);
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    // --- Music control ---
    public void playMusic(String resourcePath, boolean loop) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            Media media = new Media(getClass().getResource(resourcePath).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(getMusicVolume());
            if (loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Failed to play music: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setMusicVolume(double volume) {
        double v = clamp(volume);
        prefs.putDouble(MUSIC_VOLUME_KEY, v);
        if (mediaPlayer != null) mediaPlayer.setVolume(v);
    }

    public double getMusicVolume() {
        return prefs.getDouble(MUSIC_VOLUME_KEY, 0.5);
    }

    public void setSfxVolume(double volume) {
        prefs.putDouble(SFX_VOLUME_KEY, clamp(volume));
    }

    public double getSfxVolume() {
        return prefs.getDouble(SFX_VOLUME_KEY, 0.5);
    }

    public boolean isMusicPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    private double clamp(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}

