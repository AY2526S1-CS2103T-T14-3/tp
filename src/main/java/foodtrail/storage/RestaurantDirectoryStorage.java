package foodtrail.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.RestaurantDirectory;

/**
 * Represents a storage for {@link RestaurantDirectory}.
 */
public interface RestaurantDirectoryStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getRestaurantDirectoryFilePath();

    /**
     * Returns RestaurantDirectory data as a {@link ReadOnlyRestaurantDirectory}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory() throws DataLoadingException;

    /**
     * @see #getRestaurantDirectoryFilePath()
     */
    Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyRestaurantDirectory} to the storage.
     * @param restaurantDirectory cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory) throws IOException;

    /**
     * @see #saveRestaurantDirectory(ReadOnlyRestaurantDirectory)
     */
    void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory, Path filePath) throws IOException;

}
