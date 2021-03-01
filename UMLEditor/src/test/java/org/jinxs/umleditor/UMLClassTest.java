package org.jinxs.umleditor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * Unit test for UMLClass.
 */
public class UMLClassTest  {

    // CLASS TESTS: CONSTRUCTOR
    // --------------------------------------------
    @Test
    public void testConstructor() {
        UMLClass c = new UMLClass("testName");
        assertEquals("Named to \"testName\"", "testName", c.name);

        // Likely want to add tests to ensure illegal names are not allowed in the future
        // Ex: starting with a number, names with spaces
    }


    // RELATIONSHIP TESTS: ADD, DELETE, GET
    // --------------------------------------------
    // getRels is effectively tested while testing add and delete

    @Test
    public void addRelTest() {
        UMLClass c = new UMLClass("className");

        // Make sure rels is empty at first
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Add a single relationship where c is the src
        c.addRel("otherClass1", true);
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(0).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass1");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");

        // Add a second relationship where c is the dest
        c.addRel("otherClass2", false);
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(1).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(1).get(0), "otherClass2");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(1).get(1), "dest");

        // Add two more rels
        c.addRel("otherClass3", false);
        c.addRel("otherClass4", true);
        assertEquals("Relationships ArrayList has size 4", c.getRels().size(), 4);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(2).size(), 2);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(3).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(2).get(0), "otherClass3");
        assertEquals("First part of rel is the other class name", c.getRels().get(3).get(0), "otherClass4");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(2).get(1), "dest");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(3).get(1), "src");

        // Make sure that the capacity is ensured when exceeding 100 rels
        for (int i = 5; i < 105; ++i) {
            c.addRel("otherClass" + i, true);
        }
        assertEquals("Relationships ArrayList has size 104", c.getRels().size(), 104);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(103).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(103).get(0), "otherClass104");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(103).get(1), "src");

    }

    // Depends on addRel working
    @Test
    public void deleteRelTest() {
        UMLClass c = new UMLClass("className");

        // Add and remove 1 relationship
        c.addRel("otherClass1", true);
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        c.deleteRel("otherClass1");
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Remove from the empty list
        c.deleteRel("otherClass1");
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Add 2 rels and remove the 2nd
        c.addRel("otherClass1", true);
        c.addRel("otherClass2", false);
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        c.deleteRel("otherClass2");
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(0).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass1");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");

        // Add 2 more rels and remove the 1st from the previous test
        c.addRel("otherClass3", false);
        c.addRel("otherClass4", true);
        assertEquals("Relationships ArrayList has size 3", c.getRels().size(), 3);
        c.deleteRel("otherClass1");
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass3");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
        assertEquals("First part of rel is the other class name", c.getRels().get(1).get(0), "otherClass4");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(1).get(1), "src");

        // Add 100 more rels and delete all but one
        for (int i = 5; i < 105; ++i) {
            c.addRel("otherClass" + i, false);
        }
        assertEquals("Relationships ArrayList has size 102", c.getRels().size(), 102);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(101).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(101).get(0), "otherClass104");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(101).get(1), "dest");

        for (int i = 3; i < 104; ++i) {
            c.deleteRel("otherClass" + i);
        }
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 2", c.getRels().get(0).size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass104");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
    }

    // ATTRIBUTE TESTS: ADD, DELETE, RENAME, GET
    // --------------------------------------------
    // getFields is effectively tested while testing add, delete, and rename
    // Attribute tests are very similar to the relationship tests

    @Test
    public void addAttrTest() {
        UMLClass c = new UMLClass("className");

        // Make sure attributes is empty at first
        assertTrue("Attributes ArrayList is empty", c.getFields().isEmpty());

        // Add a single attr
        c.addAttr("attr1");
        assertEquals("Attributes ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("Attribute is added correctly", c.getFields().get(0), "attr1");

        // Add a second attr
        c.addAttr("attr2");
        assertEquals("Attributes ArrayList has size 2", c.getFields().size(), 2);
        assertEquals("Attribute is added correctly", c.getFields().get(1), "attr2");

        // Add two more attrs
        c.addAttr("attr3");
        c.addAttr("attr4");
        assertEquals("Attributes ArrayList has size 4", c.getFields().size(), 4);
        assertEquals("Attribute is added correctly", c.getFields().get(2), "attr3");
        assertEquals("Attribute is added correctly", c.getFields().get(3), "attr4");

        // Make sure that the capacity is ensured when exceeding 100 attrs
        for (int i = 5; i < 105; ++i) {
            c.addAttr("attr" + i);
        }
        assertEquals("Attributes ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Attribute is added correctly", c.getFields().get(103), "attr104");

        // Make sure duplicates are not allowed
        assertTrue("addAttr returns false when adding dup", !c.addAttr("attr1"));
        assertEquals("Attributes ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(103), "attr104");

        assertTrue("addAttr returns false when adding dup", !c.addAttr("attr104"));
        assertEquals("Attributes ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(103), "attr104");

        assertTrue("addAttr returns false when adding dup", !c.addAttr("attr53"));
        assertEquals("Attributes ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(103), "attr104");
        assertEquals("Attributes stays the same when adding dup", c.getFields().get(52), "attr53");
    }

    // Depends on addAttr working
    @Test
    public void deleteAttrTest() {
        UMLClass c = new UMLClass("className");

        // Add and remove 1 attribute
        c.addAttr("attr1");
        assertEquals("Attributes ArrayList has size 1", c.getFields().size(), 1);
        c.deleteAttr("attr1");
        assertEquals("Attributes ArrayList has size 0", c.getFields().size(), 0);

        // Remove from the empty list
        assertTrue("deleteAttr returns false on nonexsitant attrs", !c.deleteRel("attr1"));
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Add 2 attrs and remove the 2nd
        c.addAttr("attr1");
        c.addAttr("attr2");
        assertEquals("Attributes ArrayList has size 2", c.getFields().size(), 2);
        c.deleteAttr("attr2");
        assertEquals("Attributes ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("Attribute stays in the list when another is deleted", c.getFields().get(0), "attr1");

        // Add 2 more attrs and remove the 1st from the previous test
        c.addAttr("attr3");
        c.addAttr("attr4");
        assertEquals("Attributes ArrayList has size 3", c.getFields().size(), 3);
        c.deleteAttr("attr1");
        assertEquals("Attributes ArrayList has size 2", c.getFields().size(), 2);
        assertEquals("Attribute stays in the list when another is deleted", c.getFields().get(0), "attr3");
        assertEquals("Attribute stays in the list when another is deleted", c.getFields().get(1), "attr4");

        // Add 100 more attrs and delete all but one
        for (int i = 5; i < 105; ++i) {
            c.addAttr("attr" + i);
        }
        assertEquals("Attributes ArrayList has size 102", c.getFields().size(), 102);
        assertEquals("First attr is correct after adding many attrs", c.getFields().get(0), "attr3");
        assertEquals("Last attr is correct after adding many attrs", c.getFields().get(101), "attr104");

        for (int i = 3; i < 104; ++i) {
            c.deleteAttr("attr" + i);
        }
        assertEquals("Attributes ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("First attr is correct after deleting many attrs", c.getFields().get(0), "attr104");
    }

    @Test
    public void renameAttrTest() {
        UMLClass c = new UMLClass("className");

        // Try to rename on empty attr list
        c.renameAttr("attr1", "attr2");
        assertEquals("Attributes ArrayList has size 0", c.getFields().size(), 0);

        // Add and rename one attr
        c.addAttr("attr1");
        assertEquals("Attributes ArrayList has size 1", c.getFields().size(), 1);
        c.renameAttr("attr1", "attr2");
        assertEquals("Attributes ArrayList has size 1 after renaming", c.getFields().size(), 1);
        assertEquals("First attr is correct after renaming", c.getFields().get(0), "attr2");

        // Add 2 more attrs and rename the last one
        c.addAttr("attr3");
        c.addAttr("attr4");
        assertEquals("Attributes ArrayList has size 3", c.getFields().size(), 3);
        c.renameAttr("attr4", "attr5");
        assertEquals("Attributes ArrayList has size 3 after rename", c.getFields().size(), 3);
        assertEquals("First attr is correct after renaming third", c.getFields().get(0), "attr2");
        assertEquals("Second attr is correct after renaming others", c.getFields().get(1), "attr3");
        assertEquals("Third attr is correct after renaming", c.getFields().get(2), "attr5");

        // Delete a renamed attr
        c.deleteAttr("attr5");
        assertEquals("Attributes ArrayList has size 2", c.getFields().size(), 2);

        // Delete all attrs
        c.deleteAttr("attr2");
        c.deleteAttr("attr3");
        assertEquals("Attributes ArrayList has size 0 after deleting", c.getFields().size(), 0);

        // Attempt to rename deleted attr
        assertTrue("Deleted attr cannot be renamed", !c.renameAttr("attr3", "attr6"));
        assertEquals("Attributes ArrayList has size 0", c.getFields().size(), 0);

        // Add >100 attrs and rename the first and last
        for (int i = 0; i < 107; ++i) {
            c.addAttr("attr" + i);
        }
        assertEquals("Attributes ArrayList has size 107", c.getFields().size(), 107);
        assertTrue("First attr can be renamed when attrs > 100", c.renameAttr("attr0", "coolAttr"));
        assertTrue("Last attr can be renamed when attrs > 100", c.renameAttr("attr106", "coolerAttr"));
        assertEquals("First attr is correct after renaming when attrs > 100", c.getFields().get(0), "coolAttr");
        assertEquals("Last attr is correct after renaming when attrs > 100", c.getFields().get(106), "coolerAttr");
    }
}