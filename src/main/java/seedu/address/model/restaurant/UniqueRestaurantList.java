package seedu.address.model.restaurant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.restaurant.exceptions.DuplicateRestaurantException;
import seedu.address.model.restaurant.exceptions.RestaurantNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}. As such, adding and updating of
 * persons uses Person#isSamePerson(Person) for equality so as to ensure that the person being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a person uses Person#equals(Object) so
 * as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Restaurant#isSameRestaurant(Restaurant)
 */
public class UniqueRestaurantList implements Iterable<Restaurant> {

    private final ObservableList<Restaurant> internalList = FXCollections.observableArrayList();
    private final ObservableList<Restaurant> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent restaurant as the given argument.
     */
    public boolean contains(Restaurant toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRestaurant);
    }

    /**
     * Adds a person to the list.
     * The restaurant must not already exist in the list.
     */
    public void add(Restaurant toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRestaurantException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.The restaurant identity of {@code editedRestaurant} must not be the same
     * as another existing restaurant in the list.
     */
    public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
        requireAllNonNull(target, editedRestaurant);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RestaurantNotFoundException();
        }

        if (!target.isSameRestaurant(editedRestaurant) && contains(editedRestaurant)) {
            throw new DuplicateRestaurantException();
        }

        internalList.set(index, editedRestaurant);
    }

    /**
     * Removes the equivalent person from the list.
     * The restaurant must exist in the list.
     */
    public void remove(Restaurant toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RestaurantNotFoundException();
        }
    }

    public void setRestaurants(UniqueRestaurantList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setRestaurants(List<Restaurant> restaurants) {
        requireAllNonNull(restaurants);
        if (!restaurantsAreUnique(restaurants)) {
            throw new DuplicateRestaurantException();
        }

        internalList.setAll(restaurants);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Restaurant> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Restaurant> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueRestaurantList)) {
            return false;
        }

        UniqueRestaurantList otherUniqueRestaurantList = (UniqueRestaurantList) other;
        return internalList.equals(otherUniqueRestaurantList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code restaurants} contains only unique restaurants.
     */
    private boolean restaurantsAreUnique(List<Restaurant> restaurants) {
        for (int i = 0; i < restaurants.size() - 1; i++) {
            for (int j = i + 1; j < restaurants.size(); j++) {
                if (restaurants.get(i).isSameRestaurant(restaurants.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
