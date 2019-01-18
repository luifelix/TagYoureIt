package model;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * The class represents the Model. The Model is responsible for
 * keeping track of any methods that want to access or edit any
 * file, array list, hash map that would be used in the program.
 * The user cannot access model itself, but can use these methods
 * via means of the Controller class.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class Model extends Observable implements Serializable {
    /**
     * List of accepted image file extensions
     */
    private static final ArrayList<String> EXTENSIONS = new ArrayList<>(
            Arrays.asList("jpg", "png", "bmp", "jpeg", "gif"));

    /**
     * The String representation of the path to the imageData file
     */
    private static final String IMAGE_DATA_PATH = "imageData.ser";

    /**
     * The String representation of the path to the existingTags file
     */
    private static final String EXISTING_TAGS_PATH = "existingTags.ser";

    /**
     * The ArrayList of all ImageFiles
     */
    private ArrayList<ImageFile> imageFiles;

    /**
     * The root directory this program is running in.
     */
    private File root;

    /**
     * ExistingTags object for this Model
     */
    private ExistingTags existingTags;

    /**
     * Creates a new Model for this program, rooted at file.  Walks the
     * file tree rooted at file, and adds any un-tracked imageFiles to HashMap
     * imageFiles
     *
     * @param file the root directory this program is responsible for.
     */
    @SuppressWarnings("unchecked")
    public Model(File file) throws IOException {
        this.root = file;
        existingTags = new ExistingTags();
        imageFiles = new ArrayList<>();
        File imageData = new File(IMAGE_DATA_PATH);
        if (imageData.exists()) {
            try {
                imageFiles = (ArrayList<ImageFile>) readObject(IMAGE_DATA_PATH);
            } catch (ClassNotFoundException | IOException |
                    ClassCastException e) {
                // imageData.ser was corrupted
                Files.delete(imageData.toPath());
            }
        }
        File tagData = new File(EXISTING_TAGS_PATH);
        if (tagData.exists()) {
            try {
                existingTags = (ExistingTags) readObject(EXISTING_TAGS_PATH);
            } catch (ClassNotFoundException | IOException |
                    ClassCastException e) {
                // existingTags.ser was corrupted
                Files.delete(tagData.toPath());
            }
        }
        // The FileVisitor to be used in Files.walkFileTree.
        FileVisitor<Path> fv = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs)
                    throws IOException {
                if (attrs.isRegularFile()) {
                    String fileExtension = file.toString().substring(
                            file.toString().lastIndexOf('.') + 1).toLowerCase();
                    // Check if the file is a image by checking its extension.
                    if (EXTENSIONS.contains(fileExtension)) {
                        if (getImageFile(file.toFile()) == null) {
                            imageFiles.add(new ImageFile(file.toFile()));
                        } else {
                            // Check imageFile for tags not in ExistingTags, and add them
                            ImageFile im = getImageFile(file.toFile());
                            if (!existingTags.getExistingTags().containsAll(
                                    im.getCurrentTags())) {
                                for (Tag tag : im.getCurrentTags()) {
                                    existingTags.addTag(tag);
                                }
                            }
                        }
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(file.toPath(), fv);
    }

    /**
     * Reads the Object at path from serialized file.
     *
     * @param path the path to the serialized file
     * @return the Object serialized to the file found at path.
     * @throws IOException            if I/O errors occurred while reading from
     *                                the file
     * @throws ClassNotFoundException Class definition of a serialized object
     *                                cannot be found.
     */
    private Object readObject(String path)
            throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(path);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        Object inputObj = input.readObject();
        input.close();
        return inputObj;
    }

    /**
     * Writes obj to file at filePath.
     *
     * @param filePath the file to write the records to
     */
    private void writeObject(String filePath, Object obj) throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(obj);
        output.close();
    }

    /**
     * Search this model's collection of ImageFiles for file.
     *
     * @param file the file being searched for in imageFiles
     * @return the ImageFile corresponding to file, null if no imageFile exists
     */
    public ImageFile getImageFile(File file) {
        for (ImageFile i : this.imageFiles) {
            if (i.getFile().equals(file)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Return this model's set of existing tags
     *
     * @return this model's set of existing tags
     */
    public ExistingTags getExistingTags() {
        return this.existingTags;
    }

    /**
     * Return the collection of ImageFiles for this Model.
     *
     * @return the collection of ImageFiles for this Model.
     */
    public ArrayList<ImageFile> getImageFiles() {
        return new ArrayList<>(imageFiles);
    }

    /**
     * Return the root file this model was created at
     *
     * @return the root file this model was created at
     */
    public File getRoot() {
        return this.root;
    }

    /**
     * Serializes the imageFiles and existingTags
     */
    public void saveAll() {
        try {
            writeObject(IMAGE_DATA_PATH, imageFiles);
            writeObject(EXISTING_TAGS_PATH, existingTags);
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
}
