package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.restaurant.Restaurant;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FoodTrail foodTrail;
    private final UserPrefs userPrefs;
    private final FilteredList<Restaurant> filteredRestaurants;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyFoodTrail foodTrail, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(foodTrail, userPrefs);

        logger.fine("Initializing with food trail: " + foodTrail + " and user prefs " + userPrefs);

        this.foodTrail = new FoodTrail(foodTrail);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredRestaurants = new FilteredList<>(this.foodTrail.getRestaurantList());
    }

    public ModelManager() {
        this(new FoodTrail(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

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
    public Path getFoodTrailFilePath() {
        return userPrefs.getFoodTrailFilePath();
    }

    @Override
    public void setFoodTrailFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setFoodTrailFilePath(addressBookFilePath);
    }

    //=========== FoodTrail ================================================================================

    @Override
    public void setFoodTrail(ReadOnlyFoodTrail addressBook) {
        this.foodTrail.resetData(addressBook);
    }

    @Override
    public ReadOnlyFoodTrail getFoodTrail() {
        return foodTrail;
    }

    @Override
    public boolean hasRestaurant(Restaurant restaurant) {
        requireNonNull(restaurant);
        return foodTrail.hasRestaurant(restaurant);
    }

    @Override
    public void deleteRestaurant(Restaurant target) {
        foodTrail.removeRestaurant(target);
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        foodTrail.addRestaurant(restaurant);
        updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);
    }

    @Override
    public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
        requireAllNonNull(target, editedRestaurant);

        foodTrail.setRestaurant(target, editedRestaurant);
    }

    //=========== Filtered restaurant List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code restaurant} backed by the internal list of
     * {@code versionedAddressBook}
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
        return foodTrail.equals(otherModelManager.foodTrail)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredRestaurants.equals(otherModelManager.filteredRestaurants);
    }

}
