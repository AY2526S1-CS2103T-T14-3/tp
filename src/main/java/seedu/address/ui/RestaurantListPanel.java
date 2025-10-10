package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.restaurant.Restaurant;

/**
 * Panel containing the list of persons.
 */
public class RestaurantListPanel extends UiPart<Region> {
    private static final String FXML = "RestaurantListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(RestaurantListPanel.class);

    @FXML
    private ListView<Restaurant> restaurantListView;

    /**
     * Creates a {@code RestaurantListPanel} with the given {@code ObservableList}.
     */
    public RestaurantListPanel(ObservableList<Restaurant> personList) {
        super(FXML);
        restaurantListView.setItems(personList);
        restaurantListView.setCellFactory(listView -> new RestaurantListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class RestaurantListViewCell extends ListCell<Restaurant> {
        @Override
        protected void updateItem(Restaurant person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new RestaurantCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
