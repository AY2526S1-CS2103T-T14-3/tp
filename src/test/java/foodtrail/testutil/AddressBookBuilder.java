package foodtrail.testutil;

import foodtrail.model.AddressBook;
import foodtrail.model.restaurant.Restaurant;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withRestaurant("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Restaurant} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withRestaurant(Restaurant restaurant) {
        addressBook.addRestaurant(restaurant);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
