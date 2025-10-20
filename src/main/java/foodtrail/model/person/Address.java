package foodtrail.model.person;

import static foodtrail.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            """
            Address should not be empty, not exceed 100 characters,
            and must end with ', Singapore' followed by a 6-digit postal code.
            Preferred format: [Street Address], [Building Name], Singapore [Postal Code].
            Spaces within the postal code will be ignored for validation.
            """;

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        // 1. Check if empty or blank
        if (test.trim().isEmpty()) {
            return false; // "Address cannot be empty"
        }

        // 2. Check if length > 100
        if (test.length() > 100) {
            return false; // "Address cannot exceed 100 characters"
        }

        // 3. Use regex to split address into main part and postal code
        // The postal code must be preceded by a comma, "Singapore", and mandatory whitespace.
        Pattern addressPattern = Pattern.compile("^(.*),\\s+Singapore\\s+((\\d\\s*){6})$", Pattern.CASE_INSENSITIVE);
        Matcher addressMatcher = addressPattern.matcher(test);

        if (!addressMatcher.matches()) {
            return false;
        }

        // We have a match, let's validate the parts.
        String addressPart = addressMatcher.group(1);
        String rawPostalCode = addressMatcher.group(2);

        // Validate address part
        if (addressPart.trim().isEmpty()) {
            return false; // Address part before comma is empty
        }

        if (!addressPart.matches("^[a-zA-Z0-9\\s,#\\'-/.]*$")) {
            return false;
        }

        // Validate postal code part
        String cleanedPostalCode = rawPostalCode.replaceAll("\\s", "");
        if (!cleanedPostalCode.matches("\\d{6}")) {
            // This is a safeguard, the main regex should have caught this.
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
