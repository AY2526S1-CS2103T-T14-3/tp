package foodtrail.logic.commands;

import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalRestaurants.POPEYES;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.commons.core.GuiSettings;
import foodtrail.logic.commands.exceptions.CommandException;
import foodtrail.model.Model;
import foodtrail.model.ReadOnlyRestaurantDirectory;
import foodtrail.model.ReadOnlyUserPrefs;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.RestaurantBuilder;
import javafx.collections.ObservableList;

public class AddCommandTest {

    @Test
    public void constructor_nullRestaurant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_restaurantAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingRestaurantAdded modelStub = new ModelStubAcceptingRestaurantAdded();
        Restaurant validRestaurant = new RestaurantBuilder().build();

        String restaurantDetails = "\n" + "Name: " + validRestaurant.getName() + "\n"
                + "Phone: " + validRestaurant.getPhone() + "\n"
                + "Address: " + validRestaurant.getAddress() + "\n"
                + "Tags: " + validRestaurant.getTags().stream()
                .map(t -> t.tagName)
                .collect(Collectors.joining(", "));

        CommandResult commandResult = new AddCommand(validRestaurant).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, restaurantDetails),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validRestaurant), modelStub.restaurantsAdded);
    }

    @Test
    public void execute_duplicateRestaurant_throwsCommandException() {
        Restaurant validRestaurant = new RestaurantBuilder().build();
        AddCommand addCommand = new AddCommand(validRestaurant);
        ModelStub modelStub = new ModelStubWithRestaurant(validRestaurant);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_RESTAURANT, () ->
                addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Restaurant jollibee = new RestaurantBuilder().withName("Jollibee").build();
        Restaurant kfc = new RestaurantBuilder().withName("KFC").build();
        AddCommand addJollibeeCommand = new AddCommand(jollibee);
        AddCommand addKfcCommand = new AddCommand(kfc);

        // same object -> returns true
        assertTrue(addJollibeeCommand.equals(addJollibeeCommand));

        // same values -> returns true
        AddCommand addJollibeeCommandCopy = new AddCommand(jollibee);
        assertTrue(addJollibeeCommand.equals(addJollibeeCommandCopy));

        // different types -> returns false
        assertFalse(addJollibeeCommand.equals(1));

        // null -> returns false
        assertFalse(addJollibeeCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(addJollibeeCommand.equals(addKfcCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(POPEYES);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + POPEYES + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getRestaurantDirectoryFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setRestaurantDirectoryFilePath(Path restaurantDirectoryFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRestaurant(Restaurant restaurant) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setRestaurantDirectory(ReadOnlyRestaurantDirectory newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyRestaurantDirectory getRestaurantDirectory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRestaurant(Restaurant restaurant) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteRestaurant(Restaurant target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setRestaurant(Restaurant target, Restaurant editedRestaurant) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortRestaurantListByName() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Restaurant> getFilteredRestaurantList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredRestaurantList(Predicate<Restaurant> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single restaurant.
     */
    private class ModelStubWithRestaurant extends ModelStub {
        private final Restaurant restaurant;

        ModelStubWithRestaurant(Restaurant restaurant) {
            requireNonNull(restaurant);
            this.restaurant = restaurant;
        }

        @Override
        public boolean hasRestaurant(Restaurant restaurant) {
            requireNonNull(restaurant);
            return this.restaurant.isSameRestaurant(restaurant);
        }
    }

    /**
     * A Model stub that always accept the restaurant being added.
     */
    private class ModelStubAcceptingRestaurantAdded extends ModelStub {
        final ArrayList<Restaurant> restaurantsAdded = new ArrayList<>();

        @Override
        public boolean hasRestaurant(Restaurant restaurant) {
            requireNonNull(restaurant);
            return restaurantsAdded.stream().anyMatch(restaurant::isSameRestaurant);
        }

        @Override
        public void addRestaurant(Restaurant restaurant) {
            requireNonNull(restaurant);
            restaurantsAdded.add(restaurant);
        }

        @Override
        public void sortRestaurantListByName() {
            // This method is called by AddCommand, but not needed for this test.
        }

        @Override
        public ReadOnlyRestaurantDirectory getRestaurantDirectory() {
            return new RestaurantDirectory();
        }
    }

}
