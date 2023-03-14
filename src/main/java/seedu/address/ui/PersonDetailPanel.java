package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

import java.util.Comparator;
import java.util.logging.Logger;

public class PersonDetailPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonDetailPanel.class);
    @FXML
    private ListView<Person> personDetailListView;

    public PersonDetailPanel(ObservableList<Person> personList) {
        super(FXML);
        personDetailListView.setItems(personList);
        personDetailListView.setCellFactory(listView -> new PersonDetailListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonDetailListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonDetailCard(person).getRoot());
            }
        }
    }
}
