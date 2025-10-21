package foodtrail.model;

import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.KOI;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.GuiSettings;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;
import foodtrail.testutil.RestaurantDirectoryBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new RestaurantDirectory(), new RestaurantDirectory(modelManager.getRestaurantDirectory()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setRestaurantDirectoryFilePath(Paths.get("restaurant/directory/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setRestaurantDirectoryFilePath(Paths.get("new/restaurant/directory/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setRestaurantDirectoryFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setRestaurantDirectoryFilePath(null));
    }

    @Test
    public void setRestaurantDirectoryFilePath_validPath_setsRestaurantDirectoryFilePath() {
        Path path = Paths.get("restaurant/directory/file/path");
        modelManager.setRestaurantDirectoryFilePath(path);
        assertEquals(path, modelManager.getRestaurantDirectoryFilePath());
    }

    @Test
    public void hasRestaurant_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasRestaurant(null));
    }

    @Test
    public void hasRestaurant_restaurantNotInRestaurantDirectory_returnsFalse() {
        assertFalse(modelManager.hasRestaurant(MCDONALDS));
    }

    @Test
    public void hasRestaurant_restaurantInRestaurantDirectory_returnsTrue() {
        modelManager.addRestaurant(MCDONALDS);
        assertTrue(modelManager.hasRestaurant(MCDONALDS));
    }

    @Test
    public void getFilteredRestaurantList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredRestaurantList().remove(0));
    }

    @Test
    public void equals() {
        RestaurantDirectory restaurantDirectory =
                new RestaurantDirectoryBuilder().withRestaurant(MCDONALDS).withRestaurant(KOI).build();
        RestaurantDirectory differentRestaurantDirectory = new RestaurantDirectory();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(restaurantDirectory, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(restaurantDirectory, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different restaurantDirectory -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentRestaurantDirectory, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = MCDONALDS.getName().fullName.split("\\s+");
        modelManager.updateFilteredRestaurantList(new RestaurantContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(restaurantDirectory, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setRestaurantDirectoryFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(restaurantDirectory, differentUserPrefs)));
    }
}
