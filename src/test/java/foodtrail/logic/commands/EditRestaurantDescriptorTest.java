package foodtrail.logic.commands;

import static foodtrail.logic.commands.CommandTestUtil.DESC_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.DESC_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.testutil.EditRestaurantDescriptorBuilder;

public class EditRestaurantDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditRestaurantDescriptor descriptorWithSameValues = new EditRestaurantDescriptor(DESC_JOLLIBEE);
        assertTrue(DESC_JOLLIBEE.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_JOLLIBEE.equals(DESC_JOLLIBEE));

        // null -> returns false
        assertFalse(DESC_JOLLIBEE.equals(null));

        // different types -> returns false
        assertFalse(DESC_JOLLIBEE.equals(new Object()));

        // different values -> returns false
        assertFalse(DESC_JOLLIBEE.equals(DESC_KFC));

        // different name -> returns false
        EditRestaurantDescriptor editedJollibee = new EditRestaurantDescriptorBuilder(DESC_JOLLIBEE)
                .withName(VALID_NAME_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));

        // different phone -> returns false
        editedJollibee = new EditRestaurantDescriptorBuilder(DESC_JOLLIBEE).withPhone(VALID_PHONE_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));

        // different address -> returns false
        editedJollibee = new EditRestaurantDescriptorBuilder(DESC_JOLLIBEE).withAddress(VALID_ADDRESS_KFC).build();
        assertFalse(DESC_JOLLIBEE.equals(editedJollibee));
    }

    @Test
    public void toStringMethod() {
        EditRestaurantDescriptor editRestaurantDescriptor = new EditRestaurantDescriptor();
        String expected = EditRestaurantDescriptor.class.getCanonicalName() + "{name="
                + editRestaurantDescriptor.getName().orElse(null) + ", phone="
                + editRestaurantDescriptor.getPhone().orElse(null) + ", address="
                + editRestaurantDescriptor.getAddress().orElse(null) + "}";
        assertEquals(expected, editRestaurantDescriptor.toString());
    }
}
