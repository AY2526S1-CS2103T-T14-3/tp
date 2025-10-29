package foodtrail.ui;

import java.util.logging.Logger;

import foodtrail.commons.core.LogsCenter;
import javafx.fxml.FXML;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Controller for a help page
 */

public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t14-3.github.io/tp/UserGuide.html";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private TextFlow textFlow;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        Text commandList = new Text("Commands List:\n");
        commandList.getStyleClass().add("h1");

        Text addRest = new Text("Add a restaurant\n");
        addRest.getStyleClass().add("h2");
        Text addCommand = new Text("add n/NAME a/ADDRESS hp/PHONE_NUMBER [t/TAG]\n");
        addCommand.getStyleClass().add("h3");
        Text exampleAdd = new Text("Example: add n/McDonald's a/1 Jelebu Road, #02-01,"
                + " Bukit Panjang Plaza, Singapore 677743 hp/68928572\n");
        exampleAdd.getStyleClass().add("h3");
        Text exampleAdd2 = new Text("Example: add n/KFC a/701A Yishun Ave 5, #01-02,"
                + " Singapore 761701 hp/62226111 t/fastfood t/chicken\n\n");
        exampleAdd2.getStyleClass().add("h3");

        Text listRest = new Text("List all restaurants\n");
        listRest.getStyleClass().add("h2");
        Text listCommand = new Text("list\n\n");
        listCommand.getStyleClass().add("h3");

        Text markRest = new Text("Mark a restaurant as visited\n");
        markRest.getStyleClass().add("h2");
        Text markCommand = new Text("mark <index>\n");
        markCommand.getStyleClass().add("h3");
        Text exampleMark = new Text("Example: mark 3\n\n");
        exampleMark.getStyleClass().add("h3");

        Text unmarkRest = new Text("Unmark a restaurant\n");
        unmarkRest.getStyleClass().add("h2");
        Text unmarkCommand = new Text("unmark <index>\n");
        unmarkCommand.getStyleClass().add("h3");
        Text exampleUnmark = new Text("Example: unmark 3\n\n");
        exampleUnmark.getStyleClass().add("h3");

        Text deleteRest = new Text("Delete a restaurant\n");
        deleteRest.getStyleClass().add("h2");
        Text deleteCommand = new Text("delete <index>\n");
        deleteCommand.getStyleClass().add("h3");
        Text exampleDelete = new Text("Example: delete 3\n\n");
        exampleDelete.getStyleClass().add("h3");

        Text editRest = new Text("Edit a restaurant\n");
        editRest.getStyleClass().add("h2");
        Text editCommand = new Text("edit <index> n/NAME a/ADDRESS hp/PHONE_NUMBER\n");
        editCommand.getStyleClass().add("h3");
        Text exampleEdit = new Text("Example: edit 1 n/Subway hp/66591189\n");
        exampleEdit.getStyleClass().add("h3");
        Text exampleEdit2 = new Text("Example: edit 2 a/701A Yishun Ave 5, #01-02, Singapore 761701\n\n");
        exampleEdit2.getStyleClass().add("h3");

        Text rateRest = new Text("Rate a restaurant from 0 to 5 stars\n");
        rateRest.getStyleClass().add("h2");
        Text rateCommand = new Text("rate <index> <rating>\n");
        rateCommand.getStyleClass().add("h3");
        Text exampleRate = new Text("Example: rate 1 5\n\n");
        exampleRate.getStyleClass().add("h3");

        Text unrateRest = new Text("Remove a restaurant's rating\n");
        unrateRest.getStyleClass().add("h2");
        Text unrateCommand = new Text("unrate <index>\n");
        unrateCommand.getStyleClass().add("h3");
        Text exampleUnrate = new Text("Example: unrate 1\n\n");
        exampleUnrate.getStyleClass().add("h3");

        Text findRest = new Text("Find a restaurant\n");
        findRest.getStyleClass().add("h2");
        Text findCommand = new Text("find <keyword>\n");
        findCommand.getStyleClass().add("h3");
        Text exampleFind = new Text("Example: find mcdonald\n");
        exampleFind.getStyleClass().add("h3");
        Text exampleFind2 = new Text("Example: find halal, fast food\n\n");
        exampleFind2.getStyleClass().add("h3");

        Text tagRest = new Text("Tag a restaurant\n");
        tagRest.getStyleClass().add("h2");
        Text tagCommand = new Text("tag <index> t/TAG\n");
        tagCommand.getStyleClass().add("h3");
        Text exampleTag = new Text("Example: tag 3 t/fast food t/halal\n\n");
        exampleTag.getStyleClass().add("h3");

        Text untagRest = new Text("Untag a restaurant\n");
        untagRest.getStyleClass().add("h2");
        Text untagCommand = new Text("untag <index> t/TAG\n");
        untagCommand.getStyleClass().add("h3");
        Text exampleUntag = new Text("Example: untag 3 t/fast food t/halal\n\n");
        exampleUntag.getStyleClass().add("h3");

        Text clearRest = new Text("Clear all restaurants\n");
        clearRest.getStyleClass().add("h2");
        Text clearCommand = new Text("clear\n\n");
        clearCommand.getStyleClass().add("h3");

        Text exitRest = new Text("Exit the program\n");
        exitRest.getStyleClass().add("h2");
        Text exitCommand = new Text("exit\n\n");
        exitCommand.getStyleClass().add("h3");

        Text userGuide = new Text("For more information, refer to the user guide. " + USERGUIDE_URL + "\n");
        userGuide.getStyleClass().add("h3");

        textFlow.getChildren().setAll(
                commandList, addRest, addCommand, exampleAdd, exampleAdd2, listRest, listCommand,
                markRest, markCommand, exampleMark, unmarkRest, unmarkCommand, exampleUnmark,
                deleteRest, deleteCommand, exampleDelete, editRest, editCommand, exampleEdit,
                exampleEdit2, rateRest, rateCommand, exampleRate, unrateRest, unrateCommand, exampleUnrate,
                findRest, findCommand, exampleFind, exampleFind2, tagRest, tagCommand, exampleTag,
                untagRest, untagCommand, exampleUntag, clearRest, clearCommand, exitRest, exitCommand, userGuide);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *                               <ul>
     *                               <li>
     *                               if this method is called on a thread other than
     *                               the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or
     *                               layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();

        // Open the USERGUIDE_URL in a browser tab
        /*
         * try {
         * if (Desktop.isDesktopSupported()) {
         * Desktop.getDesktop().browse(new URI(USERGUIDE_URL));
         * }
         * } catch (Exception e) {
         * logger.warning("Failed to open user guide in browser: " + e.getMessage());
         * }
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
