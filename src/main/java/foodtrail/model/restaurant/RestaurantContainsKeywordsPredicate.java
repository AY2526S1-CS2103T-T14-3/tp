package foodtrail.model.restaurant;

import java.util.List;
import java.util.function.Predicate;

import foodtrail.commons.util.StringUtil;
import foodtrail.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Restaurant}'s attributes matches any of the keywords given.
 */
public class RestaurantContainsKeywordsPredicate implements Predicate<Restaurant> {
    private final List<String> keywords;

    public RestaurantContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Restaurant restaurant) {
        return keywords.stream()
                .anyMatch(keyword ->
                        StringUtil.containsSubstringIgnoreCase(restaurant.getName().fullName, keyword)
                        || StringUtil.containsSubstringIgnoreCase(restaurant.getPhone().value, keyword)
                        || StringUtil.containsSubstringIgnoreCase(restaurant.getAddress().value, keyword)
                        || restaurant.getTags().stream().anyMatch(tag -> StringUtil
                                .containsSubstringIgnoreCase(tag.tagName, keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RestaurantContainsKeywordsPredicate)) {
            return false;
        }

        RestaurantContainsKeywordsPredicate otherRestaurantContainsKeywordsPredicate =
                (RestaurantContainsKeywordsPredicate) other;
        return keywords.equals(otherRestaurantContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
