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

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Restaurant restaurantToDelete = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_RESTAURANT);

        String restaurantDetails = "\n" + "Name: " + restaurantToDelete.getName() + "\n"
                + "Phone: " + restaurantToDelete.getPhone() + "\n"
                + "Address: " + restaurantToDelete.getAddress() + "\n"
                + "Tags: " + restaurantToDelete.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_RESTAURANT_SUCCESS,
                restaurantDetails);

        ModelManager expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        expectedModel.deleteRestaurant(restaurantToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantToDelete = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_RESTAURANT);

        String restaurantDetails = "\n" + "Name: " + restaurantToDelete.getName() + "\n"
                + "Phone: " + restaurantToDelete.getPhone() + "\n"
                + "Address: " + restaurantToDelete.getAddress() + "\n"
                + "Tags: " + restaurantToDelete.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        expectedModel.deleteRestaurant(restaurantToDelete);
        showNoRestaurant(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_RESTAURANT);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_RESTAURANT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_RESTAURANT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoRestaurant(Model model) {
        model.updateFilteredRestaurantList(p -> false);

        assertTrue(model.getFilteredRestaurantList().isEmpty());
    }
}
