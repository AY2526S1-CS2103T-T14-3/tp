package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.restaurant.Restaurant;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Restaurant> PREDICATE_SHOW_ALL_RESTAURANTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' foodtrail file path.
     */
    Path getFoodTrailFilePath();

    /**
     * Sets the user prefs' foodtrail file path.
     */
    void setFoodTrailFilePath(Path foodTrailFilePath);

    /**
     * Replaces foodtrail data with the data in {@code foodTrail}.
     */
    void setFoodTrail(ReadOnlyFoodTrail foodTrail);

    /** Returns the AddressBook */
    ReadOnlyFoodTrail getFoodTrail();

    /**
     * Returns true if a Restaurant with the same identity as {@code Restaurant} exists in the food trail.
     */
    boolean hasRestaurant(Restaurant restaurant);

    /**
     * Deletes the given Restaurant.
     * The Restaurant must exist in the food trail.
     */
    void deleteRestaurant(Restaurant target);

    /**
     * Adds the given Restaurant.
     * {@code Restaurant} must not already exist in the address book.
     */
    void addRestaurant(Restaurant restaurant);

    /**
     * Replaces the given Restaurant {@code target} with {@code editedRestaurant}.
     * {@code target} must exist in the address book.
     * The Restaurant identity of {@code editedRestaurant} must not be the same as
     *  another existing Restaurant in the address book.
     */
    void setRestaurant(Restaurant target, Restaurant editedRestaurant);

    /** Returns an unmodifiable view of the filtered Restaurant list */
    ObservableList<Restaurant> getFilteredRestaurantList();

    /**
     * Updates the filter of the filtered Restaurant list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredRestaurantList(Predicate<Restaurant> predicate);
}
