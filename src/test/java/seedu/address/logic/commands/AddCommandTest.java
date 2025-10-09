package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalRestaurants.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.FoodTrail;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyFoodTrail;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.testutil.RestaurantBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingRestaurantAdded modelStub = new ModelStubAcceptingRestaurantAdded();
        Restaurant validRestaurant = new RestaurantBuilder().build();

        CommandResult commandResult = new AddCommand(validRestaurant).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validRestaurant)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validRestaurant), modelStub.restaurantsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Restaurant validPerson = new RestaurantBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand
                .MESSAGE_DUPLICATE_RESTAURANT, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Restaurant alice = new RestaurantBuilder().withName("Alice").build();
        Restaurant bob = new RestaurantBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different restaurant -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
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
        public Path getFoodTrailFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFoodTrailFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRestaurant(Restaurant restaurant) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFoodTrail(ReadOnlyFoodTrail newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyFoodTrail getFoodTrail() {
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
    private class ModelStubWithPerson extends ModelStub {
        private final Restaurant restaurant;

        ModelStubWithPerson(Restaurant restaurant) {
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
        public ReadOnlyFoodTrail getFoodTrail() {
            return new FoodTrail();
        }
    }

}
