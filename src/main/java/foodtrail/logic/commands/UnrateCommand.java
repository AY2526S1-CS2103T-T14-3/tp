package foodtrail.logic.commands;

import static foodtrail.logic.Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX;
import static java.util.Objects.requireNonNull;

import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Restaurant;

/**
 * Removes the rating from a restaurant in the restaurant directory.
 */
public class UnrateCommand extends Command {

    public static final String COMMAND_WORD = "unrate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the rating from a restaurant.\n"
            + "Parameters: INDEX (positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_UNRATE_SUCCESS = "Removed rating from %1$s.";
    public static final String MESSAGE_RESTAURANT_NOT_RATED = "This restaurant has no rating to remove: %1$s.";

    private final Index index;

    /**
     * @param index of the restaurant in the filtered restaurant list to unrate
     */
    public UnrateCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToEdit = lastShownList.get(index.getZeroBased());

        // Check if restaurant has a rating
        if (!restaurantToEdit.getRating().isPresent()) {
            throw new CommandException(String.format(MESSAGE_RESTAURANT_NOT_RATED, restaurantToEdit.getName()));
        }

        Restaurant edited = restaurantToEdit.withRating(null);

        model.setRestaurant(restaurantToEdit, edited);
        return new CommandResult(String.format(MESSAGE_UNRATE_SUCCESS, edited.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnrateCommand)) {
            return false;
        }
        UnrateCommand o = (UnrateCommand) other;
        return index.equals(o.index);
    }

    @Override
    public String toString() {
        return String.format("%s{index=%s}",
                getClass().getSimpleName(), index);
    }
}
