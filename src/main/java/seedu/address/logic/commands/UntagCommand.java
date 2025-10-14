package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Remove a tag of an existing person in the address book.
 */
public class UntagCommand extends Command {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Remove a tag of the person identified "
            + "by the index number used in the displayed person list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "friend";

    public static final String MESSAGE_UNTAG_SUCCESS = "Removed tag %2$s from Person: %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "This tag does not exist for this person.";

    private final Index index;
    private final Tag tag;

    /**
     * @param index of the person in the filtered person list to add the tag to
     * @param tag to be added to the person
     */
    public UntagCommand(Index index, Tag tag) {
        requireNonNull(index);
        requireNonNull(tag);

        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> existingTags = personToEdit.getTags();

        if (!existingTags.contains(tag)) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }

        Set<Tag> newTags = new HashSet<>(existingTags);
        newTags.remove(tag);

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getAddress(), newTags);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNTAG_SUCCESS, Messages.format(editedPerson), tag));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return index.equals(otherUntagCommand.index)
                && tag.equals(otherUntagCommand.tag);
    }
}
