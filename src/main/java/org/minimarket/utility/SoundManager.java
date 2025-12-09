package org.minimarket.utility;

import javafx.scene.media.AudioClip;
import java.net.URL;

/**
 * SoundManager handles loading and playing sound effects across the Mini Market application.
 *
 * This utility class improves the user experience by providing auditory feedback
 * for actions such as clicking buttons, completing operations successfully,
 * or encountering errors.
 *
 * It loads all sounds once at startup and provides simple play methods that
 * controllers can call when needed.
 */
public class SoundManager {

    /** Sound played for general button clicks */
    private final AudioClip clickSound;

    /** Sound played when an action is successful (e.g., item added) */
    private final AudioClip successSound;

    /** Sound played for errors or invalid actions */
    private final AudioClip errorSound;

    /**
     * Constructor loads all sound files from the resources folder.
     * Each sound is retrieved using the classpath to support packaging into a JAR.
     */
    public SoundManager() {
        clickSound = loadSound("/sounds/click.wav");
        successSound = loadSound("/sounds/success.wav");
        errorSound = loadSound("/sounds/error.wav");
    }

    /**
     * Loads a sound file from the resources directory and converts it to an AudioClip.
     *
     * @param path the resource path of the sound file
     * @return an AudioClip if the file exists; null otherwise
     */
    private AudioClip loadSound(String path) {
        try {
            URL resource = SoundManager.class.getResource(path);

            if (resource != null) {
                return new AudioClip(resource.toExternalForm());
            } else {
                System.err.println("Missing sound file: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading sound: " + path);
            return null;
        }
    }

    /**
     * Plays a click sound (used for button presses).
     */
    public void playClickSound() {
        if (clickSound != null) clickSound.play();
    }

    /**
     * Plays a success sound (used for correct input, completed tasks).
     */
    public void playSuccessSound() {
        if (successSound != null) successSound.play();
    }

    /**
     * Plays an error sound (used for invalid input or failed operations).
     */
    public void playErrorSound() {
        if (errorSound != null) errorSound.play();
    }
}

