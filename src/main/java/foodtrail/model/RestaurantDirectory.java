package foodtrail.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;

import foodtrail.commons.util.ToStringBuilder;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.UniqueRestaurantList;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the restaurant-directory level
 * Duplicates are not allowed (by .isSameRestaurant comparison)
 */
public class RestaurantDirectory implements ReadOnlyRestaurantDirectory {

    private final UniqueRestaurantList restaurants;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication
     * between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other
     * ways to avoid duplication
     * among constructors.
     */
    {
        restaurants = new UniqueRestaurantList();
    }

    public RestaurantDirectory() {
    }

    /**
     * Creates an RestaurantDirectory using the Restaurants in the
     * {@code toBeCopied}
     */
    public RestaurantDirectory(ReadOnlyRestaurantDirectory toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the restaurant directory with {@code restaurants}.
     * {@code restaurants} must not contain duplicate restaurants.
     */
    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants.setRestaurants(restaurants);
    }

    /**
     * Resets the existing data of this {@code RestaurantDirectory} with
     * {@code newData}.
     */
    public void resetData(ReadOnlyRestaurantDirectory newData) {
        requireNonNull(newData);

        setRestaurants(newData.getRestaurantList());
    }

    //// restaurant-level operations

    /**
     * Returns true if a restaurant with the same identity as {@code restaurant}
     * exists in the restaurant directory.
     */
    public boolean hasRestaurant(Restaurant restaurant) {
        requireNonNull(restaurant);
        return restaurants.contains(restaurant);
    }

    /**
     * Adds a restaurant to the restaurant directory.
     * The restaurant must not already exist in the restaurant directory.
     */
    public void addRestaurant(Restaurant p) {
        restaurants.add(p);
    }

    /** Sorts restaurants using the provided comparator. */
    public void sortRestaurant(Comparator<Restaurant> comparator) {
        requireNonNull(comparator);
        restaurants.sort(comparator);
    }

    /**
     * Replaces the given restaurant {@code target} in the list with
     * {@code editedRestaurant}.
     * {@code target} must exist in the restaurant directory.
     * The restaurant identity of {@code editedRestaurant} must not be
     * the same as another existing restaurant in the restaurant directory.
     */
    public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
        requireNonNull(editedRestaurant);

        restaurants.setRestaurant(target, editedRestaurant);
    }

    /**
     * Removes {@code key} from this {@code RestaurantDirectory}.
     * {@code key} must exist in the restaurant directory.
     */
    public void removeRestaurant(Restaurant key) {
        restaurants.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("restaurants", restaurants)
                .toString();
    }

    @Override
    public ObservableList<Restaurant> getRestaurantList() {
        return restaurants.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RestaurantDirectory)) {
            return false;
        }

        RestaurantDirectory otherRestaurantDirectory = (RestaurantDirectory) other;
        return restaurants.equals(otherRestaurantDirectory.restaurants);
    }

    @Override
    public int hashCode() {
        return restaurants.hashCode();
    }
}
