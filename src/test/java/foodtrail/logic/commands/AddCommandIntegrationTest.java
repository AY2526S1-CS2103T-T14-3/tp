package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("\nName: ").append(validRestaurant.getName());
        detailsBuilder.append("\nPhone: ").append(validRestaurant.getPhone());
        detailsBuilder.append("\nAddress: ").append(validRestaurant.getAddress());

        if (!validRestaurant.getTags().isEmpty()) {
            String tagsString = validRestaurant.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));
            detailsBuilder.append("\nTags: ").append(tagsString);
        }

        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        expectedModel.addRestaurant(validRestaurant);
        expectedModel.sortRestaurantListByName();

        assertCommandSuccess(new AddCommand(validRestaurant), model,
                String.format(AddCommand.MESSAGE_SUCCESS, detailsBuilder.toString()),
                expectedModel);
    }

    @Test
    public void execute_duplicateRestaurant_throwsCommandException() {
        Restaurant restaurantInList = model.getRestaurantDirectory().getRestaurantList().get(0);
        assertCommandFailure(new AddCommand(restaurantInList), model,
                AddCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

}
