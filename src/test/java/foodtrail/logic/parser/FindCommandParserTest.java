package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.FindCommand;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new RestaurantContainsKeywordsPredicate(Arrays.asList("ang mo kio", "serangoon")));
        assertParseSuccess(parser, "ang mo kio, serangoon", expectedFindCommand);

        // multiple whitespaces around keywords
        assertParseSuccess(parser, " \n ang mo kio \n, \t serangoon  \t", expectedFindCommand);
    }

    @Test
    public void parse_emptyKeywords_throwsParseException() {
        // empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // only commas
        assertParseFailure(parser, ",", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, ", ,", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

}
