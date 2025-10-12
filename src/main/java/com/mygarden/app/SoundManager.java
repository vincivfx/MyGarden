package com.mygarden.app;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private static SoundManager instance;
    private AudioClip clickClip;
    private AudioClip purchaseClip;

    private SoundManager() {
        try {
            String clickRes = getClass().getResource("/audio/onClick.mp3").toExternalForm();
            clickClip = new AudioClip(clickRes);
        } catch (Exception e) {
            System.err.println("Failed to load click sound: " + e.getMessage());
        }

        try {
            String purchaseRes = getClass().getResource("/audio/onPurchase.mp3").toExternalForm();
            purchaseClip = new AudioClip(purchaseRes);
        } catch (Exception e) {
            System.err.println("Failed to load purchase sound: " + e.getMessage());
        }
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void playClick() {
        playClip(clickClip);
    }

    public void playPurchase() {
        playClip(purchaseClip);
    }

    private void playClip(AudioClip clip) {
        if (clip == null) return;
        double vol = 1.0;
        try {
            vol = AudioManager.getInstance().getSfxVolume();
        } catch (Exception ignored) { }
        clip.setVolume(clamp(vol));
        clip.play();
    }

    private double clamp(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}