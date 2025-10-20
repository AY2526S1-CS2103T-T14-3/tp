package foodtrail.storage;

import static foodtrail.storage.JsonAdaptedRestaurant.MISSING_FIELD_MESSAGE_FORMAT;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.KOI;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Rating;

public class JsonAdaptedRestaurantTest {
    private static final String INVALID_NAME = "";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = KOI.getName().toString();
    private static final String VALID_PHONE = KOI.getPhone().toString();
    private static final String VALID_ADDRESS = KOI.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = KOI.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final Integer VALID_RATING = 4;

    @Test
    public void toModelType_validRestaurantDetails_returnsRestaurant() throws Exception {
        JsonAdaptedRestaurant restaurant = new JsonAdaptedRestaurant(KOI);
        assertEquals(KOI, restaurant.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(INVALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(null, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, INVALID_PHONE, VALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, null, VALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, INVALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, null, VALID_TAGS, /*rating*/ null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, restaurant::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, VALID_ADDRESS, invalidTags, /*rating*/ null);
        assertThrows(IllegalValueException.class, restaurant::toModelType);
    }

    @Test
    public void toModelType_validRating_preserved() throws Exception {
        // KOI in TypicalRestaurants has a rating (4) in your sample data; build from it
        JsonAdaptedRestaurant restaurant = new JsonAdaptedRestaurant(KOI);
        assertEquals(KOI, restaurant.toModelType());
    }

    @Test
    public void toModelType_invalidRatingNegative_throwsIllegalArgumentException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, -1);
        assertThrows(IllegalArgumentException.class, Rating.MESSAGE_CONSTRAINTS, restaurant::toModelType);
    }

    @Test
    public void toModelType_invalidRatingTooLarge_throwsIllegalArgumentException() {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, 10);
        assertThrows(IllegalArgumentException.class, Rating.MESSAGE_CONSTRAINTS, restaurant::toModelType);
    }

    @Test
    public void toModelType_nullRating_okayMeansEmptyOptional() throws Exception {
        JsonAdaptedRestaurant restaurant =
                new JsonAdaptedRestaurant(VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, /*rating*/ null);
        // Should parse successfully with empty Optional<Rating>
        restaurant.toModelType();
    }

}
