package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * An ExistingTags object is a list of currently existing tags.
 * This class provides methods to create the list and add and
 * remove tags from it.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class ExistingTags extends Observable implements Serializable {
    /**
     * The list of currently existing tags
     */
    private ArrayList<Tag> existingTags;

    /**
     * Constructs an empty list of existing tags.
     */
    ExistingTags() {
        existingTags = new ArrayList<>();
    }

    /**
     * This function returns this collection
     * of tags.
     *
     * @return a shallow copy of the current collection of existing tags.
     */
    public ArrayList<Tag> getExistingTags() {
        return new ArrayList<>(existingTags);
    }

    /**
     * Given the name of the tag, this function search through the list of
     * existing tags and returns the Tag with name tagName, and null if
     * none is found.
     *
     * @param tagName the tag name being searched for
     * @return the Tag with name tagName, null if none is found.
     */
    public Tag findTag(String tagName) {
        for (Tag tag : this.getExistingTags()) {
            if (tag.getTag().equals(tagName)) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Adds a new Tag to the collection of existing tags, if it does not already
     * exist
     *
     * @param tag the tag that is added to the collection of existing tags.
     */
    public void addTag(Tag tag) {
        if (!this.existingTags.contains(tag)) {
            this.existingTags.add(tag);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Removes a Tag from the collection of existing tags.
     *
     * @param tag the tag to be removed from the currently list of existing tags
     */
    public void removeTag(Tag tag) {
        this.existingTags.remove(tag);
        setChanged();
        notifyObservers();
    }

}

