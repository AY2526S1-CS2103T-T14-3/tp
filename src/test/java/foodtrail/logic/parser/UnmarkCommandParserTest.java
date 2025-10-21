package foodtrail.logic.parser;

import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;

import org.junit.jupiter.api.Test;

import foodtrail.logic.Messages;
import foodtrail.logic.commands.UnmarkCommand;

public class UnmarkCommandParserTest {

    private UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_validArgs_returnsUnmarkCommand() {
        assertParseSuccess(parser, "1", new UnmarkCommand(INDEX_FIRST_RESTAURANT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
    }
}
