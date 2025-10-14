package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_JOLLIBEE;
import static seedu.address.logic.commands.CommandTestUtil.DESC_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_KFC;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_JOLLIBEE);
        assertTrue(DESC_JOLLIBEE.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_JOLLIBEE.equals(DESC_JOLLIBEE));

        // null -> returns false
        assertFalse(DESC_JOLLIBEE.equals(null));

        // different types -> returns false
        assertFalse(DESC_JOLLIBEE.equals(5));

        // different values -> returns false
        assertFalse(DESC_JOLLIBEE.equals(DESC_KFC));

        // different name -> returns false
        EditPersonDescriptor editedJollibee = new EditPersonDescriptorBuilder(DESC_JOLLIBEE)
                .withName(VALID_NAME_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));

        // different phone -> returns false
        editedJollibee = new EditPersonDescriptorBuilder(DESC_JOLLIBEE).withPhone(VALID_PHONE_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));

        // different address -> returns false
        editedJollibee = new EditPersonDescriptorBuilder(DESC_JOLLIBEE).withAddress(VALID_ADDRESS_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
