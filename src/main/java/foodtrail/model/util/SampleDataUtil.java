package foodtrail.model.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Contains utility methods for populating {@code RestaurantDirectory} with sample data.
 */
public class SampleDataUtil {
    public static Restaurant[] getSampleRestaurants() {
        return new Restaurant[] {
            new Restaurant(new Name("Anna's Curry"), new Phone("61234567"),
                    new Address("123 Serangoon Rd, Singapore 218227"),
                    getTagSet(),
                    Optional.empty()),
            new Restaurant(new Name("Astons Specialities"), new Phone("62351234"),
                    new Address("201 Victoria St, #04-06 Bugis+, Singapore 188067"),
                    getTagSet("western"),
                    Optional.empty()),
            new Restaurant(new Name("Hawker Chan"), new Phone("62190000"),
                    new Address("78 Smith St, Singapore 058972"),
                    getTagSet(),
                    Optional.empty()),
            new Restaurant(new Name("KOI Thé"), new Phone("64812345"),
                    new Address("53 Ang Mo Kio Ave 3, #B2-08 AMK Hub, Singapore 569933"),
                    getTagSet("bubble tea", "drinks"),
                    Optional.of(new Rating(4))),
            new Restaurant(new Name("McDonald's"), new Phone("67773777"),
                    new Address("200 Victoria St, #01-49 Bugis Junction, Singapore 188021"),
                    getTagSet("fast food"),
                    Optional.empty()),
            new Restaurant(new Name("Popeyes Louisiana Kitchen"), new Phone("65153456"),
                    new Address("3 Simei Street 6, #01-01 Eastpoint Mall, Singapore 528833"),
                    getTagSet(),
                    Optional.empty()),
            new Restaurant(new Name("Subway"), new Phone("67890123"),
                    new Address("10 Tampines Central 1, #01-18 Tampines 1, Singapore 529536"),
                    getTagSet(),
                    Optional.empty())
        };
    }

    public static ReadOnlyRestaurantDirectory getSampleRestaurantDirectory() {
        RestaurantDirectory sampleAb = new RestaurantDirectory();
        for (Restaurant sampleRestaurant : getSampleRestaurants()) {
            sampleAb.addRestaurant(sampleRestaurant);
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
