package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditRestaurantDescriptor;
import seedu.address.model.restaurant.Address;
import seedu.address.model.restaurant.Email;
import seedu.address.model.restaurant.Name;
import seedu.address.model.restaurant.Phone;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditRestaurantDescriptionBuilder {

    private EditRestaurantDescriptor descriptor;

    public EditRestaurantDescriptionBuilder() {
        descriptor = new EditRestaurantDescriptor();
    }

    public EditRestaurantDescriptionBuilder(EditRestaurantDescriptor descriptor) {
        this.descriptor = new EditRestaurantDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code restaurant}'s details
     */
    public EditRestaurantDescriptionBuilder(Restaurant restaurant) {
        descriptor = new EditRestaurantDescriptor();
        descriptor.setName(restaurant.getName());
        descriptor.setPhone(restaurant.getPhone());
        descriptor.setEmail(restaurant.getEmail());
        descriptor.setAddress(restaurant.getAddress());
        descriptor.setTags(restaurant.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRestaurantDescriptionBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRestaurantDescriptionBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRestaurantDescriptionBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditRestaurantDescriptionBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditRestaurantDescriptionBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditRestaurantDescriptor build() {
        return descriptor;
    }
}
