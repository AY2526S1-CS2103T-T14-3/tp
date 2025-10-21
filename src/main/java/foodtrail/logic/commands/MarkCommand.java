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
 * Marks a restaurant as visited.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the restaurant identified by the index number used in the"
            + " displayed restaurant list as visited.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_RESTAURANT_SUCCESS = "Marked Restaurant: %1$s";

    private final Index targetIndex;

    /**
     * @param targetIndex of the restaurant in the filtered restaurant list to mark
     */
    public MarkCommand(Index targetIndex) {
        requireAllNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToMark = lastShownList.get(targetIndex.getZeroBased());
        Restaurant markedRestaurant = restaurantToMark.withMark(new IsMarked(true));

        model.setRestaurant(restaurantToMark, markedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        return new CommandResult(String.format(MESSAGE_MARK_RESTAURANT_SUCCESS, markedRestaurant));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
    }
}
