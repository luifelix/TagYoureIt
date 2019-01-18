package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * An AlertBox displays an alert window.
 * This class provides methods to create and display said window.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

//Adapted from: https://www.youtube.com/watch?v=SpL3EToqaXA
public class AlertBox {

    /**
     * The title of this alert window.
     */
    private String title;

    /**
     * The message displayed in this alert window.
     */
    private String alert;

    /**
     * Creates an alert window with the title and alert message provided.
     *
     * @param title the title of the alert window
     * @param alert the message to be displayed in the alert window
     */
    public AlertBox(String title, String alert) {
        this.title = title;
        this.alert = alert;
    }

    /**
     * Displays this alert window.
     */
    public void display() {
        Stage window = new Stage();
        window.getIcons().add(new Image("file:resources/logo.png"));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label labelMessage = new Label();
        labelMessage.setText(alert);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15, 15, 15, 15));
        layout.getChildren().addAll(labelMessage, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
