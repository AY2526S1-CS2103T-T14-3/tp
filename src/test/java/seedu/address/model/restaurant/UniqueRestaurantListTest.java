package seedu.address.model.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalRestaurants.ALICE;
import static seedu.address.testutil.TypicalRestaurants.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.restaurant.exceptions.DuplicateRestaurantException;
import seedu.address.model.restaurant.exceptions.RestaurantNotFoundException;
import seedu.address.testutil.RestaurantBuilder;


public class UniqueRestaurantListTest {

    private final UniqueRestaurantList uniqueRestaurantList = new UniqueRestaurantList();

    @Test
    public void contains_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.contains(null));
    }

    @Test
    public void contains_restaurantNotInList_returnsFalse() {
        assertFalse(uniqueRestaurantList.contains(ALICE));
    }

    @Test
    public void contains_restaurantInList_returnsTrue() {
        uniqueRestaurantList.add(ALICE);
        assertTrue(uniqueRestaurantList.contains(ALICE));
    }

    @Test
    public void contains_restaurantWithSameIdentityFieldsInList_returnsTrue() {
        uniqueRestaurantList.add(ALICE);
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueRestaurantList.contains(editedAlice));
    }

    @Test
    public void add_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.add(null));
    }

    @Test
    public void add_duplicateRestaurant_throwsDuplicateRestaurantException() {
        uniqueRestaurantList.add(ALICE);
        assertThrows(DuplicateRestaurantException.class, () -> uniqueRestaurantList.add(ALICE));
    }

    @Test
    public void setRestaurant_nullTargetRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.setRestaurant(null, ALICE));
    }

    @Test
    public void setRestaurant_nullEditedRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.setRestaurant(ALICE, null));
    }

    @Test
    public void setRestaurant_targetRestaurantNotInList_throwsRestaurantNotFoundException() {
        assertThrows(RestaurantNotFoundException.class, () -> uniqueRestaurantList.setRestaurant(ALICE, ALICE));
    }

    @Test
    public void setRestaurant_editedRestaurantIsSameRestaurant_success() {
        uniqueRestaurantList.add(ALICE);
        uniqueRestaurantList.setRestaurant(ALICE, ALICE);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(ALICE);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasSameIdentity_success() {
        uniqueRestaurantList.add(ALICE);
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueRestaurantList.setRestaurant(ALICE, editedAlice);
        UniqueRestaurantList expectedUniquePersonList = new UniqueRestaurantList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasDifferentIdentity_success() {
        uniqueRestaurantList.add(ALICE);
        uniqueRestaurantList.setRestaurant(ALICE, BOB);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(BOB);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasNonUniqueIdentity_throwsDuplicateRestaurantException() {
        uniqueRestaurantList.add(ALICE);
        uniqueRestaurantList.add(BOB);
        assertThrows(DuplicateRestaurantException.class, () -> uniqueRestaurantList.setRestaurant(ALICE, BOB));
    }

    @Test
    public void remove_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.remove(null));
    }

    @Test
    public void remove_restaurantDoesNotExist_throwsRestaurantNotFoundException() {
        assertThrows(RestaurantNotFoundException.class, () -> uniqueRestaurantList.remove(ALICE));
    }

    @Test
    public void remove_existingRestaurant_removesRestaurant() {
        uniqueRestaurantList.add(ALICE);
        uniqueRestaurantList.remove(ALICE);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_nullUniqueRestaurantList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList
                .setRestaurants((UniqueRestaurantList) null));
    }

    @Test
    public void setRestaurants_uniqueRestaurantList_replacesOwnListWithProvidedUniqueRestaurantList() {
        uniqueRestaurantList.add(ALICE);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(BOB);
        uniqueRestaurantList.setRestaurants(expectedUniqueRestaurantList);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList
                .setRestaurants((List<Restaurant>) null));
    }

    @Test
    public void setRestaurants_list_replacesOwnListWithProvidedList() {
        uniqueRestaurantList.add(ALICE);
        List<Restaurant> restaurantList = Collections.singletonList(BOB);
        uniqueRestaurantList.setRestaurants(restaurantList);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(BOB);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_listWithDuplicateRestaurants_throwsDuplicateRestaurantException() {
        List<Restaurant> listWithDuplicateRestaurants = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateRestaurantException.class, () -> uniqueRestaurantList
                .setRestaurants(listWithDuplicateRestaurants));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueRestaurantList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueRestaurantList.asUnmodifiableObservableList().toString(), uniqueRestaurantList.toString());
    }
}
