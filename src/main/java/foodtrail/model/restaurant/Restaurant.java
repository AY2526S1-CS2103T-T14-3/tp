package foodtrail.model.restaurant;

import static foodtrail.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import foodtrail.commons.util.ToStringBuilder;

/**
 * Represents a Restaurant in the restaurant directory.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Restaurant {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<Rating> rating;

    /**
     * Every field must be present and not null.
     */
    public Restaurant(Name name, Phone phone, Address address, Set<Tag> tags, Optional<Rating> rating) {
        requireAllNonNull(name, phone, address, tags);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.tags.addAll(tags);
        this.rating = rating == null ? Optional.empty() : rating;
    }

    // Backward-compatible 4-arg constructor (no rating provided -> blank)
    public Restaurant(Name name, Phone phone, Address address, Set<Tag> tags) {
        this(name, phone, address, tags, Optional.empty());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Optional<Rating> getRating() {
        return rating;
    }

    /**
     * Returns a new Restaurant with the same details as this restaurant, except with the given rating.
     */
    public Restaurant withRating(Rating newRating) {
        return new Restaurant(this.name, this.phone, this.address, this.tags, Optional.ofNullable(newRating));
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both restaurants have the same name.
     * This defines a weaker notion of equality between two restaurants.
     */
    public boolean isSameRestaurant(Restaurant otherRestaurant) {
        if (otherRestaurant == this) {
            return true;
        }

        return otherRestaurant != null
                && otherRestaurant.getName().equals(getName());
    }

    /**
     * Returns true if both restaurants have the same identity and data fields.
     * This defines a stronger notion of equality between two restaurants.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Restaurant)) {
            return false;
        }

        Restaurant otherRestaurant = (Restaurant) other;
        return name.equals(otherRestaurant.name)
                && phone.equals(otherRestaurant.phone)
                && address.equals(otherRestaurant.address)
                && tags.equals(otherRestaurant.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

}
