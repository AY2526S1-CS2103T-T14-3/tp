package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;

import java.util.Set;

import org.junit.jupiter.api.Test;

import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;
import foodtrail.testutil.RestaurantBuilder;
import foodtrail.logic.Messages;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_untagRestaurant_success() {
        Restaurant restaurantToUntag = model.getFilteredRestaurantList().get(INDEX_SECOND_RESTAURANT.getZeroBased());
        Set<Tag> tagsToRemove = Set.of(new Tag("bubbletea"));
        UntagCommand untagCommand = new UntagCommand(INDEX_SECOND_RESTAURANT, tagsToRemove);

        Restaurant expectedRestaurant = new RestaurantBuilder(restaurantToUntag).withTags("drinks").build();

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_RESTAURANT_SUCCESS, Messages.format(expectedRestaurant));

        // Create a copy of the model's restaurant directory to set up the expected state
        RestaurantDirectory expectedRestaurantDirectory = new RestaurantDirectory(model.getRestaurantDirectory());
        expectedRestaurantDirectory.setRestaurant(restaurantToUntag, expectedRestaurant);
        Model expectedModel = new ModelManager(expectedRestaurantDirectory, new UserPrefs());

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }
}
