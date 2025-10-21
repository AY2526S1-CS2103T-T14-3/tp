package foodtrail.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import foodtrail.commons.core.GuiSettings;
import foodtrail.model.restaurant.Restaurant;
import javafx.collections.ObservableList;

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
     * Returns the user prefs' restaurant directory file path.
     */
    Path getRestaurantDirectoryFilePath();

    /**
     * Sets the user prefs' restaurant directory file path.
     */
    void setRestaurantDirectoryFilePath(Path restaurantDirectoryFilePath);

    /**
     * Replaces restaurant directory data with the data in {@code restaurantDirectory}.
     */
    void setRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory);

    /** Returns the RestaurantDirectory */
    ReadOnlyRestaurantDirectory getRestaurantDirectory();

    /**
     * Returns true if a restaurant with the same identity as {@code restaurant} exists in the restaurant directory.
     */
    boolean hasRestaurant(Restaurant restaurant);

    /**
     * Deletes the given restaurant.
     * The restaurant must exist in the restaurant directory.
     */
    void deleteRestaurant(Restaurant target);

    /**
     * Adds the given restaurant.
     * {@code restaurant} must not already exist in the restaurant directory.
     */
    void addRestaurant(Restaurant restaurant);

    /**
     * Replaces the given restaurant {@code target} with {@code editedRestaurant}.
     * {@code target} must exist in the restaurant directory.
     * The restaurant identity of {@code editedRestaurant} must not be the same
     * as another existing restaurant in the restaurant directory.
     */
    void setRestaurant(Restaurant target, Restaurant editedRestaurant);

    /** Returns an unmodifiable view of the filtered restaurant list */
    ObservableList<Restaurant> getFilteredRestaurantList();

    /**
     * Updates the filter of the filtered restaurant list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredRestaurantList(Predicate<Restaurant> predicate);
}
