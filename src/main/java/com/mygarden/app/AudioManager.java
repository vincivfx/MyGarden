package com.mygarden.app;

import java.util.prefs.Preferences;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    private static final String PREF_KEY_VOLUME = "bg_volume";
    private static final double DEFAULT_VOLUME = 1.0; // 0.0 - 1.0
    private static AudioManager instance;
    private MediaPlayer player;
    private Preferences prefs;

    private AudioManager() {
        prefs = Preferences.userNodeForPackage(AudioManager.class);
        double vol = prefs.getDouble(PREF_KEY_VOLUME, DEFAULT_VOLUME);

        try {
            String resource = getClass().getResource("/audio/background.mp3").toExternalForm();
            Media media = new Media(resource);
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(vol);
            player.setOnError(() -> System.err.println("Media error: " + player.getError()));
        } catch (Exception e) {
            System.err.println("Failed to load background audio: " + e.getMessage());
            player = null;
        }
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    public void play() {
        if (player != null) player.play();
    }

    public void pause() {
        if (player != null) player.pause();
    }

    public boolean isPlaying() {
        return player != null && player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void setVolume(double volume) {
        if (player != null) {
            player.setVolume(clamp(volume));
            prefs.putDouble(PREF_KEY_VOLUME, player.getVolume());
        }
    }

    public double getVolume() {
        return player != null ? player.getVolume() : 0.0;
    }

    private double clamp(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    public void dispose() {
    if (player != null) {
        player.dispose();   // releases file handle
        player = null;
    }
}
}
