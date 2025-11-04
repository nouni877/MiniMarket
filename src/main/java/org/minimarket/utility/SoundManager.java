package org.minimarket.utility;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class SoundManager {

    private final AudioClip clickSound;
    private final AudioClip successSound;
    private final AudioClip errorSound;

    public SoundManager() {
        clickSound = loadSound("/sounds/click.wav");
        successSound = loadSound("/sounds/success.wav");
        errorSound = loadSound("/sounds/error.wav");
    }

    private AudioClip loadSound(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                return new AudioClip(resource.toExternalForm());
            } else {
                System.err.println("⚠️ Missing sound file: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading sound: " + path);
            return null;
        }
    }

    public void playClickSound() {
        if (clickSound != null) clickSound.play();
    }

    public void playSuccessSound() {
        if (successSound != null) successSound.play();
    }

    public void playErrorSound() {
        if (errorSound != null) errorSound.play();
    }
}
