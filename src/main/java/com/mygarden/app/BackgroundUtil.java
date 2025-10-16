package com.mygarden.app;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class BackgroundUtil {
    private BackgroundUtil() {}

    public static void applyMainBackground(ImageView target, ResourceBundle bundle) {
        if (target == null) return;

        String path = null;
        URL url = null;

        try {
            if (bundle != null && bundle.containsKey("main.background")) {
                path = bundle.getString("main.background").trim();
                if (!path.isEmpty()) {
                    url = BackgroundUtil.class.getResource(path);
                    if (url == null) {
                        url = BackgroundUtil.class.getResource("/" + path);
                    }
                }
            }
        } catch (Exception ignored) {}

        if (url == null) {
            String lang = "en";
            try {
                if (bundle != null && bundle.getLocale() != null && bundle.getLocale().getLanguage() != null) {
                    lang = bundle.getLocale().getLanguage();
                }
            } catch (Exception ignored) {}
            url = BackgroundUtil.class.getResource(String.format("/images/MainPage_%s.jpg", lang));
            if (url == null) {
                url = BackgroundUtil.class.getResource(String.format("/images/MainPage_%s.png", lang));
            }
        }

        if (url == null) {
            url = BackgroundUtil.class.getResource("/images/MainPage.jpg");
            if (url == null) url = BackgroundUtil.class.getResource("/images/MainPage.png");
        }

        if (url != null) {
            try {
                Image img = new Image(url.toExternalForm(), true);
                target.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("BackgroundUtil: no MainPage image found in resources.");
        }
    }
}

