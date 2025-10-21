package foodtrail.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import foodtrail.model.Model;
import foodtrail.model.ModelManager;
import foodtrail.model.RestaurantDirectory;
import foodtrail.model.UserPrefs;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.testutil.RestaurantBuilder;

public class SortCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new RestaurantDirectory(), new UserPrefs());

        // Unsorted input (mixed case to prove case-insensitive ordering)
        Restaurant c = new RestaurantBuilder().withName("zeta cafe").build();
        Restaurant a = new RestaurantBuilder().withName("Alpha Sushi").build();
        Restaurant b = new RestaurantBuilder().withName("beta Bites").build();
        Restaurant d = new RestaurantBuilder().withName("Delta Diner").build();

        model.addRestaurant(c);
        model.addRestaurant(a);
        model.addRestaurant(b);
        model.addRestaurant(d);
    }

    @Test
    public void execute_sortsByNameAscending_caseInsensitive() {
        new SortCommand().execute(model);

        List<Restaurant> view = model.getFilteredRestaurantList();
        assertEquals(4, view.size());

        // Expected A→Z order by name, ignoring case:
        assertEquals("Alpha Sushi", view.get(0).getName().fullName);
        assertEquals("beta Bites", view.get(1).getName().fullName);
        assertEquals("Delta Diner", view.get(2).getName().fullName);
        assertEquals("zeta cafe", view.get(3).getName().fullName);
    }

    @Test
    public void execute_idempotent_secondCallKeepsOrder() {
        new SortCommand().execute(model);
        List<Restaurant> first = List.copyOf(model.getFilteredRestaurantList());
        new SortCommand().execute(model);
        List<Restaurant> second = model.getFilteredRestaurantList();

        assertEquals(first.size(), second.size());
        for (int i = 0; i < first.size(); i++) {
            assertTrue(first.get(i).isSameRestaurant(second.get(i)));
        }
    }
}
