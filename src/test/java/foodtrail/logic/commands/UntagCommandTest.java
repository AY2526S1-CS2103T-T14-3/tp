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
import java.util.HashSet;
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
import foodtrail.testutil.RestaurantBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private static final String TAG_STUB = "someTag";
    private static final String ANOTHER_TAG_STUB = "anotherTag";

    private Model model = new ModelManager(getTypicalRestaurantDirectory(), new UserPrefs());

    @Test
    public void execute_untagRestaurant_success() {
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased());
        Set<Tag> newTags = new HashSet<>(firstRestaurant.getTags());
        Set<Tag> tagsToRemove = Collections.singleton(newTags.stream().findFirst().orElse(new Tag(TAG_STUB)));
        newTags.removeAll(tagsToRemove);

        Restaurant editedRestaurant = new RestaurantBuilder(firstRestaurant).withTags(newTags.stream()
                .map(t -> t.tagName).toArray(String[]::new)).build();

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_RESTAURANT, tagsToRemove);

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_SUCCESS,
                Messages.format(editedRestaurant), tagsToRemove);

        Model expectedModel = new ModelManager(new RestaurantDirectory(model.getRestaurantDirectory()),
                new UserPrefs());
        expectedModel.setRestaurantDirectory(model.getRestaurantDirectory());
        expectedModel.setRestaurant(firstRestaurant, editedRestaurant);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonExistentTag_throwsCommandException() {
        Set<Tag> nonExistentTags = new HashSet<>();
        nonExistentTags.add(new Tag("nonExistentTag"));
        String nonExistentTagString = nonExistentTags.stream().map(Tag::toString)
                .collect(Collectors.joining(", "));
        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_RESTAURANT, nonExistentTags);

        assertCommandFailure(untagCommand, model, UntagCommand.MESSAGE_TAG_NOT_FOUND
                + nonExistentTagString);
    }

    @Test
    public void execute_invalidRestaurantIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        Set<Tag> tagList = new HashSet<>();
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

        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(TAG_STUB));
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagList);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(TAG_STUB));
        Set<Tag> anotherTagList = new HashSet<>();
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
