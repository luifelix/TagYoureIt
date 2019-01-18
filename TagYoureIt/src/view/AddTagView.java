package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * An AddTagView prompts the user for a new tag that is
 * to be added to the list of currently existing tags.
 * This class provides a method for displaying said prompt.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class AddTagView {

    /**
     * The name of the tag that user wants to add.
     */
    private String tag;

    /**
     * Returns the tag the user added to the list of currently existing tags
     * through the prompt.
     *
     * @return the tag the user added to the list of existing tags.
     */
    public String display() {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:resources/logo.png"));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Tag");
        Label label1 = new Label("Tag Name:");
        TextField textBox = new TextField();

        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> {
            tag = textBox.getText();
            window.close();
        });

        textBox.setOnAction(event -> {
            doneButton.fire();
            event.consume();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            tag = null;
            window.close();
        });

        VBox layout = new VBox(15);
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.getChildren().addAll(cancelButton, doneButton);
        layout.getChildren().addAll(label1, textBox, buttons);
        layout.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setWidth(300);
        window.showAndWait();

        return tag;
    }
}
