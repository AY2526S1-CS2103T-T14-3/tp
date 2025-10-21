package foodtrail.model;

import foodtrail.model.restaurant.Restaurant;
import javafx.collections.ObservableList;

/**
 * Unmodifiable view of a restaurant directory
 */
public interface ReadOnlyRestaurantDirectory {

    /**
     * Returns an unmodifiable view of the restaurants list.
     * This list will not contain any duplicate restaurants.
     */
    ObservableList<Restaurant> getRestaurantList();

}
