package foodtrail.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import foodtrail.logic.parser.Prefix;
import foodtrail.model.restaurant.Restaurant;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX = "The restaurant index provided is invalid";
    public static final String MESSAGE_RESTAURANTS_LISTED_OVERVIEW = "%1$d restaurants listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code restaurant} for display to the user.
     */
    public static String format(Restaurant restaurant) {
        final StringBuilder builder = new StringBuilder();
        builder.append(restaurant.getName())
                .append("; Phone: ")
                .append(restaurant.getPhone())
                .append("; Address: ")
                .append(restaurant.getAddress())
                .append("; Tags: ");
        restaurant.getTags().forEach(builder::append);
        return builder.toString();
    }

}
