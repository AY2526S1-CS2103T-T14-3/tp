package foodtrail.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import foodtrail.model.ReadOnlyAddressBook;
import foodtrail.model.restaurant.Restaurant;

/**
 * Ensures that every Restaurant created in SampleDataUtil#getSampleRestaurants() is valid and covered.
 */
public class SampleDataUtilCoverageTest {

    @Test
    public void getSampleRestaurants_allEntriesPresentAndValid() {
        Restaurant[] sample = SampleDataUtil.getSampleRestaurants();
        assertNotNull(sample);
        assertTrue(sample.length >= 6, "Expected at least 6 sample restaurants");

        boolean hasRating = false;
        boolean hasNoRating = false;

        for (Restaurant p : sample) {
            assertNotNull(p.getName(), "Restaurant name should not be null");
            assertNotNull(p.getPhone(), "Restaurant phone should not be null");
            assertNotNull(p.getAddress(), "Restaurant address should not be null");
            assertNotNull(p.getTags(), "Restaurant tags should not be null");

            if (p.getRating().isPresent()) {
                hasRating = true;
                int value = p.getRating().get().value;
                // range check instead of Rating.isValidRating()
                assertTrue(value >= 0 && value <= 5,
                        "Rating value out of range for " + p.getName() + ": " + value);
            } else {
                hasNoRating = true;
            }
        }

        // ensure both rated and unrated restaurants exist
        assertTrue(hasRating, "At least one sample restaurant should have a rating");
        assertTrue(hasNoRating, "At least one sample restaurant should have no rating");
    }

    @Test
    public void getSampleAddressBook_containsAllSampleRestaurants() {
        ReadOnlyAddressBook ab = SampleDataUtil.getSampleAddressBook(); // ✅ fix type
        List<Restaurant> list = ab.getRestaurantList();
        Restaurant[] sample = SampleDataUtil.getSampleRestaurants();

        assertEquals(sample.length, list.size(),
                "AddressBook should contain all sample restaurants");

        for (Restaurant p : sample) {
            assertTrue(list.stream().anyMatch(x -> x.isSameRestaurant(p)),
                    "Sample restaurant missing: " + p.getName());
        }
    }
}
