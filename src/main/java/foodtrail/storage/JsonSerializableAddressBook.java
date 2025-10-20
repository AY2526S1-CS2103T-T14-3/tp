package foodtrail.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.model.AddressBook;
import foodtrail.model.ReadOnlyAddressBook;
import foodtrail.model.restaurant.Restaurant;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_RESTAURANT = "Restaurant list contains duplicate restaurant(s).";

    private final List<JsonAdaptedRestaurant> restaurants = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given restaurants.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("restaurants") List<JsonAdaptedRestaurant> restaurants) {
        this.restaurants.addAll(restaurants);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        restaurants.addAll(source.getRestaurantList().stream()
                .map(JsonAdaptedRestaurant::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedRestaurant jsonAdaptedRestaurant : restaurants) {
            Restaurant restaurant = jsonAdaptedRestaurant.toModelType();
            if (addressBook.hasRestaurant(restaurant)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_RESTAURANT);
            }
            addressBook.addRestaurant(restaurant);
        }
        return addressBook;
    }

}
