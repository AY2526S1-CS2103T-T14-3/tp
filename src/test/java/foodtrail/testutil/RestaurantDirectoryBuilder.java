package foodtrail.testutil;

import foodtrail.model.RestaurantDirectory;
import foodtrail.model.restaurant.Restaurant;

/**
 * A utility class to help with building RestaurantDirectory objects.
 * Example usage: <br>
 *     {@code RestaurantDirectory ab = new RestaurantDirectoryBuilder().withRestaurant("KFC").build();}
 */
public class RestaurantDirectoryBuilder {

    private RestaurantDirectory restaurantDirectory;

    public RestaurantDirectoryBuilder() {
        restaurantDirectory = new RestaurantDirectory();
    }

    public RestaurantDirectoryBuilder(RestaurantDirectory restaurantDirectory) {
        this.restaurantDirectory = restaurantDirectory;
    }

    /**
     * Adds a new {@code Restaurant} to the {@code RestaurantDirectory} that we are building.
     */
    public RestaurantDirectoryBuilder withRestaurant(Restaurant restaurant) {
        restaurantDirectory.addRestaurant(restaurant);
        return this;
    }

    public RestaurantDirectory build() {
        return restaurantDirectory;
    }
}
