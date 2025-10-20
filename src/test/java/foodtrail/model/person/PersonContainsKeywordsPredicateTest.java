package foodtrail.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import foodtrail.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Burger"));
        assertTrue(predicate.test(new PersonBuilder().withName("Burger King").build()));

        // Multiple keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Burger", "King"));
        assertTrue(predicate.test(new PersonBuilder().withName("Burger King").build()));

        // Only one matching keyword
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("King", "Chan"));
        assertTrue(predicate.test(new PersonBuilder().withName("Hawker Chan").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("bUrGeR", "kInG"));
        assertTrue(predicate.test(new PersonBuilder().withName("Burger King").build()));

        // Partial keyword match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Burg"));
        assertTrue(predicate.test(new PersonBuilder().withName("Burger King").build()));
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("96437852"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("96437852").build()));

        // Partial keyword match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("7852"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("96437852").build()));
    }

    @Test
    public void test_addressContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Orchard"));
        assertTrue(predicate.test(new PersonBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));

        // Partial keyword match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("ard"));
        assertTrue(predicate.test(new PersonBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("OrCHard"));
        assertTrue(predicate.test(new PersonBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));
    }

    @Test
    public void test_tagsContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("western"));
        assertTrue(predicate.test(new PersonBuilder().withTags("western", "fastfood").build()));

        // Partial keyword match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("west"));
        assertTrue(predicate.test(new PersonBuilder().withTags("western").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("wEsTeRn"));
        assertTrue(predicate.test(new PersonBuilder().withTags("western").build()));
    }


    @Test
    public void test_keywordMatchesMultipleAttributes_returnsTrue() {
        // Keyword matches name and address
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Burger"));
        assertTrue(predicate.test(new PersonBuilder().withName("Burger King")
                .withAddress("68 Orchard Rd, #B1-11 Plaza Singapura, Singapore 238839").build()));
    }


    @Test
    public void test_doesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("McDonalds").build()));

        // Non-matching keyword for all attributes
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Pizza"));
        assertFalse(predicate.test(new PersonBuilder().withName("Burger King").withPhone("85633335")
                .withAddress("200 Victoria St, Singapore 188021").withTags("fastfood").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
