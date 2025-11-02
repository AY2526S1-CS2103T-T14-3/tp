package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import foodtrail.commons.util.ToStringBuilder;
import foodtrail.model.Model;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;

/**
 * Finds and lists all restaurants in restaurant directory whose attributes contain any of the argument keywords.
 * Keyword matching is case-insensitive and based on substrings.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all restaurants whose attributes contain "
            + "the specified keyword (case-insensitive). "
            + "The search is performed on name, phone number, address, and tags.\n"
            + "Parameters: KEYWORD\n"
            + "Example: " + COMMAND_WORD + " kfc";

    public static final String MESSAGE_FIND_SUCCESS = "%1$d restaurants listed with the keyword '%2$s'";

    private final RestaurantContainsKeywordsPredicate predicate;
    private final String keyword;

    /**
     * Creates a FindCommand to find the specified {@code RestaurantContainsKeywordsPredicate} and keyword.
     */
    public FindCommand(RestaurantContainsKeywordsPredicate predicate, String keyword) {
        this.predicate = predicate;
        this.keyword = keyword;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredRestaurantList(predicate);
        return new CommandResult(
                String.format(MESSAGE_FIND_SUCCESS, model.getFilteredRestaurantList().size(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate)
                && keyword.equals(otherFindCommand.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("keyword", keyword)
                .toString();
    }
}
