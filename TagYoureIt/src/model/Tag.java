package model;

import java.io.Serializable;

/**
 * A Tag is a label for a file image.
 * This class simply stores the name of the tag.
 *
 * @author David C., Felix L., Helen J., Alex T.
 * @version 1.8
 */

public class Tag implements Serializable {
    /**
     * The name of this tag
     */
    private String tag;

    /**
     * Constructs a tag with the name provided.
     *
     * @param tag the name of this tag.
     */
    public Tag(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the name of this tag.
     *
     * @return the name of this tag.
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * Returns whether two objects are both tags and contain the same name.
     *
     * @param o the object to be compared with
     * @return true if they are both tags and contain the same name
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Tag && this.tag.equals(((Tag) o).getTag());
    }

    /**
     * Returns the name of this tag
     *
     * @return the name of this tag
     */
    @Override
    public String toString() {
        return this.getTag();
    }
}