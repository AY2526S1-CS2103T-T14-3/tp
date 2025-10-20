package foodtrail.testutil;

import java.util.HashSet;
import java.util.Set;

import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;
import foodtrail.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Pizza Hut";
    public static final String DEFAULT_PHONE = "62353535";
    public static final String DEFAULT_ADDRESS = "18 Yishun Ave 9, #01-82 Junction 9, Singapore 768897";

    private Name name;
    private Phone phone;
    private Address address;
    private Set<Tag> tags;
    private Rating rating;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        rating = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Restaurant restaurantToCopy) {
        name = restaurantToCopy.getName();
        phone = restaurantToCopy.getPhone();
        address = restaurantToCopy.getAddress();
        tags = new HashSet<>(restaurantToCopy.getTags());
        rating = restaurantToCopy.getRating().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code Person} that we are building.
     */
    public PersonBuilder withRating(int value) {
        this.rating = new Rating(value);
        return this;
    }

    public Restaurant build() {
        return new Restaurant(name, phone, address, tags);
    }

}
