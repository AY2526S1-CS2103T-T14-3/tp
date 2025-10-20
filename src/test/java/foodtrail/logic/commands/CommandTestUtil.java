package foodtrail.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.logic.parser.CliSyntax.PREFIX_TAG;
import static foodtrail.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.AddressBook;
import foodtrail.model.Model;
import foodtrail.model.person.Person;
import foodtrail.model.person.PersonContainsKeywordsPredicate;
import foodtrail.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_JOLLIBEE = "Jollibee";
    public static final String VALID_NAME_KFC = "KFC";
    public static final String VALID_PHONE_JOLLIBEE = "67353711";
    public static final String VALID_PHONE_KFC = "62226111";
    public static final String VALID_ADDRESS_JOLLIBEE = "1 Scotts Rd, #01-07 Shaw Centre, Singapore 228208";
    public static final String VALID_ADDRESS_KFC = "176 Orchard Rd, #01-34/35/36 The Centrepoint, Singapore 238843";
    public static final String VALID_TAG_FASTFOOD = "fastfood";
    public static final String VALID_TAG_HALAL = "halal";

    public static final String NAME_DESC_JOLLIBEE = " " + PREFIX_NAME + VALID_NAME_JOLLIBEE;
    public static final String NAME_DESC_KFC = " " + PREFIX_NAME + VALID_NAME_KFC;
    public static final String PHONE_DESC_JOLLIBEE = " " + PREFIX_PHONE + VALID_PHONE_JOLLIBEE;
    public static final String PHONE_DESC_KFC = " " + PREFIX_PHONE + VALID_PHONE_KFC;
    public static final String ADDRESS_DESC_JOLLIBEE = " " + PREFIX_ADDRESS + VALID_ADDRESS_JOLLIBEE;
    public static final String ADDRESS_DESC_KFC = " " + PREFIX_ADDRESS + VALID_ADDRESS_KFC;
    public static final String TAG_DESC_HALAL = " " + PREFIX_TAG + VALID_TAG_HALAL;
    public static final String TAG_DESC_FASTFOOD = " " + PREFIX_TAG + VALID_TAG_FASTFOOD;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + ""; // empty string not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "6512a345"; // 'a' not allowed in phones
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS + "Singapore"; // empty string not allowed
    // for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "western*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_JOLLIBEE;
    public static final EditCommand.EditPersonDescriptor DESC_KFC;

    static {
        DESC_JOLLIBEE = new EditPersonDescriptorBuilder().withName(VALID_NAME_JOLLIBEE)
                .withPhone(VALID_PHONE_JOLLIBEE).withAddress(VALID_ADDRESS_JOLLIBEE).build();
        DESC_KFC = new EditPersonDescriptorBuilder().withName(VALID_NAME_KFC)
                .withPhone(VALID_PHONE_KFC).withAddress(VALID_ADDRESS_KFC).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
