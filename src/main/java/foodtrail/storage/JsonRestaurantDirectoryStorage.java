package foodtrail.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import foodtrail.commons.core.LogsCenter;
import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.commons.util.FileUtil;
import foodtrail.commons.util.JsonUtil;
import foodtrail.model.ReadOnlyRestaurantDirectory;

/**
 * A class to access RestaurantDirectory data stored as a json file on the hard disk.
 */
public class JsonRestaurantDirectoryStorage implements RestaurantDirectoryStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonRestaurantDirectoryStorage.class);

    private Path filePath;

    public JsonRestaurantDirectoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getRestaurantDirectoryFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory() throws DataLoadingException {
        return readRestaurantDirectory(filePath);
    }

    /**
     * Similar to {@link #readRestaurantDirectory()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableRestaurantDirectory> jsonRestaurantDirectory = JsonUtil.readJsonFile(
                filePath, JsonSerializableRestaurantDirectory.class);
        if (!jsonRestaurantDirectory.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonRestaurantDirectory.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory) throws IOException {
        saveRestaurantDirectory(restaurantDirectory, filePath);
    }

    /**
     * Similar to {@link #saveRestaurantDirectory(ReadOnlyRestaurantDirectory)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory, Path filePath)
            throws IOException {
        requireNonNull(restaurantDirectory);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableRestaurantDirectory(restaurantDirectory), filePath);
    }

}
