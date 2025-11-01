package foodtrail.ui;

import java.util.logging.Logger;

import foodtrail.commons.core.LogsCenter;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-t14-3.github.io/tp/UserGuide.html";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private ScrollPane scrollPane;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);

        VBox container = new VBox();
        container.setSpacing(10);
        container.setPadding(new Insets(10));

        Label commandList = new Label("Commands List:");
        commandList.getStyleClass().add("h1");
        commandList.setStyle("-fx-text-fill: #5C4033");
        container.getChildren().add(commandList);

        VBox addBox = createCommandBox(
                "Add a restaurant",
                "add n/NAME a/ADDRESS hp/PHONE_NUMBER [t/TAG]",
                "Example: add n/McDonald's a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 hp/68928572",
                "Example: add n/KFC a/701A Yishun Ave 5, #01-02, Singapore 761701 hp/62226111 t/fastfood t/chicken"
        );
        container.getChildren().add(addBox);

        VBox listBox = createCommandBox(
                "List all restaurants",
                "list"
        );
        container.getChildren().add(listBox);

        VBox markBox = createCommandBox(
                "Mark a restaurant as visited",
                "mark INDEX",
                "Example: mark 3"
        );
        container.getChildren().add(markBox);

        VBox unmarkBox = createCommandBox(
                "Unmark a restaurant",
                "unmark INDEX",
                "Example: unmark 3"
        );
        container.getChildren().add(unmarkBox);

        VBox deleteBox = createCommandBox(
                "Delete a restaurant",
                "delete INDEX",
                "Example: delete 3"
        );
        container.getChildren().add(deleteBox);

        VBox editBox = createCommandBox(
                "Edit a restaurant",
                "edit INDEX [n/NAME] [a/ADDRESS] [hp/PHONE_NUMBER]",
                "Example: edit 1 n/Subway hp/66591189",
                "Example: edit 2 a/701A Yishun Ave 5, #01-02, Singapore 761701"
        );
        container.getChildren().add(editBox);

        VBox rateBox = createCommandBox(
                "Rate a restaurant from 0 to 5 stars",
                "rate INDEX r/RATING",
                "Example: rate 1 r/5"
        );
        container.getChildren().add(rateBox);

        VBox unrateBox = createCommandBox(
                "Remove a restaurant's rating",
                "unrate INDEX",
                "Example: unrate 1"
        );
        container.getChildren().add(unrateBox);

        VBox findBox = createCommandBox(
                "Find a restaurant",
                "find KEYWORD[, MORE_KEYWORD]",
                "Example: find mcdonald",
                "Example: find halal, fast food"
        );
        container.getChildren().add(findBox);

        VBox tagBox = createCommandBox(
                "Tag a restaurant",
                "tag INDEX t/TAG",
                "Example: tag 3 t/fast food t/halal"
        );
        container.getChildren().add(tagBox);

        VBox untagBox = createCommandBox(
                "Untag a restaurant",
                "untag INDEX t/TAG",
                "Example: untag 3 t/fast food t/halal"
        );
        container.getChildren().add(untagBox);

        VBox clearBox = createCommandBox(
                "Clear all restaurants",
                "clear"
        );
        container.getChildren().add(clearBox);

        VBox exitBox = createCommandBox(
                "Exit the program",
                "exit"
        );
        container.getChildren().add(exitBox);

        Label userGuide = new Label("For more information, refer to the user guide. " + USERGUIDE_URL);
        userGuide.getStyleClass().add("h3");
        userGuide.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-text-fill: #5C4033");
        container.getChildren().add(userGuide);

        scrollPane.setContent(container);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    private VBox createCommandBox(String title, String... lines) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.setStyle("-fx-border-color: grey; -fx-border-width: 1; -fx-border-radius: 5; "
                + "-fx-padding: 10; -fx-background-color: white");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h2");
        titleLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: #5C4033;");
        box.getChildren().add(titleLabel);

        if (lines.length > 0) {
            Label syntaxLabel = new Label(lines[0]);
            syntaxLabel.getStyleClass().add("h3");
            syntaxLabel.setStyle("-fx-text-fill: blue; -fx-background-color: transparent; -fx-font-family: Consolas");
            syntaxLabel.setWrapText(true);
            box.getChildren().add(syntaxLabel);
        }

        if (lines.length > 1) {
            VBox exampleContainer = new VBox();
            exampleContainer.setPadding(new Insets(10));
            exampleContainer.setSpacing(5);
            exampleContainer.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
            for (int i = 1; i < lines.length; i++) {
                Label exampleLabel = new Label(lines[i]);
                exampleLabel.getStyleClass().add("h3");
                exampleLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: #5C4033; "
                        + "-fx-font-family: Consolas");
                exampleLabel.setWrapText(true);
                exampleContainer.getChildren().add(exampleLabel);
            }
            box.getChildren().add(exampleContainer);
        }
        return box;
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException <ul>
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
