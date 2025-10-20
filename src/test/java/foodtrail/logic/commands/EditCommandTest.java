package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodtrail.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodtrail.logic.commands.CommandTestUtil.showPersonAtIndex;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static foodtrail.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static foodtrail.testutil.TypicalPersons.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.index.Index;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.model.AddressBook;
import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.EditRestaurantDescriptorBuilder;
import foodtrail.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Restaurant restaurantToEdit = model.getFilteredRestaurantList().get(INDEX_FIRST_PERSON.getZeroBased());
        Restaurant editedRestaurant = new PersonBuilder().build();
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder(editedRestaurant).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        Restaurant finalExpectedRestaurant = new Restaurant(editedRestaurant.getName(), editedRestaurant.getPhone(),
                editedRestaurant.getAddress(), restaurantToEdit.getTags());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(finalExpectedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setRestaurant(restaurantToEdit, finalExpectedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredRestaurantList().size());
        Restaurant lastRestaurant = model.getFilteredRestaurantList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastRestaurant);
        Restaurant editedRestaurant = personInList.withName(VALID_NAME_KFC).withPhone(VALID_PHONE_KFC).build();

        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC)
                .withPhone(VALID_PHONE_KFC).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setRestaurant(lastRestaurant, editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditRestaurantDescriptor());
        Restaurant editedRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Restaurant restaurantInFilteredList = model.getFilteredRestaurantList().get(INDEX_FIRST_PERSON.getZeroBased());
        Restaurant editedRestaurant = new PersonBuilder(restaurantInFilteredList).withName(VALID_NAME_KFC).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedRestaurant));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setRestaurant(model.getFilteredRestaurantList().get(0), editedRestaurant);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Restaurant firstRestaurant = model.getFilteredRestaurantList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder(firstRestaurant).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Restaurant restaurantInList = model.getAddressBook().getRestaurantList()
                .get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditRestaurantDescriptorBuilder(restaurantInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRestaurantList().size() + 1);
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getRestaurantList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditRestaurantDescriptorBuilder().withName(VALID_NAME_KFC).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_JOLLIBEE);

        // same values -> returns true
        EditRestaurantDescriptor copyDescriptor = new EditRestaurantDescriptor(DESC_JOLLIBEE);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_JOLLIBEE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_KFC)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditRestaurantDescriptor editRestaurantDescriptor = new EditRestaurantDescriptor();
        EditCommand editCommand = new EditCommand(index, editRestaurantDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editRestaurantDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
