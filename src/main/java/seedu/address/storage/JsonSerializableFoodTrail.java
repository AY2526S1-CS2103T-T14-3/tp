package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.FoodTrail;
import seedu.address.model.ReadOnlyFoodTrail;
import seedu.address.model.restaurant.Restaurant;

/**
 * An Immutable FoodTrail that is serializable to JSON format.
 */
@JsonRootName(value = "foodtrail")
class JsonSerializableFoodTrail {

    public static final String MESSAGE_DUPLICATE_RESTAURANT = "Restaurants list contains duplicate restaurant(s).";

    private final List<JsonAdaptedRestaurant> restaurants = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given restaurants.
     */
    @JsonCreator
    public JsonSerializableFoodTrail(@JsonProperty("restaurants") List<JsonAdaptedRestaurant> restaurants) {
        this.restaurants.addAll(restaurants);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableFoodTrail(ReadOnlyFoodTrail source) {
        restaurants.addAll(source.getRestaurantList().stream()
                .map(JsonAdaptedRestaurant::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code FoodTrail} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public FoodTrail toModelType() throws IllegalValueException {
        FoodTrail foodTrail = new FoodTrail();
        for (JsonAdaptedRestaurant jsonAdaptedFoodTrail : restaurants) {
            Restaurant restaurant = jsonAdaptedFoodTrail.toModelType();
            if (foodTrail.hasRestaurant(restaurant)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_RESTAURANT);
            }
            foodTrail.addRestaurant(restaurant);
        }
        return foodTrail;
    }

}
