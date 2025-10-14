package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t14-3.github.io/tp/UserGuide.html";
    public static final String USER_COMMANDS = """
            Commands List:
            Add a restaurant
            add n/NAME a/ADDRESS hp/PHONE_NUMBER [t/TAG]
            Example: add n/McDonald's a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 hp/68928572
            Example: add n/KFC a/701A Yishun Ave 5, #01-02, Singapore 761701 hp/62226111 t/fastfood t/chicken\n
            List all restaurants
            list\n
            Delete a restaurant
            delete <index>
            Example: delete 3\n
            Edit a restaurant
            edit <index> n/NAME a/ADDRESS hp/PHONE_NUMBER
            Example: edit 1 n/Subway hp/66591189
            Example: edit 2 a/701A Yishun Ave 5, #01-02, Singapore 761701\n
            Rate a restaurant from 0 to 5 stars
            rate <index> <rating>
            Example: rate 1 5\n
            Find a restaurant
            find <keyword>
            Example: find mcdonald
            Example: find halal, fastfood\n
            Tag a restaurant
            tag <index> t/TAG
            Example: tag 3 t/fantastic t/halal\n
            Untag a restaurant
            untag <index> t/TAG
            Example: untag 3 t/fantastic t/halal\n
            Clear all restaurants
            clear\n
            Exit the program
            exit
            """;

    public static final String HELP_MESSAGE =
            USER_COMMANDS + "\nFor more information, refer to the user guide:\n" + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();

        // Open the USERGUIDE_URL in a browser tab
        /*
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(USERGUIDE_URL));
            }
        } catch (Exception e) {
            logger.warning("Failed to open user guide in browser: " + e.getMessage());
        }
        */

    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
