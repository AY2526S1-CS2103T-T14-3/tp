package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import foodtrail.commons.util.ToStringBuilder;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a restaurant to the restaurant list. "
            + "Parameters: "
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
    public static final String MESSAGE_DUPLICATE_PERSON = "This restaurant already exists in the restaurant list";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
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
