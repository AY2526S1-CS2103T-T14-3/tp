package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalRestaurants.ALICE;
import static seedu.address.testutil.TypicalRestaurants.HOON;
import static seedu.address.testutil.TypicalRestaurants.IDA;
import static seedu.address.testutil.TypicalRestaurants.getTypicalFoodTrail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.FoodTrail;
import seedu.address.model.ReadOnlyFoodTrail;

public class JsonFoodTrailStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonFoodTrailStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readFoodTrail(null));
    }

    private java.util.Optional<ReadOnlyFoodTrail> readFoodTrail(String filePath) throws Exception {
        return new JsonFoodTrailStorage(Paths.get(filePath)).readFoodTrail(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readFoodTrail("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readFoodTrail("notJsonFoodTrail.json"));
    }

    @Test
    public void readFoodTrail_invalidFoodTrail_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodTrail("invalidRestaurantFoodTrail.json"));
    }

    @Test
    public void readFoodTrail_invalidAndValidFoodTrail_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readFoodTrail("invalidAndValidRestaurantFoodTrail.json"));
    }

    @Test
    public void readAndSaveFoodTrail_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempFoodTrail.json");
        FoodTrail original = getTypicalFoodTrail();
        JsonFoodTrailStorage jsonFoodTrailStorage = new JsonFoodTrailStorage(filePath);

        // Save in new file and read back
        jsonFoodTrailStorage.saveFoodTrail(original, filePath);
        ReadOnlyFoodTrail readBack = jsonFoodTrailStorage.readFoodTrail(filePath).get();
        assertEquals(original, new FoodTrail(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addRestaurant(HOON);
        original.removeRestaurant(ALICE);
        jsonFoodTrailStorage.saveFoodTrail(original, filePath);
        readBack = jsonFoodTrailStorage.readFoodTrail(filePath).get();
        assertEquals(original, new FoodTrail(readBack));

        // Save and read without specifying file path
        original.addRestaurant(IDA);
        jsonFoodTrailStorage.saveFoodTrail(original); // file path not specified
        readBack = jsonFoodTrailStorage.readFoodTrail().get(); // file path not specified
        assertEquals(original, new FoodTrail(readBack));

    }

    @Test
    public void saveFoodTrail_nullFoodTrail_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveFoodTrail(null, "SomeFile.json"));
    }

    /**
     * Saves {@code foodTrail} at the specified {@code filePath}.
     */
    private void saveFoodTrail(ReadOnlyFoodTrail foodTrail, String filePath) {
        try {
            new JsonFoodTrailStorage(Paths.get(filePath))
                    .saveFoodTrail(foodTrail, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveFoodTrail_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveFoodTrail(new FoodTrail(), null));
    }
}
