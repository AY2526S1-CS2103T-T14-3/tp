package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CliSyntax.PREFIX_RATING;
import static java.util.Objects.requireNonNull;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.RateCommand;
import foodtrail.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RateCommand object
 */
public class RateCommandParser implements Parser<RateCommand> {

    private static final String MESSAGE_INVALID_RATING = "Rating must be an integer between 0 and 5 (inclusive).";
    static final String MESSAGE_MISSING_RATING_PREFIX = String.format(
            "Rating must be provided with the %s prefix (e.g. %s4).",
            PREFIX_RATING, PREFIX_RATING);

    @Override
    public RateCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(tokens[0]);

        String ratingToken = tokens[1];
        String ratingPrefix = PREFIX_RATING.getPrefix();
        if (!ratingToken.startsWith(ratingPrefix)) {
            throw new ParseException(MESSAGE_MISSING_RATING_PREFIX);
        }
        ratingToken = ratingToken.substring(ratingPrefix.length());

        final int rating;
        try {
            if (ratingToken.isEmpty()) {
                throw new NumberFormatException("Missing rating value");
            }
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
