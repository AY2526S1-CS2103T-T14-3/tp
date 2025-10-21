package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Adds a tag to an existing restaurant in the restaurant directory.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the restaurant identified "
            + "by the index number used in the displayed restaurant directory. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "halal";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added tag to restaurant: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This tag already exists for the restaurant.";

    private final Index index;
    private final Set<Tag> tag;

    /**
     * @param index of the restaurant in the filtered restaurant list to add the tag to
     * @param tag to be added to the restaurant
     */
    public TagCommand(Index index, Set<Tag> tag) {
        requireNonNull(index);
        requireNonNull(tag);

        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> existingTags = restaurantToEdit.getTags();

        Set<Tag> newTags = new HashSet<>(existingTags);
        newTags.addAll(tag);

        Restaurant editedRestaurant = new Restaurant(
                restaurantToEdit.getName(), restaurantToEdit.getPhone(),
                restaurantToEdit.getAddress(), newTags);

        model.setRestaurant(restaurantToEdit, editedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        return new CommandResult(String.format(MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedRestaurant)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return index.equals(otherTagCommand.index)
                && tag.equals(otherTagCommand.tag);
    }
}
