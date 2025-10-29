package foodtrail.logic.commands;

import static foodtrail.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static foodtrail.logic.parser.CliSyntax.PREFIX_NAME;
import static foodtrail.logic.parser.CliSyntax.PREFIX_PHONE;
import static foodtrail.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import foodtrail.commons.core.index.Index;
import foodtrail.commons.util.CollectionUtil;
import foodtrail.commons.util.ToStringBuilder;
import foodtrail.logic.Messages;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.restaurant.Address;
import foodtrail.model.restaurant.IsMarked;
import foodtrail.model.restaurant.Name;
import foodtrail.model.restaurant.Phone;
import foodtrail.model.restaurant.Rating;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.Tag;

/**
 * Edits the details of an existing restaurant in the restaurant directory.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the restaurant identified "
            + "by the index number used in the displayed restaurant directory. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 ";

    public static final String MESSAGE_EDIT_RESTAURANT_SUCCESS = "Edited restaurant: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_RESTAURANT = "This restaurant already exists "
            + "in the restaurant directory.";

    private final Index index;
    private final EditRestaurantDescriptor editRestaurantDescriptor;

    /**
     * @param index of the restaurant in the filtered restaurant list to edit
     * @param editRestaurantDescriptor details to edit the restaurant with
     */
    public EditCommand(Index index, EditRestaurantDescriptor editRestaurantDescriptor) {
        requireNonNull(index);
        requireNonNull(editRestaurantDescriptor);

        this.index = index;
        this.editRestaurantDescriptor = new EditRestaurantDescriptor(editRestaurantDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Restaurant> lastShownList = model.getFilteredRestaurantList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);
        }

        Restaurant restaurantToEdit = lastShownList.get(index.getZeroBased());
        Restaurant editedRestaurant = createEditedRestaurant(restaurantToEdit, editRestaurantDescriptor);

        if (!restaurantToEdit.isSameRestaurant(editedRestaurant) && model.hasRestaurant(editedRestaurant)) {
            throw new CommandException(MESSAGE_DUPLICATE_RESTAURANT);
        }

        model.setRestaurant(restaurantToEdit, editedRestaurant);
        model.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        String restaurantDetails = "\n" + "Name: " + editedRestaurant.getName() + "\n"
                + "Phone: " + editedRestaurant.getPhone() + "\n"
                + "Address: " + editedRestaurant.getAddress() + "\n"
                + "Tags: " + editedRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(MESSAGE_EDIT_RESTAURANT_SUCCESS, restaurantDetails));
    }

    /**
     * Creates and returns a {@code Restaurant} with the details of {@code restaurantToEdit}
     * edited with {@code editRestaurantDescriptor}.
     */
    private static Restaurant createEditedRestaurant(Restaurant restaurantToEdit,
                                                 EditRestaurantDescriptor editRestaurantDescriptor) {
        assert restaurantToEdit != null;

        Name updatedName = editRestaurantDescriptor.getName().orElse(restaurantToEdit.getName());
        Phone updatedPhone = editRestaurantDescriptor.getPhone().orElse(restaurantToEdit.getPhone());
        Address updatedAddress = editRestaurantDescriptor.getAddress().orElse(restaurantToEdit.getAddress());
        Set<Tag> currentTags = restaurantToEdit.getTags();
        Optional<Rating> currentRating = restaurantToEdit.getRating();
        IsMarked currentIsMarked = restaurantToEdit.getIsMarked();

        return new Restaurant(updatedName, updatedPhone, updatedAddress, currentTags, currentRating, currentIsMarked);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editRestaurantDescriptor.equals(otherEditCommand.editRestaurantDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editRestaurantDescriptor", editRestaurantDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the restaurant with. Each non-empty field value will replace the
     * corresponding field value of the restaurant.
     */
    public static class EditRestaurantDescriptor {
        private Name name;
        private Phone phone;
        private Address address;

        public EditRestaurantDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditRestaurantDescriptor(EditRestaurantDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, address);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditRestaurantDescriptor)) {
                return false;
            }

            EditRestaurantDescriptor otherEditRestaurantDescriptor = (EditRestaurantDescriptor) other;
            return Objects.equals(name, otherEditRestaurantDescriptor.name)
                    && Objects.equals(phone, otherEditRestaurantDescriptor.phone)
                    && Objects.equals(address, otherEditRestaurantDescriptor.address);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("address", address)
                    .toString();
        }
    }
}
