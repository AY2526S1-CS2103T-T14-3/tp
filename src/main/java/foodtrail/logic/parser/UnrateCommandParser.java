package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static java.util.Objects.requireNonNull;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.UnrateCommand;
import foodtrail.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnrateCommand object
 */
public class UnrateCommandParser implements Parser<UnrateCommand> {

    @Override
    public UnrateCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(tokens[0]);

        return new UnrateCommand(index);
    }
}
