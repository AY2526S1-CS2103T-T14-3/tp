package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyFoodTrail;

/**
 * A class to access AddressBook data stored as a json file on the hard disk.
 */
public class JsonFoodTrailStorage implements FoodTrailStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonFoodTrailStorage.class);

    private Path filePath;

    public JsonFoodTrailStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFoodTrailFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyFoodTrail> readFoodTrail() throws DataLoadingException {
        return readFoodTrail(filePath);
    }

    /**
     * Similar to {@link #readFoodTrail()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyFoodTrail> readFoodTrail(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableFoodTrail> jsonFoodTrail = JsonUtil.readJsonFile(
                filePath, JsonSerializableFoodTrail.class);
        if (!jsonFoodTrail.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of((ReadOnlyFoodTrail) jsonFoodTrail.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveFoodTrail(ReadOnlyFoodTrail addressBook) throws IOException {
        saveFoodTrail(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveFoodTrail(ReadOnlyAddressBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveFoodTrail(ReadOnlyFoodTrail foodTrail, Path filePath) throws IOException {
        requireNonNull(foodTrail);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableFoodTrail(foodTrail), filePath);
    }

}
