package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_THIRD_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.IsMarked;
import foodtrail.model.restaurant.Restaurant;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnmarkCommand.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_unfilteredList_success() {
        Restaurant restaurantToUnmark = model.getFilteredRestaurantList().get(INDEX_THIRD_RESTAURANT.getZeroBased());
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_THIRD_RESTAURANT);

        ModelManager expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        Restaurant unmarkedRestaurant = restaurantToUnmark.withMark(new IsMarked(false));
        expectedModel.setRestaurant(restaurantToUnmark, unmarkedRestaurant);
        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_RESTAURANT_SUCCESS, unmarkedRestaurant);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_filteredList_success() {
        showRestaurantAtIndex(model, INDEX_THIRD_RESTAURANT);

        Restaurant restaurantToUnmark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_RESTAURANT);

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        Restaurant unmarkedRestaurant = restaurantToUnmark.withMark(new IsMarked(false));
        expectedModel.setRestaurant(restaurantToUnmark, unmarkedRestaurant);
        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_RESTAURANT_SUCCESS, unmarkedRestaurant);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        UnmarkCommand unmarkFirstCommand = new UnmarkCommand(INDEX_FIRST_RESTAURANT);
        UnmarkCommand unmarkSecondCommand = new UnmarkCommand(INDEX_SECOND_RESTAURANT);

        // same object -> returns true
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommand));

        // same values -> returns true
        UnmarkCommand unmarkFirstCommandCopy = new UnmarkCommand(INDEX_FIRST_RESTAURANT);
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkFirstCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(unmarkFirstCommand.equals(unmarkSecondCommand));
    }
}
