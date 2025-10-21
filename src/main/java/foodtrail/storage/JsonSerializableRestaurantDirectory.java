package foodtrail.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.restaurant.Restaurant;

/**
 * An Immutable RestaurantDirectory that is serializable to JSON format.
 */
@JsonRootName(value = "restaurantdirectory")
class JsonSerializableRestaurantDirectory {

    public static final String MESSAGE_DUPLICATE_RESTAURANT = "Restaurant directory contains duplicate restaurant(s).";

    private final List<JsonAdaptedRestaurant> restaurants = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableRestaurantDirectory} with the given restaurants.
     */
    @JsonCreator
    public JsonSerializableRestaurantDirectory(@JsonProperty("restaurants") List<JsonAdaptedRestaurant> restaurants) {
        this.restaurants.addAll(restaurants);
    }

    /**
     * Converts a given {@code ReadOnlyRestaurantDirectory} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableRestaurantDirectory}.
     */
    public JsonSerializableRestaurantDirectory(ReadOnlyRestaurantDirectory source) {
        restaurants.addAll(source.getRestaurantList().stream()
                .map(JsonAdaptedRestaurant::new).collect(Collectors.toList()));
    }

    /**
     * Converts this restaurant directory into the model's {@code RestaurantDirectory} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public RestaurantDirectory toModelType() throws IllegalValueException {
        RestaurantDirectory restaurantDirectory = new RestaurantDirectory();
        for (JsonAdaptedRestaurant jsonAdaptedRestaurant : restaurants) {
            Restaurant restaurant = jsonAdaptedRestaurant.toModelType();
            if (restaurantDirectory.hasRestaurant(restaurant)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_RESTAURANT);
            }
            restaurantDirectory.addRestaurant(restaurant);
        }
        return restaurantDirectory;
    }

}
