package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ImageFileTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File file;

    private ImageFile imageFile;

    @Before
    public void setUp() throws IOException {
        file = tempFolder.newFile("image1.jpg");
        imageFile = new ImageFile(file);
    }

    @After
    public void tearDown() throws IOException {
        Files.delete(file.toPath());
        imageFile = null;
    }

    @Test
    public void testGetFile() throws Exception {
        assertEquals(file, imageFile.getFile());
    }

    @Test
    public void testGetOriginalName() throws Exception {
        assertEquals("image1", imageFile.getOriginalName());
    }

    @Test
    public void testGetEmptyCurrentTags() {
        ArrayList<Tag> emptyList = new ArrayList<>();
        assertEquals(emptyList, imageFile.getCurrentTags());
    }

    @Test
    public void testGetCurrentTags() throws IOException {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("tag1"));
        tagList.add(new Tag("tag2"));
        imageFile.editTags(tagList);
        assertEquals(tagList, imageFile.getCurrentTags());
    }

    @Test
    public void testGetEmptyPastImageNames() {
        ArrayList<String> emptyList = new ArrayList<>();
        assertEquals(emptyList, imageFile.getPastImageNames());
    }

    @Test
    public void testGetPastImageNames() throws IOException {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(new Tag("tag1"), new Tag("tag2")));
        imageFile.editTags(tagList);
        ArrayList<String> imageNames = new ArrayList<>();
        imageNames.add("image1");
        assertEquals(imageNames, imageFile.getPastImageNames());
    }

    @Test
    public void testMultiplePastImageNames() throws IOException {
        ArrayList<Tag> tagList1 = new ArrayList<>();
        ArrayList<Tag> tagList2 = new ArrayList<>();
        tagList1.add(new Tag("tag1"));
        tagList2.add(new Tag("tag2"));
        imageFile.editTags(tagList1);
        imageFile.editTags(tagList2);
        ArrayList<String> expected = new ArrayList<>();
        expected.add("image1");
        expected.add("image1 @tag1");
        assertEquals(expected, imageFile.getPastImageNames());
    }

    @Test
    public void testEncodeToString() throws IOException {
        ArrayList<String> pastNames = new ArrayList<>();
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(
                new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));

        // Call editTags twice, so one encoded name will enter the list of
        // past names.
        imageFile.editTags(tagList);
        imageFile.editTags(tagList);

        pastNames.add(imageFile.getOriginalName());
        pastNames.add(imageFile.getOriginalName() + " @tag1 @tag2 @tag3");

        assertEquals(pastNames, imageFile.getPastImageNames());
    }

    @Test
    public void testEditTagsFileName() throws  IOException {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(
                new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));
        imageFile.editTags(tagList);
        String newName = "image1 @tag1 @tag2 @tag3.jpg";
        assertEquals(newName, imageFile.getFile().getName());
    }

    @Test
    public void testEditTagsFileLocation() throws IOException {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.addAll(Arrays.asList(
                new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));
        imageFile.editTags(tagList);
        File newFile = (Paths.get(
                tempFolder.getRoot().toString(),
                "image1 @tag1 @tag2 @tag3.jpg")).toFile();
        assertEquals(newFile, imageFile.getFile());
    }

    @Test
    public void testMoveToSubDirectory() throws Exception {
        File dest = tempFolder.newFolder("subDirectory");
        imageFile.moveTo(new File(dest + File.separator +
                imageFile.getOriginalName()));
        assertEquals(dest, imageFile.getFile().getParentFile());
    }
}