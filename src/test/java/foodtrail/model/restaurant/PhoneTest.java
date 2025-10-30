package foodtrail.model.restaurant;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 8 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("78128381")); // not starting with 6,8 or 9

        // valid phone numbers
        assertTrue(Phone.isValidPhone("85627513"));
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("63451179"));
    }

    @Test
    public void hashCode_test() {
        Phone phone1 = new Phone("85627513");
        Phone phone2 = new Phone("85627513");
        Phone differentPhone = new Phone("93121534");

        // Equal objects must have equal hash codes
        assertTrue(phone1.equals(phone2));
        assertEquals(phone1.hashCode(), phone2.hashCode());

        // Unequal objects should have different hash codes
        assertFalse(phone1.equals(differentPhone));
        assertNotEquals(phone1.hashCode(), differentPhone.hashCode());
    }

    @Test
    public void equals() {
        Phone phone = new Phone("85627513");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("85627513")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(new Object()));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("63451179")));
    }
}
