package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyFoodTrail;

/**
 * Represents a storage for {@link seedu.address.model.FoodTrail}.
 */
public interface FoodTrailStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getFoodTrailFilePath();

    /**
     * Returns FoodTrail data as a {@link ReadOnlyFoodTrail}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyFoodTrail> readFoodTrail() throws DataLoadingException;

    /**
     * @see #getFoodTrailFilePath()
     */
    Optional<ReadOnlyFoodTrail> readFoodTrail(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyFoodTrail} to the storage.
     * @param foodTrail cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveFoodTrail(ReadOnlyFoodTrail foodTrail) throws IOException;

    /**
     * @see #saveFoodTrail(ReadOnlyFoodTrail)
     */
    void saveFoodTrail(ReadOnlyFoodTrail foodTrail, Path filePath) throws IOException;

}
