package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnmarkCommand}.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        // Mark the restaurant first so it can be unmarked
        Restaurant restaurantToUnmark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToUnmark).withIsMarked(true).build();
        model.setRestaurant(restaurantToUnmark, markedRestaurant);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_RESTAURANT);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_RESTAURANT_SUCCESS,
                Messages.format(markedRestaurant));

        ModelManager expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        Restaurant unmarkedRestaurant = new RestaurantBuilder(markedRestaurant).withIsMarked(false).build();
        expectedModel.setRestaurant(markedRestaurant, unmarkedRestaurant);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_restaurantNotMarked_throwsCommandException() {
        Restaurant restaurantToUnmark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        // Ensure the restaurant is not marked
        Restaurant notMarkedRestaurant = new RestaurantBuilder(restaurantToUnmark).withIsMarked(false).build();
        model.setRestaurant(restaurantToUnmark, notMarkedRestaurant);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_RESTAURANT);

        assertCommandFailure(unmarkCommand, model, String.format(UnmarkCommand.MESSAGE_RESTAURANT_NOT_MARKED,
                Messages.format(notMarkedRestaurant)));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        // Mark the restaurant first so it can be unmarked
        Restaurant restaurantToUnmark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToUnmark).withIsMarked(true).build();
        model.setRestaurant(restaurantToUnmark, markedRestaurant);

        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_RESTAURANT);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_RESTAURANT_SUCCESS,
                Messages.format(markedRestaurant));

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT);
        Restaurant expectedRestaurantToUnmark = expectedModel.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant expectedMarkedRestaurant = new RestaurantBuilder(expectedRestaurantToUnmark).withIsMarked(true).build();
        expectedModel.setRestaurant(expectedRestaurantToUnmark, expectedMarkedRestaurant);

        Restaurant expectedUnmarkedRestaurant = new RestaurantBuilder(expectedMarkedRestaurant).withIsMarked(false).build();
        expectedModel.setRestaurant(expectedMarkedRestaurant, expectedUnmarkedRestaurant);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT; // INDEX_SECOND_RESTAURANT is out of bounds for a filtered list with one restaurant
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

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(targetIndex);
        String expected = UnmarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unmarkCommand.toString());
    }
}
