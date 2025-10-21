package foodtrail.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import foodtrail.commons.util.ToStringBuilder;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.UniqueRestaurantList;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the restaurant-directory level
 * Duplicates are not allowed (by .isSameRestaurant comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueRestaurantList restaurants;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        restaurants = new UniqueRestaurantList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Restaurants in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
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
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setRestaurants(newData.getRestaurantList());
    }

    //// restaurant-level operations

    /**
     * Returns true if a restaurant with the same identity as {@code restaurant} exists in the address book.
     */
    public boolean hasRestaurant(Restaurant restaurant) {
        requireNonNull(restaurant);
        return restaurants.contains(restaurant);
    }

    /**
     * Adds a restaurant to the address book.
     * The restaurant must not already exist in the address book.
     */
    public void addRestaurant(Restaurant p) {
        restaurants.add(p);
    }

    /**
     * Replaces the given restaurant {@code target} in the list with {@code editedRestaurant}.
     * {@code target} must exist in the address book.
     * The restaurant identity of {@code editedRestaurant} must not be
     * the same as another existing restaurant in the address book.
     */
    public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
        requireNonNull(editedRestaurant);

        restaurants.setRestaurant(target, editedRestaurant);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
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
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return restaurants.equals(otherAddressBook.restaurants);
    }

    @Override
    public int hashCode() {
        return restaurants.hashCode();
    }
}
