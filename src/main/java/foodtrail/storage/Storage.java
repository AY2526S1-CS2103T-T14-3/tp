package foodtrail.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.ReadOnlyUserPrefs;
import foodtrail.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends RestaurantDirectoryStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getRestaurantDirectoryFilePath();

    @Override
    Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory() throws DataLoadingException;

    @Override
    void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory) throws IOException;

}
