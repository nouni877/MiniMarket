package org.minimarket.utility;

import javafx.scene.Scene;

/**
 * StyleManager is a small utility class responsible for applying a global
 * CSS stylesheet to JavaFX scenes across the Mini Market application.
 *
 * Using a centralized style manager ensures:
 *  - consistent UI appearance
 *  - easier maintenance of visual design
 *  - cleaner code inside controllers (no repeated CSS loading)
 *
 * Any scene passed to this class will automatically load the "style.css"
 * file located in the application's resources.
 */
public class StyleManager {

    /**
     * Applies the global stylesheet to the given scene.
     *
     * @param scene the JavaFX Scene to apply the stylesheet to
     */
    public static void applyStyle(Scene scene) {
        // Add the CSS file stored in the resources folder
        scene.getStylesheets().add("style.css");
    }
}
