package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;

import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.UnrateCommand;

public class UnrateCommandParserTest {

    private final UnrateCommandParser parser = new UnrateCommandParser();

    @Test
    public void parseValidArgsReturnsUnrateCommand() {
        assertParseSuccess(parser, "1", new UnrateCommand(INDEX_FIRST_RESTAURANT));
        assertParseSuccess(parser, "2", new UnrateCommand(INDEX_SECOND_RESTAURANT));
    }

    @Test
    public void parseValidArgsExtraSpacesReturnsUnrateCommand() {
        assertParseSuccess(parser, "   1   ", new UnrateCommand(INDEX_FIRST_RESTAURANT));
        assertParseSuccess(parser, " 2 ", new UnrateCommand(INDEX_SECOND_RESTAURANT));
    }

    @Test
    public void parseMissingPartsFailure() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseTooManyArgsFailure() {
        assertParseFailure(parser, "1 2", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseInvalidIndexFailure() {
        assertParseFailure(parser, "0", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "abc", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1.5", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parseEmptyStringFailure() {
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnrateCommand.MESSAGE_USAGE));
    }
}
