package view;

import javafx.scene.image.Image;
import model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * A RemoveTagsView is displays a list of ExistingTags that the
 * user can choose to delete. This class provides methods to
 * create the graphical interfaces to display said list.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class RemoveTagsView {

    /**
     * A list of existing tags.
     */
    private ExistingTags existingTags;

    /**
     * A list of selected tags
     */
    private ArrayList<Tag> selectedTags;

    /**
     * Initializes the RemoveTagView object.
     *
     * @param existingTags a list of existing tags
     */
    RemoveTagsView(ExistingTags existingTags) {
        this.existingTags = existingTags;
    }

    /**
     * This method represents the initializing of the window that
     * user can interact with by choosing tags they wish to remove.
     *
     * @return selectedTags The ArrayList<String> that contains
     *                      all of the selected tags that user
     *                      has chosen from the whole list
     *                      of existing tags.
     */
    public ArrayList<Tag> display() {
        ListView<Tag> existingTagsView = new ListView<>();
        existingTagsView.getItems().addAll(existingTags.getExistingTags());
        existingTagsView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);
        // Adapted from https://stackoverflow.com/questions/40900478/
        existingTagsView.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();
            while (node != null &&
                    node != existingTagsView && !(node instanceof ListCell)) {
                node = node.getParent();
            }
            if (node instanceof ListCell) {
                evt.consume();

                ListCell cell = (ListCell) node;
                ListView lv = cell.getListView();
                lv.requestFocus();

                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (cell.isSelected()) {
                        lv.getSelectionModel().clearSelection(index);
                    } else {
                        lv.getSelectionModel().select(index);
                    }
                }
            }
        });
        Stage stage = new Stage();
        stage.getIcons().add(new Image("file:resources/logo.png"));
        stage.setTitle("Remove Tags");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            selectedTags = null;
            stage.close();
        });

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> {
            selectedTags = new ArrayList<>();
            selectedTags.addAll(
                    existingTagsView.getSelectionModel().getSelectedItems());
            stage.close();
        });

        Label label = new Label("Select the tags you wish to remove.");

        VBox layout = new VBox(10);
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().addAll(cancelButton, doneButton);
        layout.getChildren().addAll(label, existingTagsView, buttons);
        layout.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setWidth(500);
        stage.showAndWait();

        return selectedTags;
    }
}
