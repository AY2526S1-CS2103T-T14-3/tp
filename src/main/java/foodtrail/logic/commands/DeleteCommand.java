package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.commons.util.ToStringBuilder;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Restaurant;

/**
 * Deletes a restaurant identified using it's displayed index from the restaurant list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the restaurant identified by the index number used in the displayed restaurant list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_RESTAURANT_SUCCESS = "Deleted restaurant: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteRestaurant(restaurantToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_RESTAURANT_SUCCESS, Messages.format(restaurantToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
