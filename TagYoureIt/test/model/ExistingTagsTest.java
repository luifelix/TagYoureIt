package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ExistingTagsTest {

    private ExistingTags existingTags;

    @Before
    public void setUp() throws IOException {
        existingTags = new ExistingTags();
    }

    @After
    public void tearDown() throws Exception {
        existingTags = null;
    }

    @Test
    public void testGetEmptyTag() {
        ArrayList<Tag> emptyTag = new ArrayList<>();
        assertEquals(emptyTag, existingTags.getExistingTags());
    }

    @Test
    public void testAddTag() {
        ArrayList<Tag> newList = new ArrayList<>();
        Tag tagName = new Tag("fun");
        newList.add(tagName);
        existingTags.addTag(tagName);
        assertEquals(newList, existingTags.getExistingTags());
    }

    @Test
    public void testAddSameTag() {
        ArrayList<Tag> newList = new ArrayList<>();
        Tag tagName = new Tag("fun");
        newList.add(tagName);
        existingTags.addTag(tagName);
        existingTags.addTag(tagName);
        assertEquals(newList, existingTags.getExistingTags());
    }

    @Test
    public void testAddEmptyTag() {
        ArrayList<Tag> newList = new ArrayList<>();
        Tag tagName = new Tag("");
        newList.add(tagName);
        existingTags.addTag(tagName);
        assertEquals(newList, existingTags.getExistingTags());
    }

    @Test
    public void testFindEmptyTags() {
        assertEquals(null, existingTags.findTag(""));
    }

    @Test
    public void testFindTag() {
        Tag testTag = new Tag("fun");
        existingTags.addTag(testTag);
        assertEquals(testTag, existingTags.findTag("fun"));
    }

    @Test
    public void testRemoveTags() {
        ArrayList<Tag> newList = new ArrayList<>();
        Tag tag1 = new Tag("apple");
        Tag tag2 = new Tag("orange");
        newList.add(tag1);
        existingTags.addTag(tag1);
        existingTags.addTag(tag2);
        existingTags.removeTag(tag2);
        assertEquals(newList, existingTags.getExistingTags());
    }

    @Test
    public void testRemoveNonexistentTag() {
        ArrayList<Tag> newList = new ArrayList<>();
        Tag tag1 = new Tag("apple");
        Tag tag2 = new Tag("orange");
        newList.add(tag1);
        existingTags.addTag(tag1);
        existingTags.removeTag(tag2);
        assertEquals(newList, existingTags.getExistingTags());
    }
}
