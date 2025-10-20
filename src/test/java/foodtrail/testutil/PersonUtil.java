package foodtrail.testutil;

import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;

import foodtrail.logic.commands.AddCommand;
import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.model.restaurant.Restaurant;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Restaurant restaurant) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(restaurant);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Restaurant restaurant) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + restaurant.getName().fullName + " ");
        sb.append(PREFIX_PHONE + restaurant.getPhone().value + " ");
        sb.append(PREFIX_ADDRESS + restaurant.getAddress().value + " ");
        restaurant.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditRestaurantDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        return sb.toString();
    }
}
