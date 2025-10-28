package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
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
import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.EditRestaurantDescriptorBuilder;
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Restaurant restaurantToEdit = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant editedRestaurant = new RestaurantBuilder().build();
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder(editedRestaurant).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT, descriptor);


        Restaurant finalExpectedRestaurant = new Restaurant(editedRestaurant.getName(), editedRestaurant.getPhone(),
                editedRestaurant.getAddress(), restaurantToEdit.getTags());

        String restaurantDetails = "\n" + "Name: " + finalExpectedRestaurant.getName() + "\n"
                + "Phone: " + finalExpectedRestaurant.getPhone() + "\n"
                + "Address: " + finalExpectedRestaurant.getAddress() + "\n"
                + "Tags: " + finalExpectedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurant(restaurantToEdit, finalExpectedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastRestaurant = Index.fromOneBased(model.getFilteredRestaurantList().size());
        Restaurant lastRestaurant = model.getFilteredRestaurantList().get(indexLastRestaurant.getZeroBased());

        RestaurantBuilder restaurantInList = new RestaurantBuilder(lastRestaurant);
        Restaurant editedRestaurant = restaurantInList.withName(VALID_NAME_KFC).withPhone(VALID_PHONE_KFC).build();

        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC)
                .withPhone(VALID_PHONE_KFC).build();
        EditCommand editCommand = new EditCommand(indexLastRestaurant, descriptor);

        String restaurantDetails = "\n" + "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurant(lastRestaurant, editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT, new EditRestaurantDescriptor());
        Restaurant editedRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());

        String restaurantDetails = "\n" + "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantInFilteredList = model.getFilteredRestaurantList()
                .get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Restaurant editedRestaurant = new RestaurantBuilder(restaurantInFilteredList)
                .withName(VALID_NAME_KFC).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT,
                new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build());

        String restaurantDetails = "\n" + "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurant(model.getFilteredRestaurantList().get(0), editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateRestaurantUnfilteredList_failure() {
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder(firstRestaurant).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_RESTAURANT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

    @Test
    public void execute_duplicateRestaurantFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        // edit restaurant in filtered list into a duplicate in restaurant directory
        Restaurant restaurantInList = model.getRestaurantDirectory().getRestaurantList()
                .get(INDEX_SECOND_RESTAURANT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT,
                new EditRestaurantDescriptorBuilder(restaurantInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

    @Test
    public void execute_invalidRestaurantIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of restaurant directory
     */
    @Test
    public void execute_invalidRestaurantIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_RESTAURANT, DESC_JOLLIBEE);

        // same values -> returns true
        EditRestaurantDescriptor copyDescriptor = new EditRestaurantDescriptor(DESC_JOLLIBEE);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_RESTAURANT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_RESTAURANT, DESC_JOLLIBEE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_RESTAURANT, DESC_KFC)));
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
