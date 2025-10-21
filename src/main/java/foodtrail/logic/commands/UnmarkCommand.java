package foodtrail.logic.commands;

import static foodtrail.commons.util.CollectionUtil.requireAllNonNull;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;

import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.IsMarked;
import foodtrail.model.restaurant.Restaurant;

/**
 * Unmarks a restaurant as not visited.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the restaurant identified by the index number used "
            + "in the displayed restaurant list as not visited.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_RESTAURANT_SUCCESS = "Unmarked Restaurant: %1$s";

    private final Index targetIndex;

    /**
     * @param targetIndex of the restaurant in the filtered restaurant list to unmark
     */
    public UnmarkCommand(Index targetIndex) {
        requireAllNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToUnmark = lastShownList.get(targetIndex.getZeroBased());
        Restaurant unmarkedRestaurant = restaurantToUnmark.withMark(new IsMarked(false));

        model.setRestaurant(restaurantToUnmark, unmarkedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        return new CommandResult(String.format(MESSAGE_UNMARK_RESTAURANT_SUCCESS, unmarkedRestaurant));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndex.equals(otherUnmarkCommand.targetIndex);
    }
}
