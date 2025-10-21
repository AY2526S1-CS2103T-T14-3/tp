package foodtrail.logic;

import static foodtrail.logic.Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX;
import static foodtrail.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static foodtrail.logic.commands.CommandTestUtil.ADDRESS_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.NAME_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.PHONE_DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_JOLLIBEE;
import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodtrail.logic.commands.AddCommand;
import foodtrail.logic.commands.CommandResult;
import foodtrail.logic.commands.ListCommand;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.storage.JsonRestaurantDirectoryStorage;
import foodtrail.storage.JsonUserPrefsStorage;
import foodtrail.storage.StorageManager;
import foodtrail.testutil.RestaurantBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model;
    private Logic logic;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        JsonRestaurantDirectoryStorage restaurantDirectoryStorage =
                new JsonRestaurantDirectoryStorage(temporaryFolder.resolve("foodtrail.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(restaurantDirectoryStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredRestaurantList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredRestaurantList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the. command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getRestaurantDirectory(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an RestaurantDirectoryStorage that throws the IOException e when saving
        JsonRestaurantDirectoryStorage restaurantDirectoryStorage = new JsonRestaurantDirectoryStorage(prefPath) {
            @Override
            public void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(restaurantDirectoryStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveRestaurantDirectory method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_JOLLIBEE + PHONE_DESC_JOLLIBEE + ADDRESS_DESC_JOLLIBEE;
        Restaurant expectedRestaurant = new RestaurantBuilder().withName(VALID_NAME_JOLLIBEE)
                .withPhone(VALID_PHONE_JOLLIBEE).withAddress(VALID_ADDRESS_JOLLIBEE).build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addRestaurant(expectedRestaurant);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
