package foodtrail.model.restaurant;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only
        assertFalse(Address.isValidAddress("-")); // one character
        assertFalse(Address.isValidAddress("21 Tampines Rd, #10-81")); // missing postal code
        assertFalse(Address.isValidAddress("50 Sixth Avenue, Singapore 27649")); // postal code not 6 digits
        assertFalse(Address.isValidAddress("50 Sixth Avenue, Singapore 2764962")); // postal code not 6 digits
        assertFalse(Address.isValidAddress("50 Sixth Avenue, Singapore 27649a")); // postal code has non-digits
        assertFalse(Address.isValidAddress("50 Sixth Avenue 50 Sixth Avenue 50 Sixth Avenue 50 Sixth Avenue "
                + "50 Sixth Avenue 50 Sixth Avenue 50 Sixth Avenue, Singapore 276496")); // more than 100 characters
        assertFalse(Address.isValidAddress("  , Singapore 123456")); // empty address part
        assertFalse(Address.isValidAddress("123 Main Street Singapore 123456")); // missing comma before Singapore
        assertFalse(Address.isValidAddress("123 Main Street, Singapore123456")); // missing space after Singapore
        assertFalse(Address.isValidAddress("123 Main St!, Singapore 123456")); // invalid character '!'
        assertFalse(Address.isValidAddress("Baker & Cook, Singapore 123456")); // invalid character '&'
        assertFalse(Address.isValidAddress("1 Raffles Place, #39-01, singapore 048616")); // lowercase singapore

        // valid addresses
        assertTrue(Address.isValidAddress("50 Sixth Avenue, Singapore 276496"));
        assertTrue(Address.isValidAddress(" 2 Orchard Turn, #4-01 ION Orchard, Singapore 238801")); // long address
        assertTrue(Address.isValidAddress("456 Oak Avenue, Singapore 1 2 3 4 5 6")); // postal code with spaces
    }

    @Test
    public void equals() {
        Address address = new Address("459 Clementi Ave 3, #10-401, Singapore 120459");

        // same values -> returns true
        assertTrue(address.equals(new Address("459 Clementi Ave 3, #10-401, Singapore 120459")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(new Object()));

        // different values -> returns false
        assertFalse(address.equals(new Address("180 Kitchener Rd, Singapore 208539")));
    }

    @Test
    public void hashCode_test() {
        Address address1 = new Address("123 Main St, Singapore 123456");
        Address address2 = new Address("123 Main St, Singapore 123456");
        Address differentAddress = new Address("456 Oak Ave, Singapore 654321");

        // Equal objects must have equal hash codes
        assertTrue(address1.equals(address2));
        assertEquals(address1.hashCode(), address2.hashCode());

        // Unequal objects should have different hash codes
        assertFalse(address1.equals(differentAddress));
        assertNotEquals(address1.hashCode(), differentAddress.hashCode());
    }
}
