package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ExistingTags;
import model.Tag;

import java.util.Observable;
import java.util.Observer;

/**
 * ManageTagsView displays a list of tags that currently
 * exists in the program whenever the Manage Tags button
 * is pressed on the ImageGalleryView. This class
 * codes for just displaying, but not with adding or
 * removing the tags.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class ManageTagsView implements Observer {

    /**
     * The controller for this view
     */
    private Controller controller;

    /**
     * The collection of existing tags this File has
     */
    private ExistingTags existingTags;

    /**
     * The list of existing tags that can be viewed by user.
     */
    private ListView<Tag> existingTagsView;


    /**
     * Initializes an AddRemoveTagsView object
     */
    public ManageTagsView(ExistingTags existingTags, Controller controller) {
        this.existingTags = existingTags;
        this.controller = controller;
        existingTagsView = new ListView<>();
        loadView();
    }

    /**
     * Display the dialogue for the user to select the tags he wishes to have
     * in the current set of existing tags.
     */
    public void display() {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("file:resources/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Manage Tags");

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> stage.close());

        Button addTagButton = new Button("Add Tag");
        addTagButton.setOnAction(e -> {
            AddTagView view = new AddTagView();
            controller.addTag(view.display());
        });

        Button removeTagButton = new Button("Remove Tags");
        removeTagButton.setOnAction(e -> {
            RemoveTagsView view = new RemoveTagsView(existingTags);
            controller.removeTags(view.display());
        });

        Label label = new Label("Currently Existing Tags:");

        VBox layout = new VBox(10);
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().addAll(addTagButton, removeTagButton, doneButton);
        layout.getChildren().addAll(label, existingTagsView, buttons);
        layout.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setWidth(500);
        stage.show();
    }

    /**
     * Loads all information displayed in this view from model.
     */
    private void loadView() {
        existingTagsView.getItems().clear();
        existingTagsView.getItems().addAll(existingTags.getExistingTags());
    }

    /**
     * This method updates itself whenever a tag is added or removed
     * from existing tags. Update method is called whenever an observed
     * object is changed and is called by notifyObservers' method
     * to have all object's observers to be changed.
     *
     * @param o     The observable object.
     * @param arg   An argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        loadView();
    }
}