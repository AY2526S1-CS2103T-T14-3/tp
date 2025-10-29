package foodtrail.logic.commands;

import static foodtrail.logic.Messages.MESSAGE_RESTAURANTS_LISTED_OVERVIEW;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.testutil.TypicalRestaurants.HAWKERCHAN;
import static foodtrail.testutil.TypicalRestaurants.KOI;
import static foodtrail.testutil.TypicalRestaurants.MCDONALDS;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
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
        String expectedMessage = String.format(MESSAGE_RESTAURANTS_LISTED_OVERVIEW, 0);
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredRestaurantList(predicate);
        expectedModel.sortRestaurantListByName();
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredRestaurantList());
    }

    @Test
    public void execute_multipleKeywords_multipleRestaurantsFound() {
        String expectedMessage = String.format(MESSAGE_RESTAURANTS_LISTED_OVERVIEW, 3);
        RestaurantContainsKeywordsPredicate predicate = preparePredicate("McDonald's, KOI, Hawker");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredRestaurantList(predicate);
        expectedModel.sortRestaurantListByName();
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(HAWKERCHAN, KOI, MCDONALDS), model.getFilteredRestaurantList());
    }

    @Test
    public void toStringMethod() {
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code RestaurantContainsKeywordsPredicate}.
     */
    private RestaurantContainsKeywordsPredicate preparePredicate(String userInput) {
        List<String> keywords = Arrays.stream(userInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return new RestaurantContainsKeywordsPredicate(keywords);
    }
}
