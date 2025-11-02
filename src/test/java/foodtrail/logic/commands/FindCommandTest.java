package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalRestaurants.ANNAS;
import static foodtrail.testutil.TypicalRestaurants.ASTONS;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void equals() {
        RestaurantContainsKeywordsPredicate firstPredicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("first"));
        RestaurantContainsKeywordsPredicate secondPredicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate, "first");
        FindCommand findSecondCommand = new FindCommand(secondPredicate, "second");

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate, "first");
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noRestaurantFound() {
        String keyword = "zzzzzz";
        String expectedMessage = String.format(FindCommand.MESSAGE_FIND_SUCCESS, 0, keyword);
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList(keyword));
        FindCommand command = new FindCommand(predicate, keyword);
        expectedModel.updateFilteredRestaurantList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredRestaurantList());
    }

    @Test
    public void execute_singleKeyword_multipleRestaurantsFound() {
        String keyword = "on";
        String expectedMessage = String.format(FindCommand.MESSAGE_FIND_SUCCESS, 3, keyword);
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList(keyword));
        FindCommand command = new FindCommand(predicate, keyword);
        expectedModel.updateFilteredRestaurantList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(MCDONALDS, ASTONS, ANNAS), model.getFilteredRestaurantList());
    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Arrays.asList(keyword));
        FindCommand findCommand = new FindCommand(predicate, keyword);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate
                + ", keyword=" + keyword + "}";
        assertEquals(expected, findCommand.toString());
    }
}
