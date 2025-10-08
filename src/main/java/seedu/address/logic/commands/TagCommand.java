package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Changes the tag of an existing person in the address book.
 */

public class TagCommand extends Command{

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the tag of the restaurant identified "
            + "by the index number used in the last restaurant listing. "
            + "Existing tag will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "Western";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Tag command not implemented yet";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
