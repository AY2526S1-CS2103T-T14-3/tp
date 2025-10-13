package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Rating;

/**
 * Rates a restaurant from 0 to 5 (inclusive).
 */
public class RateCommand extends Command {

    public static final String COMMAND_WORD = "rate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Rates a restaurant from 0 to 5.\n"
            + "Parameters: INDEX (positive integer) RATING (integer 0–5)\n"
            + "Example: " + COMMAND_WORD + " 2 4";

    public static final String MESSAGE_RATE_SUCCESS = "Rated %1$s: %2$d/5";

    private final Index index;
    private final int ratingValue;

    /**
     * @param index of the person in the filtered person list to edit
     * @param ratingValue of the rating to be set
     */
    public RateCommand(Index index, int ratingValue) {
        this.index = index;
        this.ratingValue = ratingValue;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person edited = personToEdit.withRating(new Rating(ratingValue));

        model.setPerson(personToEdit, edited);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_RATE_SUCCESS, edited.getName(), ratingValue));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RateCommand)) {
            return false;
        }
        RateCommand o = (RateCommand) other;
        return index.equals(o.index) && ratingValue == o.ratingValue;
    }
}
