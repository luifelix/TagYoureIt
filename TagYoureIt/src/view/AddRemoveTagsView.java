package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ExistingTags;
import model.ImageFile;
import model.Tag;

import java.util.ArrayList;

/**
 * An AddRemoveTagsView displays a list of currently existing tags
 * from which the user can select from to name an image file.
 * This class provides methods to create the graphical interface
 * to display said list.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class AddRemoveTagsView {

    /**
     * The image file to select tags for.
     */
    private ImageFile imageFile;

    /**
     * The collection of existing tags.
     */
    private ExistingTags existingTags;

    /**
     * The tags selected by the user.
     */
    private ArrayList<Tag> selectedTags;

    /**
     * Creates a graphical interface that the user interacts with to select tags
     * from the pool of existing tags provided to rename the image file provided.
     */
    AddRemoveTagsView(ImageFile imageFile, ExistingTags existingTags) {
        this.imageFile = imageFile;
        this.existingTags = existingTags;
    }

    /**
     * Returns the list of tags the user selected for this image file through
     * the graphical interface.
     *
     * @return a list of tags the user selected for this image file
     */
    public ArrayList<Tag> display() {
        ListView<Tag> existingTagsView = new ListView<>();
        existingTagsView.getItems().addAll(existingTags.getExistingTags());
        existingTagsView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);
        for (Tag selected : imageFile.getCurrentTags()) {
            existingTagsView.getSelectionModel().select(selected);
        }
        //Adapted from https://stackoverflow.com/questions/40900478/
        existingTagsView.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();
            while (node != null && node != existingTagsView &&
                    !(node instanceof ListCell)) {
                node = node.getParent();
            }
            if (node instanceof ListCell) {
                evt.consume();
                ListCell cell = (ListCell) node;
                ListView lv = cell.getListView();
                lv.requestFocus();

                if (!cell.isEmpty()) {
                    // handle selection for non-empty cells
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
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add and Remove Tags");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            this.selectedTags = null;
            stage.close();
        });

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> {
            selectedTags = new ArrayList<>();
            selectedTags.addAll(
                    existingTagsView.getSelectionModel().getSelectedItems());
            stage.close();
        });

        Label label = new Label(
                "Click on the tags you wish to have for this image.\n\n" +
                        "Currently Existing Tags:");

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