package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;

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
                new FindCommand(new RestaurantContainsKeywordsPredicate(Collections.singletonList("kfc")), "kfc");
        assertParseSuccess(parser, "kfc", expectedFindCommand);

        // leading and trailing whitespaces
        assertParseSuccess(parser, "  kfc  ", expectedFindCommand);
    }

}
