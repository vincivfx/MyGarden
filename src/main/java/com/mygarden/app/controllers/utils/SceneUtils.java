package com.mygarden.app.controllers.utils;

import java.io.IOException;
import java.util.ResourceBundle;

import com.mygarden.app.LanguageManager;
import com.mygarden.app.controllers.AbstractController;
import com.mygarden.app.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Scene / popup helper utilities.
 *
 * Enhancements:
 * - Always load FXML with LanguageManager.getBundle() so FXML %keys resolve.
 * - Support resolving/formatting messages from the ResourceBundle.
 * - New convenience overloads showConfirmationPopupFromKey / showPopupFromKey.
 */
public class SceneUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, User currentUser) throws IOException {
        // ensure FXML is loaded with the current ResourceBundle so %keys resolve
        ResourceBundle bundle = LanguageManager.getBundle();
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile), bundle);

        Scene newScene = new Scene(fxmlLoader.load());

        if (currentUser != null) {
            Object ctrl = fxmlLoader.getController();
            if (ctrl instanceof AbstractController) {
                ((AbstractController) ctrl).setUser(currentUser);
            }
        }

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }

    // -------------------------
    // Confirmation popups
    // -------------------------

    /**
     * Show confirmation popup using an explicit string (keeps backwards compatibility).
     * If the provided questionText matches a key in the bundle, the localized pattern will be used.
     *
     * @param questionText either a literal message or a bundle key
     * @return true if user confirmed, false otherwise
     */
    public static boolean showConfirmationPopup(String questionText) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();
        String message = resolveMessage(bundle, questionText);
        return showConfirmationPopupInternal(message, bundle);
    }

    /**
     * Show confirmation popup using a bundle key and formatting arguments.
     * This is the recommended way to show localized, formatted confirmations.
     *
     * Example:
     * SceneUtils.showConfirmationPopupFromKey("popup.purchase.confirm", shopItem.getName());
     *
     * @param key  the resource bundle key (e.g. "popup.purchase.confirm")
     * @param args formatting args for String.format
     * @return true if user confirmed, false otherwise
     */
    public static boolean showConfirmationPopupFromKey(String key, Object... args) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();
        String message = resolveMessage(bundle, key, args);
        return showConfirmationPopupInternal(message, bundle);
    }

    /**
     * Internal implementation that actually loads the FXML and shows the modal dialog.
     */
    private static boolean showConfirmationPopupInternal(String message, ResourceBundle bundle) throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // block main window
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setResizable(false);

        // load with bundle so %keys in FXML resolve correctly
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/mygarden/app/utils/confirmation-popup-view.fxml"), bundle);
        Scene popUpScene = new Scene(fxmlLoader.load());

        ConfirmationPopUpController controller = fxmlLoader.getController();
        if (controller != null) {
            try {
                controller.setQuestion(message);
            } catch (Exception e) {
                // don't fail popup if controller doesn't support dynamic text
                e.printStackTrace();
            }
        }

        popup.setScene(popUpScene);
        popup.showAndWait();

        return controller != null && controller.isConfirmed();
    }

    // -------------------------
    // Informational popups
    // -------------------------

    /**
     * Show informational popup using an explicit string (keeps backwards compatibility).
     * If the provided text matches a key in the bundle, the localized message will be used.
     *
     * @param text either a literal message or a bundle key
     */
    public static void showPopup(String text) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();
        String message = resolveMessage(bundle, text);
        showPopupInternal(message, bundle);
    }

    /**
     * Show informational popup by looking up a bundle key and formatting it with args.
     *
     * Example:
     * SceneUtils.showPopupFromKey("popup.plant.bought");
     *
     * @param key  resource bundle key
     * @param args optional formatting args
     */
    public static void showPopupFromKey(String key, Object... args) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();
        String message = resolveMessage(bundle, key, args);
        showPopupInternal(message, bundle);
    }

    private static void showPopupInternal(String message, ResourceBundle bundle) throws IOException {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // block main window
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setResizable(false);

        // load with bundle so %keys in FXML resolve correctly
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/mygarden/app/utils/popup-view.fxml"), bundle);
        Scene popUpScene = new Scene(fxmlLoader.load());

        PopUpController controller = fxmlLoader.getController();
        if (controller != null) {
            try {
                controller.setText(message);
            } catch (Exception e) {
                // if controller doesn't support dynamic text, ignore and let bundle default show
                e.printStackTrace();
            }
        }

        popup.setScene(popUpScene);
        popup.showAndWait();
    }

    // -------------------------
    // Helpers
    // -------------------------

    /**
     * Resolve a message from the bundle if possible, otherwise return the literal string.
     * If args provided, formats using String.format.
     */
    private static String resolveMessage(ResourceBundle bundle, String keyOrLiteral, Object... args) {
        String pattern = keyOrLiteral == null ? "" : keyOrLiteral;
        try {
            if (bundle != null && keyOrLiteral != null && bundle.containsKey(keyOrLiteral)) {
                pattern = bundle.getString(keyOrLiteral);
            }
        } catch (Exception ignored) {
            // fall back to literal
        }

        if (args != null && args.length > 0) {
            try {
                return String.format(pattern, args);
            } catch (Exception e) {
                // formatting failed, return unformatted pattern or fallback
                e.printStackTrace();
                return pattern;
            }
        } else {
            return pattern;
        }
    }
}
