package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private static final String TAG_STUB = "someTag";
    private static final String ANOTHER_TAG_STUB = "anotherTag";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_untagPerson_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> newTags = new HashSet<>(firstPerson.getTags());
        Set<Tag> tagsToRemove = Collections.singleton(newTags.stream().findFirst().orElse(new Tag(TAG_STUB)));
        newTags.removeAll(tagsToRemove);

        Person editedPerson = new PersonBuilder(firstPerson).withTags(newTags.stream()
                .map(t -> t.tagName).toArray(String[]::new)).build();

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_SUCCESS,
                Messages.format(editedPerson), tagsToRemove);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAddressBook(model.getAddressBook());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonExistentTag_throwsCommandException() {
        Set<Tag> nonExistentTags = new HashSet<>();
        nonExistentTags.add(new Tag("nonExistentTag"));
        String nonExistentTagString = nonExistentTags.stream().map(Tag::toString)
                .collect(Collectors.joining(", "));
        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, nonExistentTags);

        assertCommandFailure(untagCommand, model, UntagCommand.MESSAGE_TAG_NOT_FOUND + nonExistentTagString);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(TAG_STUB));
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagList);

        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(TAG_STUB));
        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagList);
        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(TAG_STUB));
        Set<Tag> anotherTagList = new HashSet<>();
        anotherTagList.add(new Tag(ANOTHER_TAG_STUB));
        final UntagCommand standardCommand = new UntagCommand(INDEX_FIRST_PERSON, tagList);

        // same values -> returns true
        UntagCommand commandWithSameValues = new UntagCommand(INDEX_FIRST_PERSON, tagList);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new UntagCommand(INDEX_SECOND_PERSON, tagList)));

        // different tag -> returns false
        assertFalse(standardCommand.equals(new UntagCommand(INDEX_FIRST_PERSON, anotherTagList)));
    }
}
