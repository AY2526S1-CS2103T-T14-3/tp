package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    @Test
    public void execute_unfilteredList_success() {
        Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

        Restaurant restaurantToMark = expectedModel.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToMark).withMarked(true).build();
        expectedModel.setRestaurant(restaurantToMark, markedRestaurant);
        expectedModel.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);
        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_RESTAURANT_SUCCESS, markedRestaurant);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);
        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_filteredList_success() {
        Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Model expectedModel = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        Restaurant restaurantToMark = expectedModel.getFilteredRestaurantList()
                .get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToMark).withMarked(true).build();
        expectedModel.setRestaurant(restaurantToMark, markedRestaurant);
        expectedModel.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);
        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_RESTAURANT_SUCCESS, markedRestaurant);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);
        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_RESTAURANT);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(INDEX_FIRST_RESTAURANT);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));
    }
}
