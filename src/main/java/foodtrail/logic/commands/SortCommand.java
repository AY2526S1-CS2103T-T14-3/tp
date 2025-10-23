package foodtrail.logic.commands;

import static java.util.Objects.requireNonNull;

import foodtrail.model.Model;

/** Sorts the restaurant list by name in ascending (A→Z) order. */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts restaurants by name.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Sorted restaurants by name.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortRestaurantListByName();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
