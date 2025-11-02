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
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private static final String TAG_STUB = "someTag";
    private static final String ANOTHER_TAG_STUB = "anotherTag";
    private static final Tag TEST_TAG = new Tag("testtag"); // Tag to ensure presence for removal test

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_untagRestaurant_success() {
        // Setup: Get a restaurant and a tag that is known to exist.
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());

        // Create a version of the restaurant that is guaranteed to have TEST_TAG
        Set<Tag> tagsForTestRestaurant = new LinkedHashSet<>(firstRestaurant.getTags());
        tagsForTestRestaurant.add(TEST_TAG); // Ensure TEST_TAG is present
        Restaurant restaurantWithTestTag = new Restaurant(firstRestaurant.getName(), firstRestaurant.getPhone(),
                firstRestaurant.getAddress(), tagsForTestRestaurant,
                firstRestaurant.getRating(), firstRestaurant.getIsMarked());

        // Temporarily update the model to contain restaurantWithTestTag
        // This ensures the command operates on a restaurant that has the tag we want to remove
        model.setRestaurant(firstRestaurant, restaurantWithTestTag);

        Set<Tag> tagsToRemove = Collections.singleton(TEST_TAG);

        // Create the expected state after the command executes.
        Set<Tag> expectedTagsAfterRemoval = new LinkedHashSet<>(restaurantWithTestTag.getTags());
        expectedTagsAfterRemoval.remove(TEST_TAG);
        Restaurant editedRestaurant = new Restaurant(restaurantWithTestTag.getName(), restaurantWithTestTag.getPhone(),
                restaurantWithTestTag.getAddress(), expectedTagsAfterRemoval,
                restaurantWithTestTag.getRating(), restaurantWithTestTag.getIsMarked());

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_RESTAURANT, tagsToRemove);

        // Build the expected success message according to the new format.
        String tagsRemovedString = tagsToRemove.stream()
                .map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));

        // Replicate the logic from UntagCommand to build the details string conditionally.
        StringBuilder detailsBuilder = new StringBuilder(); // Use editedRestaurant for details
        detailsBuilder.append("Name: ").append(editedRestaurant.getName()).append("\n");
        detailsBuilder.append("Phone: ").append(editedRestaurant.getPhone()).append("\n");
        detailsBuilder.append("Address: ").append(editedRestaurant.getAddress());
        if (!editedRestaurant.getTags().isEmpty()) {
            String tagsString = editedRestaurant.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(t -> t.tagName)
                    .collect(Collectors.joining(", "));
            detailsBuilder.append("\nTags: ").append(tagsString);
        }
        String restaurantDetails = detailsBuilder.toString();
        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_SUCCESS, tagsRemovedString,
                restaurantDetails);

        Model expectedModel = new ModelManager(new RestaurantDirectory(getTypicalRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurant(firstRestaurant, restaurantWithTestTag); // First, update to restaurantWithTestTag
        expectedModel.setRestaurant(restaurantWithTestTag, editedRestaurant); // Then, update to editedRestaurant

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonExistentTag_throwsCommandException() {
        Set<Tag> nonExistentTags = new LinkedHashSet<>();
        nonExistentTags.add(new Tag("nonExistentTag"));
        String nonExistentTagString = nonExistentTags.stream().map(t -> "'" + t.tagName + "'")
                .collect(Collectors.joining(", "));
        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_RESTAURANT, nonExistentTags);

        assertCommandFailure(untagCommand, model, String.format(UntagCommand.MESSAGE_TAG_NOT_FOUND,
                nonExistentTagString));
    }

    @Test
    public void execute_invalidRestaurantIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        Set<Tag> tagList = new LinkedHashSet<>();
        tagList.add(new Tag(TAG_STUB));
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagList);

        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of restaurant directory
     */
    @Test
    public void execute_invalidRestaurantIndexFilteredList_failure() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        Index outOfBoundIndex = INDEX_SECOND_RESTAURANT;
        // ensures that outOfBoundIndex is still in bounds of restaurant directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRestaurantDirectory().getRestaurantList().size());

        Set<Tag> tagList = new LinkedHashSet<>();
        tagList.add(new Tag(TAG_STUB));
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagList);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tagList = new LinkedHashSet<>();
        tagList.add(new Tag(TAG_STUB));
        Set<Tag> anotherTagList = new LinkedHashSet<>();
        anotherTagList.add(new Tag(ANOTHER_TAG_STUB));
        final UntagCommand standardCommand = new UntagCommand(INDEX_FIRST_RESTAURANT, tagList);

        // same values -> returns true
        UntagCommand commandWithSameValues = new UntagCommand(INDEX_FIRST_RESTAURANT, tagList);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new UntagCommand(INDEX_SECOND_RESTAURANT, tagList)));

        // different tag -> returns false
        assertFalse(standardCommand.equals(new UntagCommand(INDEX_FIRST_RESTAURANT, anotherTagList)));
    }
}
