package foodtrail.logic.commands;

import static foodtrail.logic.Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX;
import static java.util.Objects.requireNonNull;

import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;

/**
 * Rates a restaurant from 0 to 5 (inclusive).
 */
public class RateCommand extends Command {

    public static final String COMMAND_WORD = "rate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Rates a restaurant from 0 to 5.\n"
            + "Parameters: INDEX (positive integer) RATING (integer 0–5)\n"
            + "Example: " + COMMAND_WORD + " 2 4";

    public static final String MESSAGE_RATE_SUCCESS = "Rated %1$s: %2$d/5";

    private final Index index;
    private final int ratingValue;

    /**
     * @param index       of the restaurant in the filtered restaurant list to edit
     * @param ratingValue of the rating to be set
     */
    public RateCommand(Index index, int ratingValue) {
        this.index = index;
        this.ratingValue = ratingValue;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToEdit = lastShownList.get(index.getZeroBased());
        Restaurant edited = restaurantToEdit.withRating(new Rating(ratingValue));

        model.setRestaurant(restaurantToEdit, edited);
        model.updateFilteredRestaurantList(Model.PREDICATE_SHOW_ALL_RESTAURANTS);
        return new CommandResult(String.format(MESSAGE_RATE_SUCCESS, edited.getName(), ratingValue));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RateCommand)) {
            return false;
        }
        RateCommand o = (RateCommand) other;
        return index.equals(o.index) && ratingValue == o.ratingValue;
    }

    @Override
    public String toString() {
        return String.format("%s{index=%s, ratingValue=%d}",
                getClass().getSimpleName(), index, ratingValue);
    }
}
