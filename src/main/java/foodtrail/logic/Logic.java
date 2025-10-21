package foodtrail.logic;

import java.nio.file.Path;

import foodtrail.commons.core.GuiSettings;
import foodtrail.logic.commands.CommandResult;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.Model;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.restaurant.Restaurant;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the RestaurantDirectory.
     *
     * @see Model#getRestaurantDirectory()
     */
    ReadOnlyRestaurantDirectory getRestaurantDirectory();

    /** Returns an unmodifiable view of the filtered list of restaurants */
    ObservableList<Restaurant> getFilteredRestaurantList();

    /**
     * Returns the user prefs' restaurant directory file path.
     */
    Path getRestaurantDirectoryFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
