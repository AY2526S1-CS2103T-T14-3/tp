package foodtrail.model.restaurant.exceptions;

/**
 * Signals that the operation is unable to find the specified Restaurant.
 */
public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Restaurant not found");
    }
}
