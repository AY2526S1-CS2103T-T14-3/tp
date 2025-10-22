package foodtrail.model.restaurant;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only

        // valid name
        assertTrue(Name.isValidName("KFC")); // only alphabets
        assertTrue(Name.isValidName("McDonald's")); // with apostrophe
        assertTrue(Name.isValidName("KOI Thé")); // accent characters
    }

    @Test
    public void hashCode_test() {
        Name name1 = new Name("KFC");
        Name name2 = new Name("KFC");
        Name differentName = new Name("Jollibee");

        // Equal objects must have equal hash codes
        assertTrue(name1.equals(name2));
        assertEquals(name1.hashCode(), name2.hashCode());

        // Unequal objects should have different hash codes
        assertFalse(name1.equals(differentName));
        assertNotEquals(name1.hashCode(), differentName.hashCode());
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
