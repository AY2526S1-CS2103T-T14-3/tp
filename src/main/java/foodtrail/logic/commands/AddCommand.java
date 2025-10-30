package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.stream.Collectors;

import foodtrail.commons.util.ToStringBuilder;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Restaurant;

/**
 * Adds a restaurant to the restaurant directory.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a restaurant to the restaurant directory. "
            + "\nParameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "McDonald's "
            + PREFIX_PHONE + "68928572 "
            + PREFIX_ADDRESS + "1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 "
            + PREFIX_TAG + "halal "
            + PREFIX_TAG + "fastfood";

    public static final String MESSAGE_SUCCESS = "New restaurant added: %1$s";
    public static final String MESSAGE_DUPLICATE_RESTAURANT = "This restaurant already exists "
            + "in the restaurant directory";

    private final Restaurant toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Restaurant}
     */
    public AddCommand(Restaurant restaurant) {
        requireNonNull(restaurant);
        toAdd = restaurant;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasRestaurant(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_RESTAURANT);
        }

        model.addRestaurant(toAdd);
        model.sortRestaurantListByName();

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("\nName: ").append(toAdd.getName());
        detailsBuilder.append("\nPhone: ").append(toAdd.getPhone());
        detailsBuilder.append("\nAddress: ").append(toAdd.getAddress());

        if (!toAdd.getTags().isEmpty()) {
            String tagsString = toAdd.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));
            detailsBuilder.append("\nTags: ").append(tagsString);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, detailsBuilder.toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
