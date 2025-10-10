package seedu.address.testutil;

import seedu.address.model.FoodTrail;
import seedu.address.model.restaurant.Restaurant;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code FoodTrail ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class FoodTrailBuilder {

    private FoodTrail foodTrail;

    public FoodTrailBuilder() {
        foodTrail = new FoodTrail();
    }

    public FoodTrailBuilder(FoodTrail addressBook) {
        this.foodTrail = addressBook;
    }

    /**
     * Adds a new {@code Restaurant} to the {@code FoodTrail} that we are building.
     */
    public FoodTrailBuilder withRestaurant(Restaurant restaurant) {
        foodTrail.addRestaurant(restaurant);
        return this;
    }

    public FoodTrail build() {
        return foodTrail;
    }
}
