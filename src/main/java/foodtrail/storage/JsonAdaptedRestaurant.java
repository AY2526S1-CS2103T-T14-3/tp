package foodtrail.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.IsMarked;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Jackson-friendly version of {@link Restaurant}.
 */
class JsonAdaptedRestaurant {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Restaurant's %s field is missing!";

    private final String name;
    private final String phone;
    private final String address;
    private final Integer rating;
    private final boolean isMarked; // Added isMarked field
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedRestaurant} with the given restaurant details.
     */
    @JsonCreator
    public JsonAdaptedRestaurant(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                                 @JsonProperty("address") String address,
                                 @JsonProperty("tags") List<JsonAdaptedTag> tags,
                                 @JsonProperty("rating") Integer rating,
                                 @JsonProperty("isMarked") boolean isMarked) { // Added isMarked to constructor
        this.name = name;
        this.phone = phone;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.rating = rating;
        this.isMarked = isMarked; // Initialize isMarked
    }

    /**
     * Converts a given {@code Restaurant} into this class for Jackson use.
     */
    public JsonAdaptedRestaurant(Restaurant source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        this.rating = source.getRating().map(r -> r.value).orElse(null);
        this.isMarked = source.getIsMarked().isVisited(); // Extract isMarked status
    }

    /**
     * Converts this Jackson-friendly adapted restaurant object into the model's
     * {@code Restaurant} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted restaurant.
     */
    public Restaurant toModelType() throws IllegalValueException {
        final List<Tag> restaurantTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            restaurantTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(restaurantTags);

        Optional<Rating> modelRating = (rating == null) ? java.util.Optional.empty()
                : java.util.Optional.of(new Rating(rating));

        if (rating != null) {
            modelRating = Optional.of(new Rating(rating));
        }
        // Pass the isMarked status to the Restaurant constructor
        return new Restaurant(modelName, modelPhone, modelAddress, modelTags, modelRating, new IsMarked(isMarked));
    }

}
