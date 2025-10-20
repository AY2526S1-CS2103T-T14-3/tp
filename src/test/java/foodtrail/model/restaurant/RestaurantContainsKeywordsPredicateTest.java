package foodtrail.model.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import foodtrail.testutil.RestaurantBuilder;

public class RestaurantContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        RestaurantContainsKeywordsPredicate firstPredicate =
                new RestaurantContainsKeywordsPredicate(firstPredicateKeywordList);
        RestaurantContainsKeywordsPredicate secondPredicate =
                new RestaurantContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        RestaurantContainsKeywordsPredicate firstPredicateCopy =
                new RestaurantContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different restaurant -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("Burger"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Burger King").build()));

        // Multiple keywords
        predicate = new RestaurantContainsKeywordsPredicate(Arrays.asList("Burger", "King"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Burger King").build()));

        // Only one matching keyword
        predicate = new RestaurantContainsKeywordsPredicate(Arrays.asList("King", "Chan"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Hawker Chan").build()));

        // Mixed-case keywords
        predicate = new RestaurantContainsKeywordsPredicate(Arrays.asList("bUrGeR", "kInG"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Burger King").build()));

        // Partial keyword match
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("Burg"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Burger King").build()));
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("96437852"));
        assertTrue(predicate.test(new RestaurantBuilder().withPhone("96437852").build()));

        // Partial keyword match
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("7852"));
        assertTrue(predicate.test(new RestaurantBuilder().withPhone("96437852").build()));
    }

    @Test
    public void test_addressContainsKeywords_returnsTrue() {
        // One keyword
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("Orchard"));
        assertTrue(predicate.test(new RestaurantBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));

        // Partial keyword match
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("ard"));
        assertTrue(predicate.test(new RestaurantBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));

        // Mixed-case keywords
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("OrCHard"));
        assertTrue(predicate.test(new RestaurantBuilder()
                .withAddress("68 Orchard Rd, #04-01 20 Plaza, Singapore 238839").build()));
    }

    @Test
    public void test_tagsContainsKeywords_returnsTrue() {
        // One keyword
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("western"));
        assertTrue(predicate.test(new RestaurantBuilder().withTags("western", "fastfood").build()));

        // Partial keyword match
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("west"));
        assertTrue(predicate.test(new RestaurantBuilder().withTags("western").build()));

        // Mixed-case keywords
        predicate = new RestaurantContainsKeywordsPredicate(Collections.singletonList("wEsTeRn"));
        assertTrue(predicate.test(new RestaurantBuilder().withTags("western").build()));
    }


    @Test
    public void test_keywordMatchesMultipleAttributes_returnsTrue() {
        // Keyword matches name and address
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.singletonList("Burger"));
        assertTrue(predicate.test(new RestaurantBuilder().withName("Burger King")
                .withAddress("68 Orchard Rd, #B1-11 Plaza Singapura, Singapore 238839").build()));
    }


    @Test
    public void test_doesNotContainKeywords_returnsFalse() {
        // Zero keywords
        RestaurantContainsKeywordsPredicate predicate =
                new RestaurantContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new RestaurantBuilder().withName("McDonalds").build()));

        // Non-matching keyword for all attributes
        predicate = new RestaurantContainsKeywordsPredicate(Arrays.asList("Pizza"));
        assertFalse(predicate.test(new RestaurantBuilder().withName("Burger King").withPhone("85633335")
                .withAddress("200 Victoria St, Singapore 188021").withTags("fastfood").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        RestaurantContainsKeywordsPredicate predicate = new RestaurantContainsKeywordsPredicate(keywords);

        String expected = RestaurantContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
