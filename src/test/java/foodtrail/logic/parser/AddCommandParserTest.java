package foodtrail.logic.parser;

import static foodtrail.logic.commands.CommandTestUtil.ADDRESS_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.ADDRESS_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.NAME_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.NAME_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.PHONE_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.PHONE_DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static foodtrail.logic.commands.CommandTestUtil.TAG_DESC_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.TAG_DESC_HALAL;
import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_HALAL;
import static foodtrail.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static foodtrail.testutil.TypicalRestaurants.JOLLIBEE;
import static foodtrail.testutil.TypicalRestaurants.KFC;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.AddCommand;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.util.SampleDataUtil;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Restaurant expectedRestaurant = new Restaurant(KFC.getName(), KFC.getPhone(), KFC.getAddress(),
                SampleDataUtil.getTagSet(VALID_TAG_FASTFOOD));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_KFC + PHONE_DESC_KFC + ADDRESS_DESC_KFC
                + TAG_DESC_FASTFOOD, new AddCommand(expectedRestaurant));

        // multiple tags - all accepted
        Restaurant expectedRestaurantMultipleTags = new Restaurant(KFC.getName(), KFC.getPhone(), KFC.getAddress(),
                SampleDataUtil.getTagSet(VALID_TAG_FASTFOOD, VALID_TAG_HALAL));
        assertParseSuccess(parser, NAME_DESC_KFC + PHONE_DESC_KFC + ADDRESS_DESC_KFC
                + TAG_DESC_HALAL + TAG_DESC_FASTFOOD, new AddCommand(expectedRestaurantMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Restaurant expectedRestaurant = new Restaurant(JOLLIBEE.getName(), JOLLIBEE.getPhone(), JOLLIBEE.getAddress(),
                new HashSet<>());
        assertParseSuccess(parser, NAME_DESC_JOLLIBEE + PHONE_DESC_JOLLIBEE + ADDRESS_DESC_JOLLIBEE,
                new AddCommand(expectedRestaurant));
    }

    // ... other tests remain the same ...
}
