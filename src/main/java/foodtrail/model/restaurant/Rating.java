package foodtrail.model.restaurant;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Restaurant's rating in the restaurant list.
 * Guarantees: immutable; is valid as declared in {@link #isValidRating(int)}
 */
public class Rating {

    public static final String MESSAGE_CONSTRAINTS =
            "Rating must be an integer between 0 and 5 (inclusive).";

    public final int value;

    /**
     * Constructs a {@code Rating}.
     *
     * @param value A valid rating.
     */
    public Rating(int value) {
        requireNonNull(value);
        if (value < 0 || value > 5) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Rating && ((Rating) other).value == value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
