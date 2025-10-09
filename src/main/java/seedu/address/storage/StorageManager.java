package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyFoodTrail;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private FoodTrailStorage foodTrailStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code FoodTrailStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(FoodTrailStorage foodTrailStorage, UserPrefsStorage userPrefsStorage) {
        this.foodTrailStorage = foodTrailStorage;
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


    // ================ AddressBook methods ==============================

    @Override
    public Path getFoodTrailFilePath() {
        return foodTrailStorage.getFoodTrailFilePath();
    }

    @Override
    public Optional<ReadOnlyFoodTrail> readFoodTrail() throws DataLoadingException {
        return readFoodTrail(foodTrailStorage.getFoodTrailFilePath());
    }

    @Override
    public Optional<ReadOnlyFoodTrail> readFoodTrail(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return foodTrailStorage.readFoodTrail(filePath);
    }

    @Override
    public void saveFoodTrail(ReadOnlyFoodTrail foodTrail) throws IOException {
        saveFoodTrail(foodTrail, foodTrailStorage.getFoodTrailFilePath());
    }

    @Override
    public void saveFoodTrail(ReadOnlyFoodTrail foodTrail, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        foodTrailStorage.saveFoodTrail(foodTrail, filePath);
    }

}
