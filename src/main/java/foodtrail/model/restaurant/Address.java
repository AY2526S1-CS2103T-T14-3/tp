package foodtrail.model.restaurant;

import static foodtrail.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Restaurant's address in the restaurant directory.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            """
            Invalid address format. Please adhere to the following rules:
            1. The address must not be empty.
            2. The address must be 1 to 100 characters long.
            3. The address part (before ", Singapore") can only contain letters, numbers, and these special characters:
            # (hash), ' (apostrophe), / (forward slash), . (full stop), + (plus sign), - (hyphen), , (comma).
            4. The address must end with ', Singapore ' then a 6-digit postal code with no spaces.
            Example: 123 Clementi Ave 3, #01-01, Singapore 120123
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
        Pattern addressPattern = Pattern.compile("^(.*),\\s+Singapore\\s((\\d){6})$");
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

        if (!addressPart.matches("^[a-zA-Z0-9\\s,#'/.+-]*$")) {
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
