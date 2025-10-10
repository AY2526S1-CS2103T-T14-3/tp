package seedu.address.model.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalRestaurants.ALICE;
import static seedu.address.testutil.TypicalRestaurants.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.RestaurantBuilder;

public class RestaurantTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Restaurant restaurant = new RestaurantBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> restaurant.getTags().remove(0));
    }

    @Test
    public void isSameRestaurant() {
        // same object -> returns true
        assertTrue(ALICE.isSameRestaurant(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameRestaurant(null));

        // same name, all other attributes different -> returns true
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameRestaurant(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new RestaurantBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameRestaurant(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Restaurant editedBob = new RestaurantBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameRestaurant(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new RestaurantBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameRestaurant(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Restaurant aliceCopy = new RestaurantBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different restaurant -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Restaurant editedAlice = new RestaurantBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new RestaurantBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new RestaurantBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new RestaurantBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new RestaurantBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Restaurant.class.getCanonicalName()
                + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                        + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
