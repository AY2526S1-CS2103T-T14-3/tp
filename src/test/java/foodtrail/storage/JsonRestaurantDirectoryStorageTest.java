package foodtrail.storage;

import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static foodtrail.testutil.TypicalRestaurants.MIXUE;
import static foodtrail.testutil.TypicalRestaurants.PIZZAHUT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodtrail.commons.exceptions.DataLoadingException;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.RestaurantDirectory;

public class JsonRestaurantDirectoryStorageTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonRestaurantDirectoryStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readRestaurantDirectory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readRestaurantDirectory(null));
    }

    private java.util.Optional<ReadOnlyRestaurantDirectory> readRestaurantDirectory(String filePath) throws Exception {
        return new JsonRestaurantDirectoryStorage(Paths.get(filePath))
                .readRestaurantDirectory(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readRestaurantDirectory("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () ->
                readRestaurantDirectory("notJsonFormatRestaurantDirectory.json"));
    }

    @Test
    public void readRestaurantDirectory_invalidRestaurantRestaurantDirectory_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () ->
                readRestaurantDirectory("invalidRestaurantRestaurantDirectory.json"));
    }

    @Test
    public void readRestaurantDirectory_invalidAndValidRestaurantRestaurantDirectory_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () ->
                readRestaurantDirectory("invalidAndValidRestaurantRestaurantDirectory.json"));
    }

    @Test
    public void readAndSaveRestaurantDirectory_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempRestaurantDirectory.json");
        RestaurantDirectory original = getTypicalRestaurantDirectory();
        JsonRestaurantDirectoryStorage jsonRestaurantDirectoryStorage = new JsonRestaurantDirectoryStorage(filePath);

        // Save in new file and read back
        jsonRestaurantDirectoryStorage.saveRestaurantDirectory(original, filePath);
        ReadOnlyRestaurantDirectory readBack = jsonRestaurantDirectoryStorage.readRestaurantDirectory(filePath).get();
        assertEquals(original, new RestaurantDirectory(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addRestaurant(MIXUE);
        original.removeRestaurant(MCDONALDS);
        jsonRestaurantDirectoryStorage.saveRestaurantDirectory(original, filePath);
        readBack = jsonRestaurantDirectoryStorage.readRestaurantDirectory(filePath).get();
        assertEquals(original, new RestaurantDirectory(readBack));

        // Save and read without specifying file path
        original.addRestaurant(PIZZAHUT);
        jsonRestaurantDirectoryStorage.saveRestaurantDirectory(original); // file path not specified
        readBack = jsonRestaurantDirectoryStorage.readRestaurantDirectory().get(); // file path not specified
        assertEquals(original, new RestaurantDirectory(readBack));

    }

    @Test
    public void saveRestaurantDirectory_nullRestaurantDirectory_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                saveRestaurantDirectory(null, "SomeFile.json"));
    }

    /**
     * Saves {@code restaurantDirectory} at the specified {@code filePath}.
     */
    private void saveRestaurantDirectory(ReadOnlyRestaurantDirectory restaurantDirectory, String filePath) {
        try {
            new JsonRestaurantDirectoryStorage(Paths.get(filePath))
                    .saveRestaurantDirectory(restaurantDirectory, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveRestaurantDirectory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                saveRestaurantDirectory(new RestaurantDirectory(), null));
    }
}
