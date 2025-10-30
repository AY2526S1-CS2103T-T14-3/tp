package foodtrail.model.restaurant;

import static foodtrail.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
        // invalid tag name
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName("abc def ghi jkl mno pqrs tuv we")); // 31 characters

        // valid tag names
        assertTrue(Tag.isValidTagName("halal"));
        assertTrue(Tag.isValidTagName("abc def ghi jkl mno pqrs tuv w")); // 30 characters (spaces)
        assertTrue(Tag.isValidTagName("abcedefeghiejklemnoepqrsetuvew")); // 30 characters (no spaces)
    }

    @Test
    public void equals() {
        Tag tag = new Tag("fastfood");

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // same values -> returns true
        Tag tagCopy = new Tag("fastfood");
        assertTrue(tag.equals(tagCopy));

        // different types -> returns false
        assertFalse(tag.equals(new Object()));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different tag -> returns false
        Tag differentTag = new Tag("western");
        assertFalse(tag.equals(differentTag));
    }

}
