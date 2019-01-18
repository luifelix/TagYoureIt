package controller;

import model.ImageFile;
import model.Model;
import model.Tag;
import view.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Controller class is responsible for relaying any changes made by the user
 * in any of the View classes to to the Model Class. It contains many methods to
 * modify the Model model such as moving files, adding tags to files and
 * reverting file names to previous names.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class Controller {
    /**
     * The Model that this controller interacts with.
     */
    private Model model;

    /**
     * Creates a new controller for model.
     *
     * @param model the model to create a controller for.
     */
    public Controller(Model model) {
        this.model = model;
    }

    /**
     * Initialize a new ImageGalleryView for the model.
     */
    public void init() {
        ImageGalleryView view = new ImageGalleryView(model, this);
        for (ImageFile f : model.getImageFiles()) {
            f.addObserver(view);
        }
        view.display();
    }

    /**
     * Move imageFile to destDirectory if no file in destDirectory has the same
     * original name.
     *
     * @param imageFile     the imageFile to move to destDirectory
     * @param destDirectory the destination directory to move imageFile to
     */
    public void moveFile(ImageFile imageFile, File destDirectory) {
        // Verify no files in destDirectory have the same name or original
        // name as imageFile
        boolean validMove = true;
        for (File child : destDirectory.listFiles()) {
            if (child.isFile() && model.getImageFile(child) != null &&
                    imageFile.getOriginalName().equals(
                            model.getImageFile(child).getOriginalName())) {
                AlertBox ab = new AlertBox("Error",
                        "A file with the same original name already " +
                                "exists");
                ab.display();
                validMove = false;
                break;
            }
        }
        // Move the file (only if it is a valid move)
        if (validMove) {
            File destFile = new File(destDirectory.getAbsolutePath() +
                    File.separator + imageFile.getFile().getName());
            try {
                imageFile.moveTo(destFile);
            } catch (IOException e) {
                AlertBox ab = new AlertBox("Error",
                        "Error occurred while attempting to move file");
                ab.display();
            }
        }
    }

    /**
     * Add a tag to the current set of existing tags, only if the tag is not
     * empty, and the same tag does not already exist.
     *
     * @param name the name of the tag to be added
     */
    public void addTag(String name) {
        if (!name.trim().equals("") &&
                model.getExistingTags().findTag(name) == null) {
            model.getExistingTags().addTag(new Tag(name));
        } else if (name.trim().equals("")) {
            AlertBox ab = new AlertBox("Error",
                    "Cannot add an empty tag");
            ab.display();
        } else if (model.getExistingTags().findTag(name) != null) {
            AlertBox ab = new AlertBox("Error",
                    "A tag with the same name already exists");
            ab.display();
        } else {
            AlertBox ab = new AlertBox("Error",
                    "An unknown error occurred while " +
                            "attempting to make this tag");
            ab.display();
        }
    }

    /**
     * Remove a tag from the current set of existing tags.  If any file has
     * any tag in tags, the user will be prompted to confirm removal of those
     * tags.
     *
     * @param tags the tags to be removed from the current set of existing tags
     */
    public void removeTags(ArrayList<Tag> tags) {
        if (tags != null) {
            // Find any images that have a tag being removed.
            ArrayList<ImageFile> warningFiles = new ArrayList<>();
            for (Tag selectedTag : tags) {
                for (ImageFile imageFile : model.getImageFiles()) {
                    if (imageFile.getFile().toString().startsWith(
                            model.getRoot().toString()) &&
                            imageFile.getCurrentTags().contains(selectedTag)) {
                        if (!warningFiles.contains(imageFile)) {
                            warningFiles.add(imageFile);
                        }
                    }
                }
            }
            // Confirm with the user to remove tags from images files.
            boolean confirm = true;
            if (!warningFiles.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ImageFile f : warningFiles) {
                    sb.append(" - ");
                    sb.append(f.getFile().toString());
                    sb.append("\n");
                }
                ConfirmBox cb = new ConfirmBox("Warning",
                        "The selected tags will be removed from " +
                                "these Files:\n" + sb.toString() +
                                "Do you wish to continue?");
                confirm = cb.display();
                // Remove tags from any image files that have a tag in tags
                if (confirm) {
                    for (ImageFile f : warningFiles) {
                        ArrayList<Tag> newTags = f.getCurrentTags();
                        newTags.removeAll(tags);
                        try {
                            f.editTags(newTags);
                        } catch (IOException e) {
                            AlertBox ab = new AlertBox("Error",
                                    "Error occurred while attempting" +
                                            " to rename file");
                            ab.display();
                            confirm = false;
                            break;
                        }
                    }
                }
            }
            // Remove the tag(s) from ExistingTags.
            if (confirm) {
                for (Tag tag : tags) {
                    model.getExistingTags().removeTag(tag);
                }
            }
        }
    }

    /**
     * Revert this imageFile's name to a previous name.
     *
     * @param imageFile the ImageFile to revert
     * @param name      the new name of imageFile
     */
    public void revertToName(ImageFile imageFile, String name) {
        // tags: Tags found in String name
        ArrayList<Tag> tags = new ArrayList<>();
        // missingTags: Tags found in name, but not in existingTags
        ArrayList<Tag> missingTags = new ArrayList<>();
        if (name.contains("@")) {
            // Populate tags and missingTags.
            for (String tagName : name.substring(
                    name.indexOf('@') + 1).split("@")) {
                if (model.getExistingTags().findTag(tagName.trim()) == null) {
                    Tag newTag = new Tag(tagName.trim());
                    missingTags.add(newTag);
                    model.getExistingTags().addTag(newTag);
                }
                tags.add(model.getExistingTags().findTag(tagName.trim()));
            }
        }
        try {
            imageFile.editTags(tags);
            if (!missingTags.isEmpty()) {
                // Notify user of any new tags created.
                StringBuilder sb = new StringBuilder();
                for (Tag tag : missingTags) {
                    sb.append(" - ").append(tag).append("\n");
                }
                String alertMessage = "The following Tags were created:\n" +
                        sb.toString();
                AlertBox ab = new AlertBox("Alert", alertMessage);
                ab.display();
            }
        } catch (IOException e) {
            AlertBox ab = new AlertBox("Error",
                    "Error occurred while attempting to rename file");
            ab.display();
        }

    }

    /**
     * Set the tags of this imageFile to tags
     *
     * @param imageFile the imageFile to set the tags of
     * @param tags      the new set of tags for imageFile
     */
    public void setTags(ImageFile imageFile, ArrayList<Tag> tags) {
        try {
            imageFile.editTags(tags);
        } catch (IOException e) {
            AlertBox ab = new AlertBox("Error",
                    "Error occurred while attempting to rename file");
            ab.display();
        }
    }

    /**
     * Initialize and display a new ImageFileView for imageFile
     *
     * @param imageFile the imageFile to display in the ImageFileView
     */
    public void openImageFileView(ImageFile imageFile) {
        ImageFileView view = new ImageFileView(imageFile,
                model.getExistingTags(), this);
        imageFile.addObserver(view);
        view.display();
    }

    /**
     * Initialize and display a new ManageTagsView
     */
    public void openManageTagsView() {
        ManageTagsView view = new ManageTagsView(model.getExistingTags(), this);
        model.getExistingTags().addObserver(view);
        view.display();
    }

    /**
     * Notify Model to serialize all data.
     */
    public void close() {
        this.model.saveAll();
    }
}
