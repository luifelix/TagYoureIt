package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ImageFile;
import model.Model;
import model.Tag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * An ImageGalleryView has three parts to it. The first part is
 * to display all images under a given directory chosen by a user.
 * The second part is the user can search for a tag and this view
 * will display images under that directory with that tag. Lastly,
 * the ImageGalleryView can display all changes made to a file whether
 * it'd be adding a tag or moving its location.
 *
 * @author David C., Felix L., Helen J., Alen T.
 * @version 1.8
 */

public class ImageGalleryView implements Observer {
    /**
     * The model that this ImageGalleryView displays information from.
     */
    private Model model;

    /**
     * The controller this ImageGalleryView relays user gestures to.
     */
    private Controller controller;

    /**
     * The Log of all file movement to be displayed to the user.
     */
    private Text logText;

    /**
     * The TilePane which contains all Images in the deepFileList.
     */
    private TilePane imageTile;

    /**
     * The TilePane which contains all ImageFiles with Tags matching the search
     * query.
     */
    private TilePane searchTile;

    /**
     * The text field where the user can search for all images with a tag.
     */
    private TextField searchBox;

    /**
     * Initializes a new ImageGalleryView with controller controller.  This
     * ImageGallery view displays all images in shallowFileList and
     * deepFile list, in separate tabs.
     */
    public ImageGalleryView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        this.logText = new Text();
        this.imageTile = new TilePane();
        this.searchTile = new TilePane();
        this.searchBox = new TextField();
        loadView();
    }

    /**
     * Initializes the view that displays all images to the User.
     */
    //Adapted from https://stackoverflow.com/questions/27182323/
    public void display() {
        Tab allImages = new Tab("All Images");
        ScrollPane tileScroll = new ScrollPane();
        imageTile.setPadding(new Insets(15, 15, 15, 15));
        imageTile.setHgap(15);
        imageTile.setVgap(15);
        tileScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tileScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tileScroll.setFitToWidth(true);
        tileScroll.setContent(imageTile);
        allImages.setContent(tileScroll);

        Tab log = new Tab("Rename Log");
        ScrollPane logScroll = new ScrollPane();
        logText.setText(ImageFile.readLog());
        logScroll.setContent(logText);
        logScroll.setPadding(new Insets(15, 15, 15, 15));
        log.setContent(logScroll);

        Tab search = new Tab("Search");
        VBox searchLayout = new VBox(15);
        Button searchButton = new Button("Search");
        HBox searchBar = new HBox(15);
        searchBar.setPadding(new Insets(15, 15, 15, 15));
        searchBar.getChildren().addAll(searchBox, searchButton);
        ScrollPane searchScroll = new ScrollPane();
        searchScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        searchTile.setHgap(15);
        searchTile.setVgap(15);
        searchScroll.setContent(searchTile);
        searchScroll.setPadding(new Insets(15, 15, 15, 15));
        searchLayout.getChildren().addAll(searchBar, searchScroll);
        search.setContent(searchLayout);

        searchButton.setOnAction(e -> search());

        TabPane root = new TabPane();
        log.setOnSelectionChanged(t -> {
            if (log.isSelected()) {
                refreshLog();
            }
        });

        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        root.setSide(Side.LEFT);
        root.getTabs().addAll(allImages, search, log);

        HBox buttons = new HBox();
        Button manageTags = new Button("Manage Tags");
        manageTags.setOnAction(e -> controller.openManageTagsView());
        buttons.getChildren().addAll(manageTags);
        buttons.setPadding(new Insets(15, 15, 15, 15));
        VBox layout = new VBox();
        layout.getChildren().addAll(buttons, root);

        Scene scene = new Scene(layout);
        Stage stage = new Stage();
        stage.getIcons().add(new Image("file:resources/logo.png"));
        stage.setTitle(this.model.getRoot().toString());
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(scene);

        root.setPrefHeight(stage.getHeight());
        stage.setOnCloseRequest(we -> controller.close());
        stage.show();
    }

    /**
     * Returns a ImageView for a File imageFile.
     *
     * @param imageFile the File to create an ImageView for.
     * @return          the ImageView for imageFile.
     */
    //Adapted from https://stackoverflow.com/questions/27182323/
    private ImageView createImageView(ImageFile imageFile) {
        ImageView imageView;
        FileInputStream input = null;
        try {
            input = new FileInputStream(imageFile.getFile());
            Image image = new Image(input,
                    150, 150, true,
                    true);
            if (!image.isError()) {
                imageView = new ImageView(image);
            } else {
                imageView = new ImageView("File:resources/error.png");
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(150);
            }
            imageView.maxWidth(150);
            imageView.maxHeight(150);
            imageView.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    controller.openImageFileView(imageFile);
                }
            });
        } catch (FileNotFoundException ex) {
            imageView = null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageView;
    }

    /**
     * Refreshes the collection of Files displayed in this ImageGalleryView
     */
    private void loadView() {
        search();
        imageTile.getChildren().clear();
        for (ImageFile im : this.model.getImageFiles()) {
            if (im.getFile().getAbsolutePath().startsWith(
                    model.getRoot().getAbsolutePath())) {
                ImageView imageView;
                imageView = createImageView(im);
                imageTile.getChildren().addAll(imageView);
            }
        }
    }

    /**
     * Refreshes the log displayed in this ImageGalleryView
     */
    private void refreshLog() {
        this.logText.setText(ImageFile.readLog());
    }

    /**
     * Update the searchTile to display all ImageFiles that have tag query
     */
    private void search() {
        ArrayList<ImageFile> found = new ArrayList<>();
        for (ImageFile f : model.getImageFiles()) {
            for (Tag tag : f.getCurrentTags()) {
                if (tag.getTag().equals(this.searchBox.getText())) {
                    found.add(f);
                }
            }
        }
        searchTile.getChildren().clear();
        for (ImageFile im : found) {
            if (im.getFile().getAbsolutePath().startsWith(
                    model.getRoot().getAbsolutePath())) {
                ImageView imageView;
                imageView = createImageView(im);
                if (imageView != null) {
                    searchTile.getChildren().addAll(imageView);
                }
            }
        }
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
