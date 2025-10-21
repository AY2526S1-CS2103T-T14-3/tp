package foodtrail.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import foodtrail.commons.core.LogsCenter;
import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.ReadOnlyUserPrefs;
import foodtrail.model.UserPrefs;

/**
 * Manages storage of RestaurantDirectory data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private RestaurantDirectoryStorage restaurantDirectoryStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code RestaurantDirectoryStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(RestaurantDirectoryStorage restaurantDirectoryStorage, UserPrefsStorage userPrefsStorage) {
        this.restaurantDirectoryStorage = restaurantDirectoryStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ RestaurantDirectory methods ==============================

    @Override
    public Path getRestaurantDirectoryFilePath() {
        return restaurantDirectoryStorage.getRestaurantDirectoryFilePath();
    }

    @Override
    public Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory() throws DataLoadingException {
        return readRestaurantDirectory(restaurantDirectoryStorage.getRestaurantDirectoryFilePath());
    }

    @Override
    public Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return restaurantDirectoryStorage.readRestaurantDirectory(filePath);
    }

    @Override
    public void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory) throws IOException {
        saveRestaurantDirectory(restaurantDirectory, restaurantDirectoryStorage.getRestaurantDirectoryFilePath());
    }

    @Override
    public void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory, Path filePath)
            throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        restaurantDirectoryStorage.saveRestaurantDirectory(restaurantDirectory, filePath);
    }

}
