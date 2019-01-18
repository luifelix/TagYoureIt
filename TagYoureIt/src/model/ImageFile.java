package model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Observable;

/**
 * An ImageFile contains information regarding a particular image file, such as
 * its name, file extension, associated tags and past names. This class also
 * provides methods to rename the image file and to move it to another
 * directory.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class ImageFile extends Observable implements Serializable {

    /**
     * The log file that records any file renaming.
     */
    private static Log log;
    static {
        try {
            log = new Log("renameLog.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The original name of this file without any tags.
     */
    private final String originalName;

    /**
     * The file extension of this file (including the '.' delimiter).
     */
    private final String fileExtension;

    /**
     * The list of tags that is the current name of the image.
     */
    private ArrayList<Tag> currentTags;

    /**
     * The list of all the names this image has had in the past.
     */
    private ArrayList<String> pastImageNames;

    /**
     * The system file object associated with this image file.
     */
    private File file;

    /**
     * Constructs an image file object from the system file object provided.
     *
     * @param file the system file object
     */
    ImageFile(File file) {
        this.file = file;
        this.originalName = file.getName().substring(
                0, file.getName().lastIndexOf('.'));
        this.fileExtension = file.getName().substring(
                file.getName().lastIndexOf('.'));
        currentTags = new ArrayList<>();
        pastImageNames = new ArrayList<>();
    }

    /**
     * Returns the contents of the log file.
     *
     * @return the contents of the log file
     */
    public static String readLog() {
        return log.readLog();
    }

    /**
     * Returns the system file associated with this image file.
     *
     * @return the system file object
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Returns the name of the file as a list of tags.
     *
     * @return the name of the file as a list of tags
     */
    public ArrayList<Tag> getCurrentTags() {
        return new ArrayList<>(currentTags);
    }

    /**
     * Returns the past names of this image file in a list.
     *
     * @return a list of the past names of this image file
     */
    public ArrayList<String> getPastImageNames() {
        return new ArrayList<>(pastImageNames);
    }

    /**
     * Returns the list of tags provided as a string with format
     * " @tag_1 @tag2 ... @tag_n".
     *
     * @param tags the list of tags to be encoded
     * @return a string with format " @tag_1 @tag2 ... @tag_n"
     */
    private String encodeToString(ArrayList<Tag> tags) {
        StringBuilder buffer = new StringBuilder();
        for (Tag t : tags) {
            buffer.append(" ").append("@").append(t.getTag());
        }
        return buffer.toString();
    }

    /**
     * Renames this image file with the list of tags provided, and records
     * the previous name of this image file.
     *
     * @param tags the new set of tags for this ImageFile
     * @throws IOException if the system file object could not be renamed
     */
    public void editTags(ArrayList<Tag> tags) throws IOException {
        String newPath = file.getParent() + File.separator +
                originalName + encodeToString(tags) + fileExtension;
        this.moveTo(new File(newPath));
        this.pastImageNames.add(originalName + encodeToString(currentTags));
        this.currentTags = tags;
        setChanged();
        notifyObservers();
    }

    /**
     * Moves this image file to the file destination provided.
     *
     * @param dest the file destination
     * @throws IOException if the associated file object could not be moved
     */
    public void moveTo(File dest) throws IOException {
        Files.move(this.file.toPath(), dest.toPath());
        log.writeLog("Move file [" + this.file.getAbsolutePath() +
                "] to [" + dest.getAbsolutePath() + "]");
        this.file = dest;
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the original name of this file, without any tags
     *
     * @return the original name of this file, without any tags
     */
    public String getOriginalName() {
        return this.originalName;
    }
}
