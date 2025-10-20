package foodtrail.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static foodtrail.testutil.Assert.assertThrows;

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
        assertFalse(Address.isValidAddress("50 Sixth Avenue, Singapore 2764962")); // postal code not 6 digits

        // valid addresses
        assertTrue(Address.isValidAddress("50 Sixth Avenue, Singapore 276496"));
        assertTrue(Address.isValidAddress(" 2 Orchard Turn, #4-01 ION Orchard, Singapore 238801")); // long address
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
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("180 Kitchener Rd, Singapore 208539")));
    }
}
