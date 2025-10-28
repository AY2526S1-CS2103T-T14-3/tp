package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Remove a tag of an existing restaurant in the restaurant directory.
 */
public class UntagCommand extends Command {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Remove a tag of the restaurant identified "
            + "by the index number used in the displayed restaurant directory. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "halal";

    public static final String MESSAGE_UNTAG_SUCCESS = "Removed %1$s tag(s) from restaurant:\n%2$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "The tag(s) does not exist for this restaurant: ";
    public static final String MESSAGE_EMPTY_TAG = "Tag name cannot be empty.";

    private final Index index;
    private final Set<Tag> tags;

    /**
     * @param index of the restaurant in the filtered restaurant list to add the tag to
     * @param tags to be added to the restaurant
     */
    public UntagCommand(Index index, Set<Tag> tags) {
        requireNonNull(index);
        requireNonNull(tags);

        this.index = index;
        this.tags = tags;
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

        if (!existingTags.containsAll(tags)) {
            Set<Tag> invalidTags = new HashSet<>(tags);
            invalidTags.removeAll(existingTags);
            String invalidTagsString = invalidTags.stream().map(t -> "'" + t.tagName + "'")
                    .collect(Collectors.joining(", "));
            throw new CommandException(MESSAGE_TAG_NOT_FOUND + invalidTagsString);
        }

        Set<Tag> newTags = new HashSet<>(existingTags);
        newTags.removeAll(tags);

        Restaurant editedRestaurant = new Restaurant(
                restaurantToEdit.getName(), restaurantToEdit.getPhone(),
                restaurantToEdit.getAddress(), newTags, restaurantToEdit.getRating(), restaurantToEdit.getIsMarked());

        model.setRestaurant(restaurantToEdit, editedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        String tagsRemovedString = tags.stream()
                .sorted(Comparator.comparing(t -> t.tagName))
                .map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));

        String restaurantDetails = "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .sorted(Comparator.comparing(t -> t.tagName))
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_UNTAG_SUCCESS, tagsRemovedString, restaurantDetails));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;
        return index.equals(otherUntagCommand.index)
                && tags.equals(otherUntagCommand.tags);
    }
}
