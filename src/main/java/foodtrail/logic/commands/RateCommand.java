package foodtrail.logic.commands;

import static foodtrail.logic.Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX;
import static foodtrail.logic.parser.CliSyntax.PREFIX_RATING;
import static java.util.Objects.requireNonNull;

import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;

/**
 * Rates a restaurant from 0 to 5 (inclusive) in the restaurant directory.
 */
public class RateCommand extends Command {

    public static final String COMMAND_WORD = "rate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Rates a restaurant from 0 to 5.\n"
            + "Parameters: INDEX (positive integer) " + PREFIX_RATING + "RATING (integer 0–5)\n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_RATING + "4";

    public static final String MESSAGE_RATE_SUCCESS = "Rated %1$s: %2$d/5";

    public static final String MESSAGE_DUPLICATE_RATING = "This restaurant already has a rating of %1$d/5.";

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

        if (restaurantToEdit.getRating().isPresent() && restaurantToEdit.getRating().get().value == ratingValue) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_RATING, ratingValue));
        }

        Restaurant edited = restaurantToEdit.withRating(new Rating(ratingValue));

        model.setRestaurant(restaurantToEdit, edited);
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
