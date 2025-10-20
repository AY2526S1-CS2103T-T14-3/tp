package foodtrail.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import foodtrail.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private FlowPane tags;
    @FXML
    private Label rating;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to
     * display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        person.getRating().ifPresentOrElse(r -> {
            rating.setText(getStarString(r.value));
            rating.setVisible(true);
            rating.setManaged(true);
        }, () -> {
            rating.setText("");
            rating.setVisible(false);
            rating.setManaged(false);
        });
    }

    /**
     * Returns a star string representation for a rating value (0–5).
     * Example: 3 -> "★★★☆☆"
     */
    private static String getStarString(int value) {
        int clamped = Math.max(0, Math.min(value, 5)); // safety guard
        String fullStars = "★★★★★";
        String emptyStars = "☆☆☆☆☆";
        return fullStars.substring(0, clamped) + emptyStars.substring(clamped);
    }
}
