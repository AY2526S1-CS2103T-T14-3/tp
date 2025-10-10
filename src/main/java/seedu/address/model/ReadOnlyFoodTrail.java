package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.restaurant.Restaurant;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyFoodTrail {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Restaurant> getRestaurantList();

}
