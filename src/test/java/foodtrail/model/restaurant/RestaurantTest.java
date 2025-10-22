package foodtrail.model.restaurant;

import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_HALAL;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.KOI;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodtrail.testutil.RestaurantBuilder;

public class RestaurantTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Restaurant restaurant = new RestaurantBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> restaurant.getTags().remove(0));
    }

    @Test
    public void isSameRestaurant() {
        // same object -> returns true
        assertTrue(MCDONALDS.isSameRestaurant(MCDONALDS));

        // null -> returns false
        assertFalse(MCDONALDS.isSameRestaurant(null));

        // same name, all other attributes different -> returns true
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS).withPhone(VALID_PHONE_KFC)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        assertTrue(MCDONALDS.isSameRestaurant(editedMcdonalds));

        // different name, all other attributes same -> returns false
        editedMcdonalds = new RestaurantBuilder(MCDONALDS).withName(VALID_NAME_KFC).build();
        assertFalse(MCDONALDS.isSameRestaurant(editedMcdonalds));

        // name differs in case, all other attributes same -> returns false
        Restaurant editedKoi = new RestaurantBuilder(KOI).withName(VALID_NAME_KFC.toLowerCase()).build();
        assertFalse(KOI.isSameRestaurant(editedKoi));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_KFC + " ";
        editedKoi = new RestaurantBuilder(KOI).withName(nameWithTrailingSpaces).build();
        assertFalse(KOI.isSameRestaurant(editedKoi));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Restaurant mcdonaldsCopy = new RestaurantBuilder(MCDONALDS).build();
        assertTrue(MCDONALDS.equals(mcdonaldsCopy));

        // same object -> returns true
        assertTrue(MCDONALDS.equals(MCDONALDS));

        // null -> returns false
        assertFalse(MCDONALDS.equals(null));

        // different type -> returns false
        assertFalse(MCDONALDS.equals(5));

        // different restaurant -> returns false
        assertFalse(MCDONALDS.equals(KOI));

        // different name -> returns false
        Restaurant editedMcdonalds = new RestaurantBuilder(MCDONALDS).withName(VALID_NAME_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different phone -> returns false
        editedMcdonalds = new RestaurantBuilder(MCDONALDS).withPhone(VALID_PHONE_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different address -> returns false
        editedMcdonalds = new RestaurantBuilder(MCDONALDS).withAddress(VALID_ADDRESS_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different tags -> returns false
        editedMcdonalds = new RestaurantBuilder(MCDONALDS).withTags(VALID_TAG_HALAL).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));
    }

    @Test
    public void toStringMethod() {
        String expected = Restaurant.class.getCanonicalName() + "{" // Start with class name and opening brace
                + "name=" + MCDONALDS.getName()
                + ", phone=" + MCDONALDS.getPhone()
                + ", address=" + MCDONALDS.getAddress()
                + ", tags=" + MCDONALDS.getTags()
                + ", rating=" + MCDONALDS.getRating().orElse(null) // Include rating, which will be null for MCDONALDS
                + ", isMarked=" + MCDONALDS.getIsMarked() // Include isMarked, which will be [   ] for MCDONALDS
                + "}"; // Closing brace
        assertEquals(expected, MCDONALDS.toString());
    }
}
