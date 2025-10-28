package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Set;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.UntagCommand;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.restaurant.Tag;

/**
 * Parses input arguments and creates a new UntagCommand object
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UntagCommand
     * and returns an UntagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UntagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
        }

        Collection<String> tagNames = argMultimap.getAllValues(PREFIX_TAG);

        // Handles `untag 1` - no tag prefix provided
        if (tagNames.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        // Handles `untag 1 t/` - tag prefix is present but empty
        if (tagNames.stream().anyMatch(String::isEmpty)) {
            throw new ParseException(UntagCommand.MESSAGE_EMPTY_TAG);
        }

        Set<Tag> tagList = ParserUtil.parseTags(tagNames);

        return new UntagCommand(index, tagList);
    }
}
