package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.FoodTrail;
import seedu.address.testutil.TypicalRestaurants;

public class JsonSerializableFoodTrailTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableFoodTrailTest");
    private static final Path TYPICAL_RESTAURANTS_FILE = TEST_DATA_FOLDER.resolve("typicalRestaurants.json");
    private static final Path INVALID_RESTAURANT_FILE = TEST_DATA_FOLDER.resolve("invalidRestaurant.json");
    private static final Path DUPLICATE_RESTAURANT_FILE = TEST_DATA_FOLDER
            .resolve("duplicateRestaurants.json");

    @Test
    public void toModelType_typicalRestaurantsFile_success() throws Exception {
        JsonSerializableFoodTrail dataFromFile = JsonUtil.readJsonFile(TYPICAL_RESTAURANTS_FILE,
                JsonSerializableFoodTrail.class).get();
        FoodTrail foodTrailFromFile = dataFromFile.toModelType();
        FoodTrail typicalRestaurantsFoodTrail = TypicalRestaurants.getTypicalFoodTrail();
        assertEquals(foodTrailFromFile, typicalRestaurantsFoodTrail);
    }

    @Test
    public void toModelType_invalidRestaurantFile_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodTrail dataFromFile = JsonUtil.readJsonFile(INVALID_RESTAURANT_FILE,
                JsonSerializableFoodTrail.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateRestaurants_throwsIllegalValueException() throws Exception {
        JsonSerializableFoodTrail dataFromFile = JsonUtil.readJsonFile(DUPLICATE_RESTAURANT_FILE,
                JsonSerializableFoodTrail.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableFoodTrail.MESSAGE_DUPLICATE_RESTAURANT,
                dataFromFile::toModelType);
    }

}
