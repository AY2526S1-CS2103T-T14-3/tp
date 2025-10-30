package foodtrail.storage;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.commons.util.JsonUtil;
import foodtrail.model.RestaurantDirectory;
import foodtrail.testutil.TypicalRestaurants;

public class JsonSerializableRestaurantDirectoryTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableRestaurantDirectoryTest");
    private static final Path TYPICAL_RESTAURANTS_FILE =
            TEST_DATA_FOLDER.resolve("typicalRestaurantsRestaurantDirectory.json");
    private static final Path INVALID_RESTAURANT_FILE =
            TEST_DATA_FOLDER.resolve("invalidRestaurantRestaurantDirectory.json");
    private static final Path DUPLICATE_RESTAURANT_FILE =
            TEST_DATA_FOLDER.resolve("duplicateRestaurantRestaurantDirectory.json");

    @Test
    public void toModelType_typicalRestaurantsFile_success() throws Exception {
        JsonSerializableRestaurantDirectory dataFromFile = JsonUtil.readJsonFile(TYPICAL_RESTAURANTS_FILE,
                JsonSerializableRestaurantDirectory.class).get();
        RestaurantDirectory restaurantDirectoryFromFile = dataFromFile.toModelType();
        RestaurantDirectory typicalRestaurantsRestaurantDirectory = TypicalRestaurants.getTypicalRestaurantDirectory();
        assertEquals(restaurantDirectoryFromFile, typicalRestaurantsRestaurantDirectory);
    }

    @Test
    public void toModelType_invalidRestaurantFile_throwsIllegalValueException() throws Exception {
        JsonSerializableRestaurantDirectory dataFromFile = JsonUtil.readJsonFile(INVALID_RESTAURANT_FILE,
                JsonSerializableRestaurantDirectory.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateRestaurants_throwsIllegalValueException() throws Exception {
        JsonSerializableRestaurantDirectory dataFromFile = JsonUtil.readJsonFile(DUPLICATE_RESTAURANT_FILE,
                JsonSerializableRestaurantDirectory.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableRestaurantDirectory.MESSAGE_DUPLICATE_RESTAURANT,
                dataFromFile::toModelType);
    }

}
