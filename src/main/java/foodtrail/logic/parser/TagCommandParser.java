package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Set;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.TagCommand;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.restaurant.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), pe);
        }

        Collection<String> tagNames = argMultimap.getAllValues(PREFIX_TAG);

        if (tagNames.isEmpty() || tagNames.stream().anyMatch(String::isEmpty)) {
            throw new ParseException(TagCommand.MESSAGE_EMPTY_TAG);
        }

        Set<Tag> tagSet = ParserUtil.parseTags(tagNames);

        if (tagSet.isEmpty()) {
            throw new ParseException(TagCommand.MESSAGE_EMPTY_TAG);
        }

        return new TagCommand(index, tagSet);
    }
}