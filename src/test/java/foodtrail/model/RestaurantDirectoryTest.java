package foodtrail.model;

import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.exceptions.DuplicateRestaurantException;
import foodtrail.testutil.RestaurantBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RestaurantDirectoryTest {

    private final RestaurantDirectory restaurantDirectory = new RestaurantDirectory();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), restaurantDirectory.getRestaurantList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> restaurantDirectory.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyRestaurantDirectory_replacesData() {
        RestaurantDirectory newData = getTypicalRestaurantDirectory();
        restaurantDirectory.resetData(newData);
        assertEquals(newData, restaurantDirectory);
    }

    @Test
    public void resetData_withDuplicateRestaurants_throwsDuplicateRestaurantException() {
        // Two restaurants with the same identity fields (Name, Address, Phone)
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS)
                .withTags(VALID_TAG_FASTFOOD).build();
        List<Restaurant> newRestaurants = Arrays.asList(MCDONALDS, editedMcdonalds);
        RestaurantDirectoryStub newData = new RestaurantDirectoryStub(newRestaurants);

        assertThrows(DuplicateRestaurantException.class, () -> restaurantDirectory.resetData(newData));
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> restaurantDirectory.hasRestaurant(null));
    }

    @Test
    public void hasRestaurant_restaurantNotInRestaurantDirectory_returnsFalse() {
        assertFalse(restaurantDirectory.hasRestaurant(MCDONALDS));
    }

    @Test
    public void hasRestaurant_restaurantInRestaurantDirectory_returnsTrue() {
        restaurantDirectory.addRestaurant(MCDONALDS);
        assertTrue(restaurantDirectory.hasRestaurant(MCDONALDS));
    }

    @Test
    public void hasRestaurant_restaurantWithSameIdentityFieldsInRestaurantDirectory_returnsTrue() {
        restaurantDirectory.addRestaurant(MCDONALDS);
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS)
                .withTags(VALID_TAG_FASTFOOD).build();
        assertTrue(restaurantDirectory.hasRestaurant(editedMcdonalds));
    }

    @Test
    public void getRestaurantList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> restaurantDirectory.getRestaurantList().remove(0));
    }

    @Test
    public void sortByNameAscendingCaseInsensitiveWithTypicalData() {
        RestaurantDirectory directory = getTypicalRestaurantDirectory();

        // Build expected order by sorting a copy (case-insensitive by name)
        Comparator<Restaurant> byNameIgnoreCase = Comparator.comparing(r -> r.getName().fullName.toLowerCase());

        List<Restaurant> before = new ArrayList<>(directory.getRestaurantList());
        List<Restaurant> expected = before.stream()
                .sorted(byNameIgnoreCase)
                .collect(Collectors.toList());

        // When: sort is applied on the directory
        directory.sortRestaurant(byNameIgnoreCase);

        // Then: order matches expected
        assertEquals(expected, directory.getRestaurantList());
    }

    @Test
    public void sort_nullComparator_throwsNullPointerException() {
        RestaurantDirectory directory = getTypicalRestaurantDirectory();
        assertThrows(NullPointerException.class, () -> directory.sortRestaurant(null));
    }

    @Test
    public void sort_preservesAllElements_noLossNoDuplicate() {
        RestaurantDirectory directory = getTypicalRestaurantDirectory();

        List<Restaurant> snapshotBefore = new ArrayList<>(directory.getRestaurantList());
        Comparator<Restaurant> byNameIgnoreCase = Comparator.comparing(r -> r.getName().fullName.toLowerCase());

        directory.sortRestaurant(byNameIgnoreCase);

        List<Restaurant> after = new ArrayList<>(directory.getRestaurantList());
        // same size
        assertEquals(snapshotBefore.size(), after.size());
        // same multiset of elements (order can change, elements must be identical)
        assertTrue(after.containsAll(snapshotBefore) && snapshotBefore.containsAll(after));
    }

    @Test
    public void toStringMethod() {
        String expected = RestaurantDirectory.class.getCanonicalName() + "{restaurants="
                + restaurantDirectory.getRestaurantList() + "}";
        assertEquals(expected, restaurantDirectory.toString());
    }

    /**
     * A stub ReadOnlyRestaurantDirectory whose restaurants list can violate
     * interface constraints.
     */
    private static class RestaurantDirectoryStub implements ReadOnlyRestaurantDirectory {
        private final ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

        RestaurantDirectoryStub(Collection<Restaurant> restaurants) {
            this.restaurants.setAll(restaurants);
        }

        @Override
        public ObservableList<Restaurant> getRestaurantList() {
            return restaurants;
        }
    }

}
