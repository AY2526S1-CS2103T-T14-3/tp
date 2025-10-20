package foodtrail.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showPersonAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static foodtrail.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.model.AddressBook;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.person.Person;
import foodtrail.model.person.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {

    private static final String TAG_STUB = "SomeTag";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addTagUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> newTags = new HashSet<>(firstPerson.getTags());
        newTags.add(new Tag(TAG_STUB));
        Person editedPerson = new Person(firstPerson.getName(), firstPerson.getPhone(),
                firstPerson.getAddress(), newTags);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, newTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addTagFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> newTags = new HashSet<>(personInFilteredList.getTags());
        newTags.add(new Tag(TAG_STUB));
        Person editedPerson = new Person(personInFilteredList.getName(), personInFilteredList.getPhone(),
                personInFilteredList.getAddress(), newTags);

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, newTags);

        String expectedMessage = String.format(TagCommand.MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, newTags);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> newTags = new HashSet<>();
        newTags.add(new Tag(TAG_STUB));
        final TagCommand standardCommand = new TagCommand(INDEX_FIRST_PERSON, newTags);

        // same values -> returns true
        TagCommand commandWithSameValues = new TagCommand(INDEX_FIRST_PERSON, newTags);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new TagCommand(INDEX_SECOND_PERSON, newTags)));

        // different tag -> returns false
        Set<Tag> diffTags = new HashSet<>();
        diffTags.add(new Tag("OtherTag"));
        assertFalse(standardCommand.equals(new TagCommand(INDEX_FIRST_PERSON, diffTags)));
    }
}
