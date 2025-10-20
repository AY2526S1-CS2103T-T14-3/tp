package foodtrail.model;

import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static foodtrail.testutil.TypicalRestaurants.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.exceptions.DuplicateRestaurantException;
import foodtrail.testutil.RestaurantBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getRestaurantList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateRestaurants_throwsDuplicateRestaurantException() {
        // Two restaurants with the same identity fields
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        List<Restaurant> newRestaurants = Arrays.asList(MCDONALDS, editedMcdonalds);
        AddressBookStub newData = new AddressBookStub(newRestaurants);

        assertThrows(DuplicateRestaurantException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasRestaurant(null));
    }

    @Test
    public void hasRestaurant_restaurantNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasRestaurant(MCDONALDS));
    }

    @Test
    public void hasRestaurant_restaurantInAddressBook_returnsTrue() {
        addressBook.addRestaurant(MCDONALDS);
        assertTrue(addressBook.hasRestaurant(MCDONALDS));
    }

    @Test
    public void hasRestaurant_restaurantWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addRestaurant(MCDONALDS);
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        assertTrue(addressBook.hasRestaurant(editedMcdonalds));
    }

    @Test
    public void getRestaurantList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getRestaurantList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{restaurants="
                + addressBook.getRestaurantList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose restaurants list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

        AddressBookStub(Collection<Restaurant> restaurants) {
            this.restaurants.setAll(restaurants);
        }

        @Override
        public ObservableList<Restaurant> getRestaurantList() {
            return restaurants;
        }
    }

}
