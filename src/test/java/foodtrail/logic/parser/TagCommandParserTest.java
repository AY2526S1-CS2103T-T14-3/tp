package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.TagCommand;
import foodtrail.model.restaurant.Tag;

public class TagCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE);

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, PREFIX_TAG + "halal", MESSAGE_INVALID_FORMAT);

        // no tag specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no tag specified
        assertParseFailure(parser, "1 t/", TagCommand.MESSAGE_EMPTY_TAG);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + PREFIX_TAG + "halal", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + PREFIX_TAG + "halal", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // empty tag
        assertParseFailure(parser, "1" + TAG_EMPTY, TagCommand.MESSAGE_EMPTY_TAG);

        // invalid tag format
        assertParseFailure(parser, "1 " + PREFIX_TAG + "halal!", Tag.MESSAGE_CONSTRAINTS);

        // one valid and one invalid tag
        assertParseFailure(parser, "1 " + PREFIX_TAG + "halal! "
                + PREFIX_TAG + "spicy", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_singleTag_success() {
        Index targetIndex = INDEX_FIRST_RESTAURANT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_TAG + "spicy";

        Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("spicy")));
        TagCommand expectedCommand = new TagCommand(targetIndex, tags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleTags_success() {
        Index targetIndex = INDEX_FIRST_RESTAURANT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_TAG + "spicy "
                + PREFIX_TAG + "western";

        Set<Tag> tags = new HashSet<>(Arrays.asList(new Tag("spicy"), new Tag("western")));
        TagCommand expectedCommand = new TagCommand(targetIndex, tags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
