package view;

import javafx.collections.FXCollections;
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
import model.ImageFile;

/**
 * A TagHistoryView displays a list of previous names of an
 * image file that was chosen from the user. This class is responsible
 * for creating the graphical interface to display the past names and
 * returns the name that the user wants to revert back to.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class TagHistoryView {
    /**
     * The ImageFile this TagHistoryView displays information about.
     */
    private ImageFile imageFile;

    /**
     * The past name of the imageFile that the user selects
     */
    private String selectedName;

    /**
     * Initializes the this TagHistoryView object
     */
    TagHistoryView(ImageFile imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * This method displays a stage populated with the different buttons and
     * the list view that the user can interact with. After a name has been
     * chosen, the chosen name will be returns as a string or null if no name
     * was chosen or the close button was clicked.
     *
     * @return the past name that was selected by the user
     */
    public String display() {
        ListView<String> pastNamesView = new ListView<>(
                FXCollections.observableArrayList(
                        imageFile.getPastImageNames()));
        Stage stage = new Stage();
        stage.getIcons().add(new Image("file:resources/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Past Names");
        Label label = new Label("Select the past name you with to revert" +
                " to, then click Done.");

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            selectedName = null;
            stage.close();
        });

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> {
            selectedName =
                    pastNamesView.getSelectionModel().getSelectedItems().get(0);
            stage.close();
        });

        VBox layout = new VBox(15);
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().addAll(cancelButton, doneButton);
        layout.getChildren().addAll(label, pastNamesView, buttons);
        Scene scene = new Scene(layout);
        layout.setPadding(new Insets(15, 15, 15, 15));
        stage.setScene(scene);
        stage.setWidth(500);
        stage.showAndWait();

        return selectedName;
    }
}
