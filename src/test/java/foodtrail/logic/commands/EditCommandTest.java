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

import org.junit.jupiter.api.BeforeEach;
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

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Restaurant restaurantToEdit = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC)
                .withPhone(VALID_PHONE_KFC).withAddress("123 New Street, #01-01 New Building, Singapore 123456").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_RESTAURANT, descriptor);

        Restaurant editedRestaurant = EditCommand.createEditedRestaurant(restaurantToEdit, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS,
                Messages.format(editedRestaurant));

        // Manually construct expected RestaurantDirectory to ensure exact state with new instances
        RestaurantDirectory expectedRestaurantDirectory = new RestaurantDirectory();
        for (Restaurant r : model.getRestaurantDirectory().getRestaurantList()) {
            if (r.isSameRestaurant(restaurantToEdit)) {
                expectedRestaurantDirectory.addRestaurant(editedRestaurant);
            } else {
                // Create a new instance for unchanged restaurants to avoid object identity issues
                expectedRestaurantDirectory.addRestaurant(new RestaurantBuilder(r).build());
            }
        }

        Model expectedModel = new ModelManager(expectedRestaurantDirectory, new UserPrefs());
        expectedModel.updateFilteredRestaurantList(Model.PREDICATE_SHOW_ALL_RESTAURANTS);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    // ... other tests ...
}
