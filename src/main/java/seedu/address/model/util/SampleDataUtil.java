package seedu.address.model.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Rating;
import seedu.address.model.person.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("McDonald's"), new Phone("67773777"),
                new Address("200 Victoria St, #01-49 Bugis Junction, Singapore 188021"),
                getTagSet("fastfood"),
                Optional.empty()),
            new Person(new Name("KOI Thé"), new Phone("64812345"),
                new Address("53 Ang Mo Kio Ave 3, #B2-08 AMK Hub, Singapore 569933"),
                getTagSet("bubbletea", "drinks"),
                Optional.of(new Rating(4))),
            new Person(new Name("Hawker Chan"), new Phone("62190000"),
                new Address("78 Smith St, Singapore 058972"),
                getTagSet(),
                Optional.empty()),
            new Person(new Name("Astons Specialities"), new Phone("62351234"),
                new Address("201 Victoria St, #04-06 Bugis+, Singapore 188067"),
                getTagSet("western"),
                Optional.empty()),
            new Person(new Name("Popeyes Louisiana Kitchen"), new Phone("65153456"),
                new Address("3 Simei Street 6, #01-01 Eastpoint Mall, Singapore 528833"),
                getTagSet(),
                Optional.empty()),
            new Person(new Name("Subway"), new Phone("67890123"),
                new Address("10 Tampines Central 1, #01-18 Tampines 1, Singapore 529536"),
                getTagSet(),
                Optional.empty()),
            new Person(new Name("Anna's Curry"), new Phone("61234567"),
                new Address("123 Serangoon Rd, Singapore 218227"),
                getTagSet(),
                Optional.empty())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
