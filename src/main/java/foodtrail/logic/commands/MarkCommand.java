package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import foodtrail.commons.core.index.Index;
import foodtrail.commons.util.ToStringBuilder;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.IsMarked;
import foodtrail.model.restaurant.Restaurant;

/**
 * Marks a restaurant identified using its displayed index from the restaurant directory as visited.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the restaurant identified by the index number used in the displayed"
            + " restaurant directory as visited.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_RESTAURANT_SUCCESS = "Marked restaurant as visited: %1$s";
    public static final String MESSAGE_RESTAURANT_ALREADY_MARKED = "This restaurant is already marked as visited: %1$s";

    private final Index targetIndex;

    public MarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToMark = lastShownList.get(targetIndex.getZeroBased());

        String restaurantDetails = "\n" + "Name: " + restaurantToMark.getName() + "\n"
                + "Phone: " + restaurantToMark.getPhone() + "\n"
                + "Address: " + restaurantToMark.getAddress() + "\n"
                + "Tags: " + restaurantToMark.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        if (restaurantToMark.getIsMarked().isVisited()) {
            throw new CommandException(String.format(MESSAGE_RESTAURANT_ALREADY_MARKED, restaurantDetails));
        }

        Restaurant markedRestaurant = restaurantToMark.withIsMarked(new IsMarked(true));

        model.setRestaurant(restaurantToMark, markedRestaurant);


        return new CommandResult(String.format(MESSAGE_MARK_RESTAURANT_SUCCESS, restaurantDetails));
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
