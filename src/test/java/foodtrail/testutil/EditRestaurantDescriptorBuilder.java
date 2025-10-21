package foodtrail.testutil;

import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Restaurant;

/**
 * A utility class to help with building EditRestaurantDescriptor objects.
 */
public class EditRestaurantDescriptorBuilder {

    private EditRestaurantDescriptor descriptor;

    public EditRestaurantDescriptorBuilder() {
        descriptor = new EditRestaurantDescriptor();
    }

    public EditRestaurantDescriptorBuilder(EditRestaurantDescriptor descriptor) {
        this.descriptor = new EditRestaurantDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditRestaurantDescriptor} with fields containing {@code restaurant}'s details
     */
    public EditRestaurantDescriptorBuilder(Restaurant restaurant) {
        descriptor = new EditRestaurantDescriptor();
        descriptor.setName(restaurant.getName());
        descriptor.setPhone(restaurant.getPhone());
        descriptor.setAddress(restaurant.getAddress());
    }

    /**
     * Sets the {@code Name} of the {@code EditRestaurantDescriptor} that we are building.
     */
    public EditRestaurantDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditRestaurantDescriptor} that we are building.
     */
    public EditRestaurantDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditRestaurantDescriptor} that we are building.
     */
    public EditRestaurantDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    public EditRestaurantDescriptor build() {
        return descriptor;
    }
}
