package seedu.address.model.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import foodtrail.model.restaurant.Rating;

public class RatingTest {

    @Test
    public void constructor_validRange_success() {
        assertEquals(0, new Rating(0).value);
        assertEquals(3, new Rating(3).value);
        assertEquals(5, new Rating(5).value);
    }

    @Test
    public void constructor_outOfRange_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(-1));
        assertThrows(IllegalArgumentException.class, () -> new Rating(6));
    }

    @Test
    public void toString_returnsNumericString() {
        assertEquals("2", new Rating(2).toString());
    }
}
