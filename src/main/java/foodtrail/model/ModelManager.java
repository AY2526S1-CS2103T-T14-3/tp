package foodtrail.model;

import static foodtrail.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import foodtrail.commons.core.GuiSettings;
import foodtrail.commons.core.LogsCenter;
import foodtrail.model.restaurant.Restaurant;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the restaurant directory data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final RestaurantDirectory restaurantDirectory;
    private final UserPrefs userPrefs;
    private final FilteredList<Restaurant> filteredRestaurants;

    /**
     * Initializes a ModelManager with the given restaurantDirectory and userPrefs.
     */
    public ModelManager(ReadOnlyRestaurantDirectory restaurantDirectory, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(restaurantDirectory, userPrefs);

        logger.fine("Initializing with restaurant directory: " + restaurantDirectory
                + " and user prefs " + userPrefs);

        this.restaurantDirectory = new RestaurantDirectory(restaurantDirectory);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredRestaurants = new FilteredList<>(this.restaurantDirectory.getRestaurantList());
    }

    public ModelManager() {
        this(new RestaurantDirectory(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getRestaurantDirectoryFilePath() {
        return userPrefs.getRestaurantDirectoryFilePath();
    }

    @Override
    public void setRestaurantDirectoryFilePath(Path restaurantDirectoryFilePath) {
        requireNonNull(restaurantDirectoryFilePath);
        userPrefs.setRestaurantDirectoryFilePath(restaurantDirectoryFilePath);
    }

    // =========== RestaurantDirectory
    // ================================================================================

    @Override
    public void setRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory) {
        this.restaurantDirectory.resetData(restaurantDirectory);
    }

    @Override
    public ReadOnlyRestaurantDirectory getRestaurantDirectory() {
        return restaurantDirectory;
    }

    @Override
    public boolean hasRestaurant(Restaurant restaurant) {
        requireNonNull(restaurant);
        return restaurantDirectory.hasRestaurant(restaurant);
    }

    @Override
    public void deleteRestaurant(Restaurant target) {
        restaurantDirectory.removeRestaurant(target);
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        restaurantDirectory.addRestaurant(restaurant);
        updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);
    }

    @Override
    public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
        requireAllNonNull(target, editedRestaurant);

        restaurantDirectory.setRestaurant(target, editedRestaurant);
    }

    @Override
    public void sortRestaurantListByName() {
        // Sorts the backing list; FilteredList reflects the new order automatically.
        restaurantDirectory.sortRestaurant(Comparator.comparing(r -> r.getName().fullName.toLowerCase()));
        // Keep current filter predicate as-is so the user’s filtered view remains, just
        // sorted.
        filteredRestaurants.setPredicate(filteredRestaurants.getPredicate());
    }

    // =========== Filtered Restaurant List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Restaurant} backed by the
     * internal list of
     * {@code versionedRestaurantDirectory}
     */
    @Override
    public ObservableList<Restaurant> getFilteredRestaurantList() {
        return filteredRestaurants;
    }

    @Override
    public void updateFilteredRestaurantList(Predicate<Restaurant> predicate) {
        requireNonNull(predicate);
        filteredRestaurants.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return restaurantDirectory.equals(otherModelManager.restaurantDirectory)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredRestaurants.equals(otherModelManager.filteredRestaurants);
    }

}
