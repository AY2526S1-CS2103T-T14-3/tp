package foodtrail.model.restaurant;

import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalPersons.KOI;
import static foodtrail.testutil.TypicalPersons.MCDONALDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import foodtrail.model.restaurant.exceptions.DuplicateRestaurantException;
import foodtrail.model.restaurant.exceptions.RestaurantNotFoundException;
import foodtrail.testutil.PersonBuilder;

public class UniqueRestaurantListTest {

    private final UniqueRestaurantList uniqueRestaurantList = new UniqueRestaurantList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueRestaurantList.contains(MCDONALDS));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueRestaurantList.add(MCDONALDS);
        assertTrue(uniqueRestaurantList.contains(MCDONALDS));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniqueRestaurantList.add(MCDONALDS);
        Restaurant editedMcdonalds = new PersonBuilder(MCDONALDS)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        assertTrue(uniqueRestaurantList.contains(editedMcdonalds));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueRestaurantList.add(MCDONALDS);
        assertThrows(DuplicateRestaurantException.class, () -> uniqueRestaurantList.add(MCDONALDS));
    }

    @Test
    public void setRestaurant_nullTargetRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueRestaurantList.setRestaurant(null, MCDONALDS));
    }

    @Test
    public void setRestaurant_nullEditedRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueRestaurantList.setRestaurant(MCDONALDS, null));
    }

    @Test
    public void setRestaurant_targetRestaurantNotInList_throwsRestaurantNotFoundException() {
        assertThrows(RestaurantNotFoundException.class, () ->
                uniqueRestaurantList.setRestaurant(MCDONALDS, MCDONALDS));
    }

    @Test
    public void setRestaurant_editedRestaurantIsSameRestaurant_success() {
        uniqueRestaurantList.add(MCDONALDS);
        uniqueRestaurantList.setRestaurant(MCDONALDS, MCDONALDS);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(MCDONALDS);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasSameIdentity_success() {
        uniqueRestaurantList.add(MCDONALDS);
        Restaurant editedMcdonalds = new PersonBuilder(MCDONALDS)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        uniqueRestaurantList.setRestaurant(MCDONALDS, editedMcdonalds);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(editedMcdonalds);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasDifferentIdentity_success() {
        uniqueRestaurantList.add(MCDONALDS);
        uniqueRestaurantList.setRestaurant(MCDONALDS, KOI);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(KOI);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurant_editedRestaurantHasNonUniqueIdentity_throwsDuplicateRestaurantException() {
        uniqueRestaurantList.add(MCDONALDS);
        uniqueRestaurantList.add(KOI);
        assertThrows(DuplicateRestaurantException.class, () -> uniqueRestaurantList.setRestaurant(MCDONALDS, KOI));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRestaurantList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(RestaurantNotFoundException.class, () -> uniqueRestaurantList.remove(MCDONALDS));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueRestaurantList.add(MCDONALDS);
        uniqueRestaurantList.remove(MCDONALDS);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueRestaurantList.setRestaurants((UniqueRestaurantList) null));
    }

    @Test
    public void setRestaurants_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniqueRestaurantList.add(MCDONALDS);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(KOI);
        uniqueRestaurantList.setRestaurants(expectedUniqueRestaurantList);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueRestaurantList.setRestaurants((List<Restaurant>) null));
    }

    @Test
    public void setRestaurants_list_replacesOwnListWithProvidedList() {
        uniqueRestaurantList.add(MCDONALDS);
        List<Restaurant> restaurantList = Collections.singletonList(KOI);
        uniqueRestaurantList.setRestaurants(restaurantList);
        UniqueRestaurantList expectedUniqueRestaurantList = new UniqueRestaurantList();
        expectedUniqueRestaurantList.add(KOI);
        assertEquals(expectedUniqueRestaurantList, uniqueRestaurantList);
    }

    @Test
    public void setRestaurants_listWithDuplicateRestaurants_throwsDuplicatePersonException() {
        List<Restaurant> listWithDuplicateRestaurants = Arrays.asList(MCDONALDS, MCDONALDS);
        assertThrows(DuplicateRestaurantException.class, () ->
                uniqueRestaurantList.setRestaurants(listWithDuplicateRestaurants));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                uniqueRestaurantList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueRestaurantList.asUnmodifiableObservableList().toString(), uniqueRestaurantList.toString());
    }
}
