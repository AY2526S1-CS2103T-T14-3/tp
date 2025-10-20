package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.commands.CommandTestUtil.ADDRESS_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.ADDRESS_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static foodtrail.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static foodtrail.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static foodtrail.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static foodtrail.logic.commands.CommandTestUtil.NAME_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.NAME_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.PHONE_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.PHONE_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static foodtrail.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static foodtrail.logic.commands.CommandTestUtil.TAG_DESC_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.TAG_DESC_HALAL;
import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_HALAL;
import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseFailure;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.testutil.TypicalPersons.JOLLIBEE;
import static foodtrail.testutil.TypicalPersons.KFC;

import org.junit.jupiter.api.Test;

import foodtrail.logic.Messages;
import foodtrail.logic.commands.AddCommand;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Person;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Tag;
import foodtrail.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(KFC).withTags(VALID_TAG_HALAL).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_KFC + PHONE_DESC_KFC
                + ADDRESS_DESC_KFC + TAG_DESC_HALAL, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(KFC).withTags(VALID_TAG_HALAL, VALID_TAG_FASTFOOD)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_KFC + PHONE_DESC_KFC + ADDRESS_DESC_KFC + TAG_DESC_FASTFOOD + TAG_DESC_HALAL,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_KFC + PHONE_DESC_KFC
                + ADDRESS_DESC_KFC + TAG_DESC_HALAL;

        // multiple names
        assertParseFailure(parser, NAME_DESC_JOLLIBEE + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_JOLLIBEE + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_JOLLIBEE + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_JOLLIBEE + NAME_DESC_JOLLIBEE + ADDRESS_DESC_JOLLIBEE
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(JOLLIBEE).withTags().build();
        assertParseSuccess(parser, NAME_DESC_JOLLIBEE + PHONE_DESC_JOLLIBEE + ADDRESS_DESC_JOLLIBEE,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_KFC + PHONE_DESC_KFC + ADDRESS_DESC_KFC,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_KFC + VALID_PHONE_KFC + ADDRESS_DESC_KFC,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_KFC + PHONE_DESC_KFC + VALID_ADDRESS_KFC,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_KFC + VALID_PHONE_KFC + VALID_ADDRESS_KFC,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_KFC + ADDRESS_DESC_KFC
                + TAG_DESC_FASTFOOD + TAG_DESC_HALAL, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_KFC + INVALID_PHONE_DESC + ADDRESS_DESC_KFC
                + TAG_DESC_FASTFOOD + TAG_DESC_HALAL, Phone.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_KFC + PHONE_DESC_KFC + INVALID_ADDRESS_DESC
                + TAG_DESC_FASTFOOD + TAG_DESC_HALAL, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_KFC + PHONE_DESC_KFC + ADDRESS_DESC_KFC
                + INVALID_TAG_DESC + VALID_TAG_HALAL, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_KFC + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_KFC + PHONE_DESC_KFC
                + ADDRESS_DESC_KFC + TAG_DESC_FASTFOOD + TAG_DESC_HALAL,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
