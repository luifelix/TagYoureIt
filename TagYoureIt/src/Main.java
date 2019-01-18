import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Model;
import view.AlertBox;

import java.io.File;

/**
 * This class represents the Main. The where the program
 * is to run in order to start the whole program. It is also
 * where the first window is created for choosing a directory.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */
public class Main extends Application {

    /**
     * The initial window opened by this application
     */
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.getIcons().add(new Image("file:resources/logo.png"));
        window.setTitle("Tag, You're it!");
        Button button = new Button("Open");

        Label label = new Label();
        label.setText("Choose your starting directory");

        ImageView logo = new ImageView("file:resources/title.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(400);

        button.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory =
                    directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                try {
                    Model model = new Model(selectedDirectory);
                    Controller controller = new Controller(model);
                    window.close();
                    controller.init();
                } catch (Exception e) {
                    AlertBox ab = new AlertBox("Error",
                            "An error occurred while " +
                                    "searching your selected directory.\n\n" +
                                    "Please try again.");
                    ab.display();
                }
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(logo, label, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 500, 250);
        window.setScene(scene);
        window.show();
    }
}
