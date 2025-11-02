package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;

import foodtrail.logic.commands.FindCommand;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new RestaurantContainsKeywordsPredicate(Collections.singletonList(trimmedArgs)),
                trimmedArgs);
    }

}
