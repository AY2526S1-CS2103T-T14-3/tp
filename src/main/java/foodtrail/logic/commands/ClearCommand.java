package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import foodtrail.model.Model;
import foodtrail.model.RestaurantDirectory;

/**
 * Clears the restaurant directory.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Restaurant directory has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setRestaurantDirectory(new RestaurantDirectory());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
