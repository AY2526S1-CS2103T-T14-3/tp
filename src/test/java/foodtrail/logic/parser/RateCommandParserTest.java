package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;

import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.RateCommand;

public class RateCommandParserTest {

    private final RateCommandParser parser = new RateCommandParser();

    @Test
    public void parseValidArgsPlainReturnsRateCommand() {
        assertParseSuccess(parser, "1 4", new RateCommand(INDEX_FIRST_RESTAURANT, 4));
    }

    @Test
    public void parseValidArgsWithPrefixReturnsRateCommand() {
        // supports "rate 1 r/5"
        assertParseSuccess(parser, "1 r/5", new RateCommand(INDEX_FIRST_RESTAURANT, 5));
    }

    @Test
    public void parseValidArgsExtraSpacesReturnsRateCommand() {
        assertParseSuccess(parser, "   1    0   ", new RateCommand(INDEX_FIRST_RESTAURANT, 0));
    }

    @Test
    public void parseMissingPartsFailure() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, RateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseInvalidIndexFailure() {
        assertParseFailure(parser, "0 3", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-2 3", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "abc 3", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parseInvalidRatingFailure() {
        assertParseFailure(parser, "1 -1", "Rating must be an integer between 0 and 5 (inclusive).");
        assertParseFailure(parser, "1 6", "Rating must be an integer between 0 and 5 (inclusive).");
        assertParseFailure(parser, "1 r/x", "Rating must be an integer between 0 and 5 (inclusive).");
    }
}
