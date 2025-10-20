package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.AddressBook;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {

    private static final String TAG_STUB = "SomeTag";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addTagUnfilteredList_success() {
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Set<Tag> newTags = new HashSet<>(firstRestaurant.getTags());
        newTags.add(new Tag(TAG_STUB));
        Restaurant editedRestaurant = new Restaurant(firstRestaurant.getName(), firstRestaurant.getPhone(),
                firstRestaurant.getAddress(), newTags);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setRestaurant(firstRestaurant, editedRestaurant);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addTagFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantInFilteredList = model.getFilteredRestaurantList()
                .get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Set<Tag> newTags = new HashSet<>(restaurantInFilteredList.getTags());
        newTags.add(new Tag(TAG_STUB));
        Restaurant editedRestaurant = new Restaurant(restaurantInFilteredList.getName(),
                restaurantInFilteredList.getPhone(),
                restaurantInFilteredList.getAddress(), newTags);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setRestaurant(model.getFilteredRestaurantList().get(0), editedRestaurant);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidRestaurantIndexUnfilteredList_failure() {
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidRestaurantIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getRestaurantList().size());

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        final TagCommand standardCommand = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);

        // same values -> returns true
        TagCommand commandWithSameValues = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new TagCommand(INDEX_SECOND_RESTAURANT, newTags)));

        // different tag -> returns false
        Set<Tag> diffTags = new HashSet<>();
        diffTags.add(new Tag("OtherTag"));
        assertFalse(standardCommand.equals(new TagCommand(INDEX_FIRST_RESTAURANT, diffTags)));
    }
}
