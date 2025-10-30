package foodtrail.model.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RatingTest {
    @Test
    public void constructor_invalidRating_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(-1));
        assertThrows(IllegalArgumentException.class, () -> new Rating(6));
    }

    @Test
    public void constructor_validRating_createsRating() {
        // EP: boundary values
        assertEquals(0, new Rating(0).value);
        assertEquals(5, new Rating(5).value);

        // EP: typical values
        assertEquals(3, new Rating(3).value);
    }

    @Test
    public void equals() {
        Rating rating = new Rating(3);

        // same values -> returns true
        assertTrue(rating.equals(new Rating(3)));

        // same object -> returns true
        assertTrue(rating.equals(rating));

        // null -> returns false
        assertFalse(rating.equals(null));

        // different types -> returns false
        assertFalse(rating.equals(new Object()));

        // different values -> returns false
        assertFalse(rating.equals(new Rating(4)));
    }

    @Test
    public void hashCode_test() {
        Rating rating1 = new Rating(3);
        Rating rating2 = new Rating(3);
        Rating differentRating = new Rating(4);

        // Equal objects must have equal hash codes
        assertTrue(rating1.equals(rating2));
        assertEquals(rating1.hashCode(), rating2.hashCode());

        // Unequal objects should have different hash codes
        assertFalse(rating1.equals(differentRating));
        assertNotEquals(rating1.hashCode(), differentRating.hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("3", new Rating(3).toString());
        assertEquals("0", new Rating(0).toString());
        assertEquals("5", new Rating(5).toString());
    }
}
