package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Rating;

/**
 * Integration + unit tests for {@link RateCommand}.
 */
public class RateCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void executeUnfilteredListValidIndexSuccess() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int newRatingValue = 4;
        Person edited = target.withRating(new Rating(newRatingValue));

        RateCommand cmd = new RateCommand(INDEX_FIRST_PERSON, newRatingValue);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(target, edited);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        String expectedMsg = String.format(RateCommand.MESSAGE_RATE_SUCCESS, edited.getName(), newRatingValue);
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);

        // sanity check: rating actually set
        Person after = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(Optional.of(new Rating(newRatingValue)), after.getRating());
    }

    @Test
    public void executeUnfilteredListInvalidIndexThrowsCommandException() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RateCommand cmd = new RateCommand(outOfBounds, 3);
        assertCommandFailure(cmd, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeFilteredListValidIndexSuccess() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON); // filter down to 1 person

        Person target = model.getFilteredPersonList().get(0);
        int newRatingValue = 5;
        Person edited = target.withRating(new Rating(newRatingValue));

        RateCommand cmd = new RateCommand(INDEX_FIRST_PERSON, newRatingValue);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(target, edited);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        String expectedMsg = String.format(RateCommand.MESSAGE_RATE_SUCCESS, edited.getName(), newRatingValue);
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    @Test
    public void executeFilteredListInvalidIndexThrowsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // There is only 1 person in filtered list; INDEX_SECOND_PERSON is invalid
        // there.
        RateCommand cmd = new RateCommand(INDEX_SECOND_PERSON, 2);
        assertCommandFailure(cmd, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RateCommand a = new RateCommand(INDEX_FIRST_PERSON, 3);
        RateCommand b = new RateCommand(INDEX_FIRST_PERSON, 3);
        RateCommand c = new RateCommand(INDEX_FIRST_PERSON, 4);
        RateCommand d = new RateCommand(INDEX_SECOND_PERSON, 3);

        assertTrue(a.equals(a)); // same object
        assertTrue(a.equals(b)); // same values
        assertFalse(a.equals(null)); // null
        assertFalse(a.equals(new ClearCommand())); // different type
        assertFalse(a.equals(c)); // different rating
        assertFalse(a.equals(d)); // different index
    }

    @Test
    public void toStringMethod() {
        Index idx = Index.fromOneBased(1);
        int ratingValue = 2;
        RateCommand cmd = new RateCommand(idx, ratingValue);

        String expected = String.format(
                "RateCommand{index=%s, ratingValue=%d}", idx, ratingValue);

        assertEquals(expected, cmd.toString());
    }
}
