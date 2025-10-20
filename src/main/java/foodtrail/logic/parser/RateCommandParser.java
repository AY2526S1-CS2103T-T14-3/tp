package foodtrail.logic.parser;

import static java.util.Objects.requireNonNull;
import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.RateCommand;
import foodtrail.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RateCommand object
 */
public class RateCommandParser implements Parser<RateCommand> {

    private static final String MESSAGE_INVALID_RATING = "Rating must be an integer between 0 and 5 (inclusive).";

    @Override
    public RateCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(tokens[0]);

        String ratingToken = tokens[1];
        if (ratingToken.startsWith("r/")) { // allow "rate 2 r/4"
            ratingToken = ratingToken.substring(2);
        }

        final int rating;
        try {
            rating = Integer.parseInt(ratingToken);
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_RATING);
        }
        if (rating < 0 || rating > 5) {
            throw new ParseException(MESSAGE_INVALID_RATING);
        }

        return new RateCommand(index, rating);
    }
}
