package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static foodtrail.testutil.TypicalRestaurants.getTypicalRestaurantDirectory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {

    private static final String TAG_STUB = "SomeTag";

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_addTagUnfilteredList_success() {
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Set<Tag> tagsToAdd = Collections.singleton(new Tag(TAG_STUB));

        Set<Tag> allTags = new LinkedHashSet<>(firstRestaurant.getTags());
        allTags.addAll(tagsToAdd);

        Restaurant editedRestaurant = new Restaurant(firstRestaurant.getName(), firstRestaurant.getPhone(),
                firstRestaurant.getAddress(), allTags, firstRestaurant.getRating(), firstRestaurant.getIsMarked());

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_RESTAURANT, tagsToAdd);

        String tagsAddedString = tagsToAdd.stream()
                .map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));
        String restaurantDetails = "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream().sorted(Comparator.comparing(t -> t.tagName))
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));
        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsAddedString, restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurant(firstRestaurant, editedRestaurant);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addTagFilteredList_success() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);

        Restaurant restaurantInFilteredList = model.getFilteredRestaurantList()
                .get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Set<Tag> tagsToAdd = Collections.singleton(new Tag(TAG_STUB));

        Set<Tag> allTags = new LinkedHashSet<>(restaurantInFilteredList.getTags());
        allTags.addAll(tagsToAdd);

        Restaurant editedRestaurant = new Restaurant(restaurantInFilteredList.getName(),
                restaurantInFilteredList.getPhone(),
                restaurantInFilteredList.getAddress(), allTags,
                restaurantInFilteredList.getRating(), restaurantInFilteredList.getIsMarked());

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_RESTAURANT, tagsToAdd);

        String tagsAddedString = tagsToAdd.stream()
                .map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));
        String restaurantDetails = "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream().sorted(Comparator.comparing(t -> t.tagName))
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));
        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, tagsAddedString, restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        showRestaurantAtIndex(expectedModel, INDEX_FIRST_RESTAURANT);
        Restaurant restaurantToEditInExpectedModel = expectedModel.getFilteredRestaurantList().get(0);
        expectedModel.setRestaurant(restaurantToEditInExpectedModel, editedRestaurant);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidRestaurantIndexUnfilteredList_failure() {
        Set<Tag> newTags = new LinkedHashSet<>();
        newTags.add(new Tag(TAG_STUB));
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of restaurant directory
     */
    @Test
    public void execute_invalidRestaurantIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        Set<Tag> newTags = new LinkedHashSet<>();
        newTags.add(new Tag(TAG_STUB));
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() {
        // Get a restaurant that has at least one tag
        Restaurant restaurantWithTags = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        assertTrue(!restaurantWithTags.getTags().isEmpty(), "Test requires the restaurant to have at least one tag.");

        // Get an existing tag to try and add again
        Tag existingTag = restaurantWithTags.getTags().iterator().next();
        Set<Tag> duplicateTagSet = Collections.singleton(existingTag);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_RESTAURANT, duplicateTagSet);
        String expectedMessage = String.format(TagCommand.MESSAGE_DUPLICATE_TAG, "'" + existingTag.tagName + "'");
        assertCommandFailure(tagCommand, model, expectedMessage);
    }


    @Test
    public void equals() {
        Set<Tag> newTags = new LinkedHashSet<>();
        newTags.add(new Tag(TAG_STUB));
        final TagCommand standardCommand = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);

        // same values -> returns true
        TagCommand commandWithSameValues = new TagCommand(INDEX_FIRST_RESTAURANT, newTags);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new Object()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new TagCommand(INDEX_SECOND_RESTAURANT, newTags)));

        // different tag -> returns false
        Set<Tag> diffTags = new LinkedHashSet<>();
        diffTags.add(new Tag("OtherTag"));
        assertFalse(standardCommand.equals(new TagCommand(INDEX_FIRST_RESTAURANT, diffTags)));
    }
}
