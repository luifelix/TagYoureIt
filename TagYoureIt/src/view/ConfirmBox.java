package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * A ConfirmBox prompts the user for a confirmation of a
 * particular action. This class contains methods to
 * create and display said prompt.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

//Adapted from: https://www.youtube.com/watch?v=HFAsMWkiLvg
public class ConfirmBox {
    /**
     * Whether the user wants the particular action performed.
     */
    private boolean answer;

    /**
     * The title of the prompt.
     */
    private String title;

    /**
     * The message prompted that describes the particular action.
     */
    private String message;

    /**
     * Creates a confirmation prompt with the title and message provided.
     *
     * @param title   the title of the confirmation prompt
     * @param message the message displayed in the confirmation prompt that
     *                describes the particular action
     */
    public ConfirmBox(String title, String message) {
        this.title = title;
        this.message = message;
    }

    /**
     * Returns whether the user wants a particular action performed as selected
     * through the confirmation prompt.
     *
     * @return true if the user selected the "Yes" option
     */
    public boolean display() {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:resources/logo.png"));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label labelMessage = new Label();
        Label blankLabel = new Label();
        labelMessage.setText(message);
        blankLabel.setText(" ");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(15);
        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(noButton, yesButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(labelMessage, buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        layout.setPadding(new Insets(15, 15, 15, 15));
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
