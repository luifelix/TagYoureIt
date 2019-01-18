package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ExistingTags;
import model.ImageFile;
import model.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * A ImageFileView displays information regarding an image file selected by the
 * user, including its graphical representation, file name, path and tags. It
 * also contains options to move the image file to another directory, to edit
 * its tags and view its tag history. This class provides methods for displaying
 * graphically said information.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class ImageFileView implements Observer {

    /**
     * The image file this view is displaying information about.
     */
    private ImageFile imageFile;

    /**
     * The collection of existing tags the user can select tags from.
     */
    private ExistingTags existingTags;

    /**
     * The controller for this ImageFileView.
     */
    private Controller controller;

    /**
     * The display of this image file's list of tag names.
     */
    private ListView<Tag> tagsView;

    /**
     * The display of this image file's path.
     */
    private Label filePath;

    /**
     * The stage for this ImageFileView.
     */
    private Stage stage;

    /**
     * Creates an interface that displays information of an image file provided.
     * It also displays the list of existing tags provided, and options to move
     * the image file to another directory, to edit its tags and view its tag
     * history.
     *
     * @param imageFile    the image file whose information is to be displayed
     * @param existingTags the collection of existing tags the user can select
     *                     from
     * @param controller   the controller to relay user gestures to
     */
    public ImageFileView(ImageFile imageFile, ExistingTags existingTags,
                         Controller controller) {
        this.imageFile = imageFile;
        this.existingTags = existingTags;
        this.controller = controller;
        this.tagsView = new ListView<>();
        this.filePath = new Label();
        this.stage = new Stage();
        stage.getIcons().add(new Image("file:resources/logo.png"));
        loadView();
    }

    /**
     * Displays an interface that displays information of this image file.
     */
    public void display() {
        ImageView imageView = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(imageFile.getFile());
            final Image image = new Image(input,
                    270, 270, true,
                    true);
            if (!image.isError()) {
                imageView = new ImageView(image);
            } else {
                imageView = new ImageView("File:resources/error.png");
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(200);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        VBox infoBox = new VBox(15);
        Label label = new Label("Current Tags for this Image:");
        tagsView.setMaxHeight(253);
        infoBox.getChildren().addAll(label, tagsView);

        HBox data = new HBox(15);
        data.getChildren().addAll(imageView, infoBox);
        data.setAlignment(Pos.CENTER);
        data.setPadding(new Insets(15, 15, 15, 15));

        Button moveTo = new Button("Move To...");
        moveTo.setOnAction(e -> {
            DirectoryChooser dirChooser = new DirectoryChooser();
            File selectedDirectory = dirChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                controller.moveFile(this.imageFile, selectedDirectory);
            }
        });
        Button editTags = new Button("Edit Tags");
        editTags.setOnAction(e -> {
            AddRemoveTagsView view = new AddRemoveTagsView(
                    imageFile, existingTags);
            ArrayList<Tag> newTags = view.display();
            if (newTags != null) {
                controller.setTags(imageFile, newTags);
            }
        });

        Button tagHistory = new Button("View Tag History");
        tagHistory.setOnAction(e -> {
            TagHistoryView view = new TagHistoryView(imageFile);
            String newName = view.display();
            if (newName != null) {
                controller.revertToName(imageFile, newName);
            }
        });

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(moveTo, editTags, tagHistory);

        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(filePath, data, buttons);
        root.setPadding(new Insets(15, 15, 15, 15));
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(680);
        stage.setHeight(400);
        stage.showAndWait();
    }

    /**
     * Loads all information displayed in this view from the model.
     */
    private void loadView() {
        filePath.setText(imageFile.getFile().getAbsolutePath());
        stage.setTitle(imageFile.getFile().getName());
        tagsView.getItems().clear();
        tagsView.getItems().addAll(imageFile.getCurrentTags());
        tagsView.requestFocus();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an Observable object's notifyObservers method to have
     * all the object's observers notified of the change.
     *
     * @param o     The observable object.
     * @param arg   An argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        loadView();
    }
}
