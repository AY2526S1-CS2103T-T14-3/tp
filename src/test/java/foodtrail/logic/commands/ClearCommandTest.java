package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;

import org.junit.jupiter.api.Test;

import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyRestaurantDirectory_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyRestaurantDirectory_success() {
        Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
        expectedModel.setRestaurantDirectory(new RestaurantDirectory());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
