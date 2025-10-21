package foodtrail.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import foodtrail.commons.core.GuiSettings;
import foodtrail.commons.core.LogsCenter;
import foodtrail.logic.commands.Command;
import foodtrail.logic.commands.CommandResult;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.logic.parser.RestaurantDirectoryParser;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.Model;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final RestaurantDirectoryParser restaurantDirectoryParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        restaurantDirectoryParser = new RestaurantDirectoryParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = restaurantDirectoryParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveRestaurantDirectory(model.getRestaurantDirectory());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyRestaurantDirectory getRestaurantDirectory() {
        return model.getRestaurantDirectory();
    }

    @Override
    public ObservableList<Restaurant> getFilteredRestaurantList() {
        return model.getFilteredRestaurantList();
    }

    @Override
    public Path getRestaurantDirectoryFilePath() {
        return model.getRestaurantDirectoryFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
