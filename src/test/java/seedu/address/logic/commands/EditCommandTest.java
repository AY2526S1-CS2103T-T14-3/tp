package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static seedu.address.testutil.TypicalRestaurants.getTypicalFoodTrail;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditRestaurantDescriptor;
import seedu.address.model.FoodTrail;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.testutil.EditRestaurantDescriptionBuilder;
import seedu.address.testutil.RestaurantBuilder;


/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalFoodTrail(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Restaurant editedRestaurant = new RestaurantBuilder().build();
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptionBuilder(editedRestaurant).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new FoodTrail(model.getFoodTrail()), new UserPrefs());
        expectedModel.setRestaurant(model.getFilteredRestaurantList().get(0), editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastRestaurant = Index.fromOneBased(model.getFilteredRestaurantList().size());
        Restaurant lastRestaurant = model.getFilteredRestaurantList().get(indexLastRestaurant.getZeroBased());

        RestaurantBuilder restaurantInList = new RestaurantBuilder(lastRestaurant);
        Restaurant editedRestaurant = restaurantInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptionBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastRestaurant, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new FoodTrail(model.getFoodTrail()), new UserPrefs());
        expectedModel.setRestaurant(lastRestaurant, editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT, new EditRestaurantDescriptor());
        Restaurant editedRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new FoodTrail(model.getFoodTrail()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantInFilteredList = model.getFilteredRestaurantList()
                .get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant editedRestaurant = new RestaurantBuilder(restaurantInFilteredList)
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT,
                new EditRestaurantDescriptionBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new FoodTrail(model.getFoodTrail()), new UserPrefs());
        expectedModel.setRestaurant(model.getFilteredRestaurantList().get(0), editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Restaurant firstPerson = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptionBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_RESTAURANT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        // edit restaurant in filtered list into a duplicate in address book
        Restaurant restaurantInList = model.getFoodTrail()
                .getRestaurantList().get(INDEX_SECOND_RESTAURANT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT,
                new EditRestaurantDescriptionBuilder(restaurantInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptionBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getFoodTrail().getRestaurantList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditRestaurantDescriptionBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_RESTAURANT, DESC_AMY);

        // same values -> returns true
        EditRestaurantDescriptor copyDescriptor = new EditRestaurantDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_RESTAURANT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_RESTAURANT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_RESTAURANT, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditRestaurantDescriptor editRestaurantDescriptor = new EditRestaurantDescriptor();
        EditCommand editCommand = new EditCommand(index, editRestaurantDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editRestaurantDescriptor="
                + editRestaurantDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
