package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalRestaurants.ALICE;
import static seedu.address.testutil.TypicalRestaurants.getTypicalFoodTrail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.exceptions.DuplicateRestaurantException;
import seedu.address.testutil.RestaurantBuilder;

public class FoodTrailTest {

    private final FoodTrail foodTrail = new FoodTrail();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), foodTrail.getRestaurantList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodTrail.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyFoodTrail_replacesData() {
        FoodTrail newData = getTypicalFoodTrail();
        foodTrail.resetData(newData);
        assertEquals(newData, foodTrail);
    }

    @Test
    public void resetData_withDuplicateRestaurants_throwsDuplicateRestaurantException() {
        // Two restaurants with the same identity fields
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Restaurant> newPersons = Arrays.asList(ALICE, editedAlice);
        FoodTrailStub newData = new FoodTrailStub(newPersons);

        assertThrows(DuplicateRestaurantException.class, () -> foodTrail.resetData(newData));
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> foodTrail.hasRestaurant(null));
    }

    @Test
    public void hasRestaurant_restaurantNotInFoodTrail_returnsFalse() {
        assertFalse(foodTrail.hasRestaurant(ALICE));
    }

    @Test
    public void hasRestaurant_restaurantInFoodTrail_returnsTrue() {
        foodTrail.addRestaurant(ALICE);
        assertTrue(foodTrail.hasRestaurant(ALICE));
    }

    @Test
    public void hasRestaurant_restaurantWithSameIdentityFieldsInFoodTrail_returnsTrue() {
        foodTrail.addRestaurant(ALICE);
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(foodTrail.hasRestaurant(editedAlice));
    }

    @Test
    public void getRestaurantList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> foodTrail.getRestaurantList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = FoodTrail.class.getCanonicalName() + "{restaurants=" + foodTrail.getRestaurantList() + "}";
        assertEquals(expected, foodTrail.toString());
    }

    /**
     * A stub ReadOnlyFoodTrail whose restaurants list can violate interface constraints.
     */
    private static class FoodTrailStub implements ReadOnlyFoodTrail {
        private final ObservableList<Restaurant> persons = FXCollections.observableArrayList();

        FoodTrailStub(Collection<Restaurant> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Restaurant> getRestaurantList() {
            return persons;
        }
    }

}
