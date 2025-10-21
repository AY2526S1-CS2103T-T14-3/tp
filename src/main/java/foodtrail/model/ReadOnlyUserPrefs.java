package foodtrail.model;

import java.nio.file.Path;

import foodtrail.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getRestaurantDirectoryFilePath();

}
