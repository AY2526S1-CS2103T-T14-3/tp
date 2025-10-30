package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
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
 * Unmarks a restaurant identified using its displayed index from the restaurant directory as not visited.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the restaurant identified by the index number"
            + " used in the displayed restaurant directory as not visited.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_RESTAURANT_SUCCESS = "Marked restaurant as not visited: %1$s";
    public static final String MESSAGE_RESTAURANT_NOT_MARKED = "This restaurant is not marked as visited: %1$s";

    private final Index targetIndex;

    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToUnmark = lastShownList.get(targetIndex.getZeroBased());

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("\nName: ").append(restaurantToUnmark.getName());
        detailsBuilder.append("\nPhone: ").append(restaurantToUnmark.getPhone());
        detailsBuilder.append("\nAddress: ").append(restaurantToUnmark.getAddress());

        if (!restaurantToUnmark.getTags().isEmpty()) {
            String tagsString = restaurantToUnmark.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));
            detailsBuilder.append("\nTags: ").append(tagsString);
        }

        if (!restaurantToUnmark.getIsMarked().isVisited()) {
            throw new CommandException(String.format(MESSAGE_RESTAURANT_NOT_MARKED, detailsBuilder.toString()));
        }

        Restaurant unmarkedRestaurant = restaurantToUnmark.withIsMarked(new IsMarked(false));

        model.setRestaurant(restaurantToUnmark, unmarkedRestaurant);
        return new CommandResult(String.format(MESSAGE_UNMARK_RESTAURANT_SUCCESS, detailsBuilder.toString()));
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
