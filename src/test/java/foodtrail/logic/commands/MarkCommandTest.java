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
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MarkCommand}.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Restaurant restaurantToMark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);

        String restaurantDetails = "\n" + "Name: " + restaurantToMark.getName() + "\n"
                + "Phone: " + restaurantToMark.getPhone() + "\n"
                + "Address: " + restaurantToMark.getAddress() + "\n"
                + "Tags: " + restaurantToMark.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_RESTAURANT_SUCCESS,
                restaurantDetails);

        ModelManager expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToMark).withIsMarked(true).build();
        expectedModel.setRestaurant(restaurantToMark, markedRestaurant);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_restaurantAlreadyMarked_throwsCommandException() {
        // Mark the first restaurant directly in the model
        Restaurant restaurantToMark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant alreadyMarkedRestaurant = new RestaurantBuilder(restaurantToMark).withIsMarked(true).build();
        model.setRestaurant(restaurantToMark, alreadyMarkedRestaurant);

        String restaurantDetails = "\n" + "Name: " + alreadyMarkedRestaurant.getName() + "\n"
                + "Phone: " + alreadyMarkedRestaurant.getPhone() + "\n"
                + "Address: " + alreadyMarkedRestaurant.getAddress() + "\n"
                + "Tags: " + alreadyMarkedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);

        assertCommandFailure(markCommand, model, String.format(MarkCommand.MESSAGE_RESTAURANT_ALREADY_MARKED,
                restaurantDetails));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantToMark = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_RESTAURANT);

        String restaurantDetails = "\n" + "Name: " + restaurantToMark.getName() + "\n"
                + "Phone: " + restaurantToMark.getPhone() + "\n"
                + "Address: " + restaurantToMark.getAddress() + "\n"
                + "Tags: " + restaurantToMark.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        Restaurant markedRestaurant = new RestaurantBuilder(restaurantToMark).withIsMarked(true).build();
        expectedModel.setRestaurant(restaurantToMark, markedRestaurant);
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT); // Update filtered list in expected model

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT; // INDEX_SECOND_RESTAURANT is out of bounds
        // for a filtered list with one restaurant
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
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

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkCommand markCommand = new MarkCommand(targetIndex);
        String expected = MarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, markCommand.toString());
    }
}
