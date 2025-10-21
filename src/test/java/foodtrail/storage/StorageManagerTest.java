package foodtrail.storage;

import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import foodtrail.commons.core.GuiSettings;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonRestaurantDirectoryStorage restaurantDirectoryStorage =
                new JsonRestaurantDirectoryStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(restaurantDirectoryStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void restaurantDirectoryReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonRestaurantDirectoryStorage} class.
         * More extensive testing of UserPref saving/reading is done
         * in {@link JsonRestaurantDirectoryStorageTest} class.
         */
        RestaurantDirectory original = getTypicalRestaurantDirectory();
        storageManager.saveRestaurantDirectory(original);
        ReadOnlyRestaurantDirectory retrieved = storageManager.readRestaurantDirectory().get();
        assertEquals(original, new RestaurantDirectory(retrieved));
    }

    @Test
    public void getRestaurantDirectoryFilePath() {
        assertNotNull(storageManager.getRestaurantDirectoryFilePath());
    }

}
