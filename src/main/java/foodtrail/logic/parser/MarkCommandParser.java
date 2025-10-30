package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.MarkCommand;
import foodtrail.logic.commands.UnmarkCommand;
import foodtrail.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MarkCommand object
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);

        return new MarkCommand(index);
    }

}
