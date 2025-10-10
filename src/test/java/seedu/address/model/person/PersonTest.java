package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HALAL;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.KOI;
import static seedu.address.testutil.TypicalPersons.MCDONALDS;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(MCDONALDS.isSamePerson(MCDONALDS));

        // null -> returns false
        assertFalse(MCDONALDS.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedMcdonalds = new PersonBuilder(MCDONALDS).withPhone(VALID_PHONE_KFC)
                .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD).build();
        assertTrue(MCDONALDS.isSamePerson(editedMcdonalds));

        // different name, all other attributes same -> returns false
        editedMcdonalds = new PersonBuilder(MCDONALDS).withName(VALID_NAME_KFC).build();
        assertFalse(MCDONALDS.isSamePerson(editedMcdonalds));

        // name differs in case, all other attributes same -> returns false
        Person editedKoi = new PersonBuilder(KOI).withName(VALID_NAME_KFC.toLowerCase()).build();
        assertFalse(KOI.isSamePerson(editedKoi));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_KFC + " ";
        editedKoi = new PersonBuilder(KOI).withName(nameWithTrailingSpaces).build();
        assertFalse(KOI.isSamePerson(editedKoi));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person mcdonaldsCopy = new PersonBuilder(MCDONALDS).build();
        assertTrue(MCDONALDS.equals(mcdonaldsCopy));

        // same object -> returns true
        assertTrue(MCDONALDS.equals(MCDONALDS));

        // null -> returns false
        assertFalse(MCDONALDS.equals(null));

        // different type -> returns false
        assertFalse(MCDONALDS.equals(5));

        // different person -> returns false
        assertFalse(MCDONALDS.equals(KOI));

        // different name -> returns false
        Person editedMcdonalds = new PersonBuilder(MCDONALDS).withName(VALID_NAME_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different phone -> returns false
        editedMcdonalds = new PersonBuilder(MCDONALDS).withPhone(VALID_PHONE_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different address -> returns false
        editedMcdonalds = new PersonBuilder(MCDONALDS).withAddress(VALID_ADDRESS_KFC).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));

        // different tags -> returns false
        editedMcdonalds = new PersonBuilder(MCDONALDS).withTags(VALID_TAG_HALAL).build();
        assertFalse(MCDONALDS.equals(editedMcdonalds));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + MCDONALDS.getName()
                + ", phone=" + MCDONALDS.getPhone()
                + ", address=" + MCDONALDS.getAddress() + ", tags=" + MCDONALDS.getTags() + "}";
        assertEquals(expected, MCDONALDS.toString());
    }
}
