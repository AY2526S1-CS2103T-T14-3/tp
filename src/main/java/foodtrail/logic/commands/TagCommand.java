package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.LinkedHashSet;
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
 * Adds a tag to an existing restaurant in the restaurant directory.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the restaurant identified "
            + "by the index number used in the displayed restaurant directory.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "halal";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added %1$s tag(s) to restaurant:\n%2$s";
    public static final String MESSAGE_DUPLICATE_TAG = "The following tag(s) already exist for this restaurant: ";
    public static final String MESSAGE_EMPTY_TAG = "Tag name cannot be empty.";

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

        Set<Tag> duplicateTags = new LinkedHashSet<>(this.tag);
        duplicateTags.retainAll(existingTags);

        if (!duplicateTags.isEmpty()) {
            String duplicateTagsString = duplicateTags.stream()
                    .map(t -> t.tagName)
                    .collect(Collectors.joining(", "));
            throw new CommandException(MESSAGE_DUPLICATE_TAG + duplicateTagsString);
        }

        Set<Tag> newTags = new LinkedHashSet<>(existingTags);
        newTags.addAll(this.tag);

        Restaurant editedRestaurant = new Restaurant(
                restaurantToEdit.getName(), restaurantToEdit.getPhone(),
                restaurantToEdit.getAddress(), newTags, restaurantToEdit.getRating(), restaurantToEdit.getIsMarked());

        model.setRestaurant(restaurantToEdit, editedRestaurant);

        String tagsAddedString = this.tag.stream()
                //.sorted(Comparator.comparing(tag -> tag.tagName))
                .map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));

        String restaurantDetails = "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_ADD_TAG_SUCCESS, tagsAddedString, restaurantDetails));
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
