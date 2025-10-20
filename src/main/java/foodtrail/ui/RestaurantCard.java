package foodtrail.ui;

import java.util.Comparator;

import foodtrail.model.restaurant.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Restaurant}.
 */
public class RestaurantCard extends UiPart<Region> {

    private static final String FXML = "RestaurantListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Restaurant restaurant;

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
     * Creates a {@code RestaurantCode} with the given {@code Restaurant} and index to
     * display.
     */
    public RestaurantCard(Restaurant restaurant, int displayedIndex) {
        super(FXML);
        this.restaurant = restaurant;
        id.setText(displayedIndex + ". ");
        name.setText(restaurant.getName().fullName);
        phone.setText(restaurant.getPhone().value);
        address.setText(restaurant.getAddress().value);
        restaurant.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        restaurant.getRating().ifPresentOrElse(r -> {
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
