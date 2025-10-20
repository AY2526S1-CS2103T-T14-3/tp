package foodtrail.model.person;

import java.util.List;
import java.util.function.Predicate;

import foodtrail.commons.util.StringUtil;
import foodtrail.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s attributes matches any of the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsSubstringIgnoreCase(person.getName().fullName, keyword)
                || StringUtil.containsSubstringIgnoreCase(person.getPhone().value, keyword)
                || StringUtil.containsSubstringIgnoreCase(person.getAddress().value, keyword)
                || person.getTags().stream().anyMatch(tag -> StringUtil
                        .containsSubstringIgnoreCase(tag.tagName, keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPersonContainsKeywordsPredicate = (PersonContainsKeywordsPredicate) other;
        return keywords.equals(otherPersonContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
