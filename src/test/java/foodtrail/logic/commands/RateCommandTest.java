package foodtrail.logic.commands;

import static foodtrail.logic.Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;

/**
 * Integration + unit tests for {@link RateCommand}.
 */
public class RateCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void executeUnfilteredListValidIndexSuccess() {
        Restaurant target = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        int newRatingValue = 4;
        Restaurant edited = target.withRating(new Rating(newRatingValue));

        RateCommand cmd = new RateCommand(INDEX_FIRST_RESTAURANT, newRatingValue);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setRestaurant(target, edited);
        expectedModel.updateFilteredRestaurantList(Model.PREDICATE_SHOW_ALL_RESTAURANTS);

        String expectedMsg = String.format(RateCommand.MESSAGE_RATE_SUCCESS, edited.getName(), newRatingValue);
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);

        // sanity check: rating actually set
        Restaurant after = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        assertEquals(Optional.of(new Rating(newRatingValue)), after.getRating());
    }

    @Test
    public void executeUnfilteredListInvalidIndexThrowsCommandException() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        RateCommand cmd = new RateCommand(outOfBounds, 3);
        assertCommandFailure(cmd, model, MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void executeFilteredListValidIndexSuccess() throws CommandException {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT); // filter down to 1 restaurant

        Restaurant target = model.getFilteredRestaurantList().get(0);
        int newRatingValue = 5;
        Restaurant edited = target.withRating(new Rating(newRatingValue));

        RateCommand cmd = new RateCommand(INDEX_FIRST_RESTAURANT, newRatingValue);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT);
        expectedModel.setRestaurant(target, edited);
        expectedModel.updateFilteredRestaurantList(Model.PREDICATE_SHOW_ALL_RESTAURANTS);

        String expectedMsg = String.format(RateCommand.MESSAGE_RATE_SUCCESS, edited.getName(), newRatingValue);
        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    @Test
    public void executeFilteredListInvalidIndexThrowsCommandException() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        // There is only 1 restaurant in filtered list; INDEX_SECOND_RESTAURANT is invalid
        // there.
        RateCommand cmd = new RateCommand(INDEX_SECOND_RESTAURANT, 2);
        assertCommandFailure(cmd, model, MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RateCommand a = new RateCommand(INDEX_FIRST_RESTAURANT, 3);
        RateCommand b = new RateCommand(INDEX_FIRST_RESTAURANT, 3);
        RateCommand c = new RateCommand(INDEX_FIRST_RESTAURANT, 4);
        RateCommand d = new RateCommand(INDEX_SECOND_RESTAURANT, 3);

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
