package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import foodtrail.logic.Messages;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
    }

    @Test
    public void execute_newRestaurant_success() {
        Restaurant validRestaurant = new RestaurantBuilder().build();

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        expectedModel.addRestaurant(validRestaurant);
        expectedModel.sortRestaurantListByName();

        assertCommandSuccess(new AddCommand(validRestaurant), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validRestaurant)),
                expectedModel);
    }

    @Test
    public void execute_duplicateRestaurant_throwsCommandException() {
        Restaurant restaurantInList = model.getRestaurantDirectory().getRestaurantList().get(0);
        assertCommandFailure(new AddCommand(restaurantInList), model,
                AddCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

}
