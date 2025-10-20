package foodtrail.testutil;

import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_ADDRESS_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_NAME_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_JOLLIBEE;
import static foodtrail.logic.commands.CommandTestUtil.VALID_PHONE_KFC;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_FASTFOOD;
import static foodtrail.logic.commands.CommandTestUtil.VALID_TAG_HALAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import foodtrail.model.AddressBook;
import foodtrail.model.restaurant.Restaurant;

/**
 * A utility class containing a list of {@code Restaurant} objects to be used in tests.
 */
public class TypicalRestaurants {

    public static final Restaurant MCDONALDS = new RestaurantBuilder().withName("McDonald's")
            .withAddress("200 Victoria St, #01-49 Bugis Junction, Singapore 188021")
            .withPhone("67773777")
            .withTags("fastfood")
            .withRating(4).build();
    public static final Restaurant KOI = new RestaurantBuilder().withName("KOI Thé")
            .withAddress("53 Ang Mo Kio Ave 3, #B2-08 AMK Hub, Singapore 569933")
            .withPhone("64812345")
            .withTags("bubbletea", "drinks")
            .withRating(4).build();
    public static final Restaurant HAWKERCHAN = new RestaurantBuilder().withName("Hawker Chan")
            .withPhone("62190000")
            .withAddress("78 Smith St, Singapore 058972")
            .withRating(2).build();
    public static final Restaurant ASTONS = new RestaurantBuilder().withName("Astons Specialities")
            .withPhone("62351234")
            .withAddress("201 Victoria St, #04-06 Bugis+, Singapore 188067")
            .withTags("western")
            .withRating(0).build();
    public static final Restaurant POPEYES = new RestaurantBuilder().withName("Popeyes Louisiana Kitchen")
            .withPhone("65153456")
            .withAddress("3 Simei Street 6, #01-01 Eastpoint Mall, Singapore 528833")
            .withRating(3).build();
    public static final Restaurant SUBWAY = new RestaurantBuilder().withName("Subway")
            .withPhone("67890123")
            .withAddress("10 Tampines Central 1, #01-18 Tampines 1, Singapore 529536")
            .withRating(5).build();
    public static final Restaurant ANNAS = new RestaurantBuilder().withName("Anna's Curry")
            .withPhone("61234567")
            .withAddress("123 Serangoon Rd, Singapore 218227")
            .withRating(4).build();
    public static final Restaurant PIZZAHUT = new RestaurantBuilder().withName("Pizza Hut")
            .withPhone("62353535")
            .withAddress("18 Yishun Ave 9, #01-82 Junction 9, Singapore 768897")
            .withRating(2).build();
    public static final Restaurant MIXUE = new RestaurantBuilder().withName("Mixue")
            .withPhone("89232876")
            .withAddress("1 Bt Batok Central, #01-07, Singapore 658713")
            .withRating(3).build();

    // Manually added - Restaurant's details found in {@code CommandTestUtil}
    public static final Restaurant JOLLIBEE = new RestaurantBuilder().withName(VALID_NAME_JOLLIBEE)
            .withPhone(VALID_PHONE_JOLLIBEE)
            .withAddress(VALID_ADDRESS_JOLLIBEE)
            .withTags(VALID_TAG_HALAL)
            .withRating(4).build();
    public static final Restaurant KFC = new RestaurantBuilder().withName(VALID_NAME_KFC).withPhone(VALID_PHONE_KFC)
            .withAddress(VALID_ADDRESS_KFC).withTags(VALID_TAG_FASTFOOD, VALID_TAG_HALAL)
            .withRating(5).build();

    private TypicalRestaurants() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical restaurants.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Restaurant restaurant : getTypicalRestaurants()) {
            ab.addRestaurant(restaurant);
        }
        return ab;
    }

    public static List<Restaurant> getTypicalRestaurants() {
        return new ArrayList<>(Arrays.asList(MCDONALDS, KOI, HAWKERCHAN, ASTONS, POPEYES, SUBWAY, ANNAS));
    }
}
