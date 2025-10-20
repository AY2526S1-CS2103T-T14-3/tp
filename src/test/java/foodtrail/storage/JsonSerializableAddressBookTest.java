package foodtrail.storage;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import foodtrail.commons.exceptions.IllegalValueException;
import foodtrail.commons.util.JsonUtil;
import foodtrail.model.AddressBook;
import foodtrail.testutil.TypicalRestaurants;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_RESTAURANTS_FILE =
            TEST_DATA_FOLDER.resolve("typicalRestaurantsAddressBook.json");
    private static final Path INVALID_RESTAURANT_FILE =
            TEST_DATA_FOLDER.resolve("invalidRestaurantAddressBook.json");
    private static final Path DUPLICATE_RESTAURANT_FILE =
            TEST_DATA_FOLDER.resolve("duplicateRestaurantAddressBook.json");

    @Test
    public void toModelType_typicalRestaurantsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_RESTAURANTS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalRestaurantsAddressBook = TypicalRestaurants.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalRestaurantsAddressBook);
    }

    @Test
    public void toModelType_invalidRestaurantFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_RESTAURANT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateRestaurants_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_RESTAURANT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_RESTAURANT,
                dataFromFile::toModelType);
    }

}
