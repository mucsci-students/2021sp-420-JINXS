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

        // Name restrictions are implemented in the editor file,
        // so they cannot be tested here
    }


    // RELATIONSHIP TESTS: ADD, DELETE, GET
    // --------------------------------------------
    // getRels is effectively tested while testing add and delete

    // Depends on constructor working
    // Changed to include types for sprint 2
    @Test
    public void addRelTest() {
        UMLClass c = new UMLClass("className");

        // Make sure rels is empty at first
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());
        
        // Add a single relationship where c is the src with type "aggregation"
        assertTrue("Adding first relationship succeeds (with 3 params)", c.addRel("otherClass1", true, "aggregation"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass1");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");
        assertEquals("Third part of rel is the type \"aggregation\"", c.getRels().get(0).get(2), "aggregation");

        // Add a second relationship where c is the dest with type "realization"
        assertTrue("Adding first relationship succeeds (with 3 params)", c.addRel("otherClass2", false, "realization"));
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        assertEquals("2nd relationship's inner ArrayList has size 3", c.getRels().get(1).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(1).get(0), "otherClass2");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(1).get(1), "dest");
        assertEquals("Third part of rel is the type \"realization\"", c.getRels().get(1).get(2), "realization");

        // Add two more rels
        assertTrue("Adding more relationships succeeds", c.addRel("otherClass3", false, "inheritance"));
        assertTrue("Adding more relationships succeeds", c.addRel("otherClass4", true, "composition"));
        assertEquals("Relationships ArrayList has size 4", c.getRels().size(), 4);
        assertEquals("3rd relationship's inner ArrayList has size 3", c.getRels().get(2).size(), 3);
        assertEquals("4th relationship's inner ArrayList has size 3", c.getRels().get(3).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(2).get(0), "otherClass3");
        assertEquals("First part of rel is the other class name", c.getRels().get(3).get(0), "otherClass4");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(2).get(1), "dest");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(3).get(1), "src");
        assertEquals("Third part of rel is the type \"inheritance\"", c.getRels().get(2).get(2), "inheritance");
        assertEquals("Third part of rel is the type \"composition\"", c.getRels().get(3).get(2), "composition");

        // Make sure that the capacity is ensured when exceeding 100 rels
        for (int i = 5; i < 105; ++i) {
            assertTrue("Adding up to and more than 100 relationships succeeds", c.addRel("otherClass" + i, true, "composition"));
        }
        assertEquals("Relationships ArrayList has size 104", c.getRels().size(), 104);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(103).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(103).get(0), "otherClass104");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(103).get(1), "src");
        assertEquals("Third part of rel is the type \"composition\"", c.getRels().get(103).get(2), "composition");

        // Relationship type restrictions are implemented in the editor file,
        // so they cannot be tested here
    }

    // Depends on addRel working
    // Does not depend on new rel types from sprint 2: tests are the same as sprint 1,
    // but adding relationship lines updated to include types, and rel size checks
    // updated to check for 3 Methods: className, isSrc, and type
    @Test
    public void deleteRelTest() {
        UMLClass c = new UMLClass("className");

        // Add and remove 1 relationship
        c.addRel("otherClass1", true, "inheritance");
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertTrue("Deleting first relationship succeeds", c.deleteRel("otherClass1"));
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Remove from the empty list
        assertTrue("Removing from empty rel list returns false", !c.deleteRel("otherClass1"));
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Add 2 rels and remove the 2nd
        c.addRel("otherClass1", true, "composition");
        c.addRel("otherClass2", false, "aggregation");
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        assertTrue("Deleting relationship when rels > 1 succeeds", c.deleteRel("otherClass2"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass1");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");
        assertEquals("Third part of rel is the type \"composition\"", c.getRels().get(0).get(2), "composition");

        // Add 2 more rels and remove the 1st from the previous test
        c.addRel("otherClass3", false, "realization");
        c.addRel("otherClass4", true, "inheritance");
        assertEquals("Relationships ArrayList has size 3", c.getRels().size(), 3);
        assertTrue("Deleting relationship when rels > 1 succeeds", c.deleteRel("otherClass1"));
        assertEquals("Relationships ArrayList has size 2", c.getRels().size(), 2);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass3");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
        assertEquals("Third part of rel is the type \"realization\"", c.getRels().get(0).get(2), "realization");
        assertEquals("First part of rel is the other class name", c.getRels().get(1).get(0), "otherClass4");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(1).get(1), "src");
        assertEquals("Third part of rel is the type \"inheritance\"", c.getRels().get(1).get(2), "inheritance");

        // Add 100 more rels and delete all but one
        for (int i = 5; i < 105; ++i) {
            c.addRel("otherClass" + i, false, "aggregation");
        }
        assertEquals("Relationships ArrayList has size 102", c.getRels().size(), 102);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(101).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(101).get(0), "otherClass104");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(101).get(1), "dest");
        assertEquals("Third part of rel is the type \"aggregation\"", c.getRels().get(101).get(2), "aggregation");

        // Delete all but the last relationship
        for (int i = 3; i < 104; ++i) {
            assertTrue("Deleting relationship when rels > 100 succeeds", c.deleteRel("otherClass" + i));
        }
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass104");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
        assertEquals("Third part of rel is the type \"aggregation\"", c.getRels().get(0).get(2), "aggregation");
    }

    // Added for sprint 2
    @Test
    public void changeRelTypeTest() {
        UMLClass c = new UMLClass("className");

        // Change one relationship where c is the destination
        c.addRel("otherClass1", false, "realization");
        assertTrue("Changing from \"realization\" to \"inheritance\" succeeds", c.changeRelType("otherClass1", "inheritance"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass1");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
        assertEquals("Third part of rel is the type \"inheritance\"", c.getRels().get(0).get(2), "inheritance");

        c.deleteRel("otherClass1");

        // Change one relationship where c is the source
        c.addRel("otherClass2", true, "aggregation");
        assertTrue("Changing from \"aggregation\" to \"composition\" succeeds", c.changeRelType("otherClass2", "composition"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass2");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");
        assertEquals("Third part of rel is the type \"composition\"", c.getRels().get(0).get(2), "composition");

        // Rename an already renamed class
        assertTrue("Changing from \"composition\" to \"inheritance\" succeeds", c.changeRelType("otherClass2", "inheritance"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass2");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "src");
        assertEquals("Third part of rel is the type \"inheritance\"", c.getRels().get(0).get(2), "inheritance");

        // Attempt to rename when the rel list is empty
        c.deleteRel("otherClass2");
        assertTrue("Calling changeRelType when rels is empty fails", !c.changeRelType("otherClass1", "composition"));
        assertEquals("Relationships ArrayList has size 0", c.getRels().size(), 0);

        // Attempt to rename a relationship that does not exist
        c.addRel("otherClass3", false, "aggregation");
        assertTrue("Calling changeRelType on a relationship that does not exist fails", !c.changeRelType("otherClass4", "composition"));
        assertEquals("Relationships ArrayList has size 1", c.getRels().size(), 1);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass3");
        assertEquals("Second part of rel is \"dest\"", c.getRels().get(0).get(1), "dest");
        assertEquals("Third part of rel is the type \"aggregation\"", c.getRels().get(0).get(2), "aggregation");

        // Add 2 more rels and rename the 2nd one (first added in this segment)
        c.addRel("otherClass4", true, "realization");
        c.addRel("otherClass5", true, "inheritance");
        assertTrue("Changing from \"realization\" to \"composition\" succeeds", c.changeRelType("otherClass4", "composition"));
        assertEquals("Relationships ArrayList has size 3", c.getRels().size(), 3);
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(1).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(1).get(0), "otherClass4");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(1).get(1), "src");
        assertEquals("Third part of rel is the type \"composition\"", c.getRels().get(1).get(2), "composition");
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(0).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(0).get(0), "otherClass3");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(0).get(1), "dest");
        assertEquals("Third part of rel is the type \"aggregation\"", c.getRels().get(0).get(2), "aggregation");
        assertEquals("Relationship's inner ArrayList has size 3", c.getRels().get(2).size(), 3);
        assertEquals("First part of rel is the other class name", c.getRels().get(2).get(0), "otherClass5");
        assertEquals("Second part of rel is \"src\"", c.getRels().get(2).get(1), "src");
        assertEquals("Third part of rel is the type \"inheritance\"", c.getRels().get(2).get(2), "inheritance");
    }

    // FIELD AND METHOD TESTS: ADD, DELETE, RENAME, GET
    // --------------------------------------------
    // getFields and getMethods are effectively tested while testing add, delete, and rename

    @Test
    public void addFieldTest() {
        UMLClass c = new UMLClass("className");

        // Make sure fields is empty at first
        assertTrue("Fields ArrayList is empty", c.getFields().isEmpty());

        // Add a single field
        assertTrue("Adding field succeeds", c.addField("attr1"));
        assertEquals("Fields ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("Field is added correctly", c.getFields().get(0), "attr1");

        // Add a second field
        assertTrue("Adding field succeeds", c.addField("attr2"));
        assertEquals("Fields ArrayList has size 2", c.getFields().size(), 2);
        assertEquals("Field is added correctly", c.getFields().get(1), "attr2");

        // Add two more fields
        assertTrue("Adding field succeeds", c.addField("attr3"));
        assertTrue("Adding field succeeds", c.addField("attr4"));
        assertEquals("Fields ArrayList has size 4", c.getFields().size(), 4);
        assertEquals("Field is added correctly", c.getFields().get(2), "attr3");
        assertEquals("Field is added correctly", c.getFields().get(3), "attr4");

        // Make sure that the capacity is ensured when exceeding 100 fields
        for (int i = 5; i < 105; ++i) {
            assertTrue("Adding field succeeds", c.addField("attr" + i));
        }
        assertEquals("Fields ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Field is added correctly", c.getFields().get(103), "attr104");

        // Make sure duplicates are not allowed
        assertTrue("addField returns false when adding dup", !c.addField("attr1"));
        assertEquals("Fields ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Fields stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Fields stays the same when adding dup", c.getFields().get(103), "attr104");

        assertTrue("addField returns false when adding dup", !c.addField("attr104"));
        assertEquals("Fields ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Fields stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Fields stays the same when adding dup", c.getFields().get(103), "attr104");

        assertTrue("addField returns false when adding dup", !c.addField("attr53"));
        assertEquals("Fields ArrayList has size 104", c.getFields().size(), 104);
        assertEquals("Fields stays the same when adding dup", c.getFields().get(0), "attr1");
        assertEquals("Fields stays the same when adding dup", c.getFields().get(103), "attr104");
        assertEquals("Fields stays the same when adding dup", c.getFields().get(52), "attr53");
    }

    @Test
    public void addMethodTest() {
        UMLClass c = new UMLClass("className");
        
        // Make sure Methods is empty at first
        assertTrue("Methods ArrayList is empty", c.getMethods().isEmpty());

        // Add a single method
        assertTrue("Adding method succeeds", c.addMethod("attr1"));
        assertEquals("Methods ArrayList has size 1", c.getMethods().size(), 1);
        assertEquals("Method is added correctly", c.getMethods().get(0).get(0), "attr1");

        // Add a second method
        assertTrue("Adding method succeeds", c.addMethod("attr2"));
        assertEquals("Methods ArrayList has size 2", c.getMethods().size(), 2);
        assertEquals("Method is added correctly", c.getMethods().get(1).get(0), "attr2");

        // Add two more methods
        assertTrue("Adding method succeeds", c.addMethod("attr3"));
        assertTrue("Adding method succeeds", c.addMethod("attr4"));
        assertEquals("Methods ArrayList has size 4", c.getMethods().size(), 4);
        assertEquals("Method is added correctly", c.getMethods().get(2).get(0), "attr3");
        assertEquals("Method is added correctly", c.getMethods().get(3).get(0), "attr4");

        // Make sure that the capacity is ensured when exceeding 100 methods
        for (int i = 5; i < 105; ++i) {
            assertTrue("Adding method succeeds", c.addMethod("attr" + i));
        }
        assertEquals("Methods ArrayList has size 104", c.getMethods().size(), 104);
        assertEquals("Method is added correctly", c.getMethods().get(103).get(0), "attr104");

        // Make sure duplicates are not allowed
        assertTrue("addMethod returns false when adding dup", !c.addMethod("attr1"));
        assertEquals("Methods ArrayList has size 104", c.getMethods().size(), 104);
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(0).get(0), "attr1");
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(103).get(0), "attr104");

        assertTrue("addMethod returns false when adding dup", !c.addMethod("attr104"));
        assertEquals("Methods ArrayList has size 104", c.getMethods().size(), 104);
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(0).get(0), "attr1");
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(103).get(0), "attr104");

        assertTrue("addMethod returns false when adding dup", !c.addMethod("attr53"));
        assertEquals("Methods ArrayList has size 104", c.getMethods().size(), 104);
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(0).get(0), "attr1");
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(103).get(0), "attr104");
        assertEquals("Methods stays the same when adding dup", c.getMethods().get(52).get(0), "attr53");
    }

    // Depends on addField and addMethod working
    @Test
    public void deleteAttrTest() {
        UMLClass c = new UMLClass("className");

        // FIELD TESTS

        // Add and remove 1 field
        c.addField("attr1");
        assertEquals("Fields ArrayList has size 1", c.getFields().size(), 1);
        assertTrue("Deleting field succeeds", c.deleteAttr("attr1", "field"));
        assertEquals("Fields ArrayList has size 0", c.getFields().size(), 0);

        // Remove from the empty list
        assertTrue("deleteAttr returns false on nonexsitant attrs", !c.deleteAttr("attr1", "field"));
        assertTrue("Fields ArrayList is empty", c.getFields().isEmpty());

        // Add 2 fields and remove the 2nd
        c.addField("attr1");
        c.addField("attr2");
        assertEquals("Fields ArrayList has size 2", c.getFields().size(), 2);
        assertTrue("Deleting field succeeds", c.deleteAttr("attr2", "field"));
        assertEquals("Fields ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("Field stays in the list when another is deleted", c.getFields().get(0), "attr1");

        // Add 2 more fields and remove the 1st from the previous test
        c.addField("attr3");
        c.addField("attr4");
        assertEquals("Fields ArrayList has size 3", c.getFields().size(), 3);
        assertTrue("Deleting field succeeds", c.deleteAttr("attr1", "field"));
        assertEquals("Fields ArrayList has size 2", c.getFields().size(), 2);
        assertEquals("Field stays in the list when another is deleted", c.getFields().get(0), "attr3");
        assertEquals("Field stays in the list when another is deleted", c.getFields().get(1), "attr4");

        // Add 100 more fields and delete all but one
        for (int i = 5; i < 105; ++i) {
            c.addField("attr" + i);
        }
        assertEquals("Fields ArrayList has size 102", c.getFields().size(), 102);
        assertEquals("First field is correct after adding many fields", c.getFields().get(0), "attr3");
        assertEquals("Last field is correct after adding many fields", c.getFields().get(101), "attr104");

        for (int i = 3; i < 104; ++i) {
            assertTrue("Deleting field succeeds", c.deleteAttr("attr" + i, "field"));
        }
        assertEquals("Fields ArrayList has size 1", c.getFields().size(), 1);
        assertEquals("First field is correct after deleting many fields", c.getFields().get(0), "attr104");

        // METHOD TESTS

        // Add and remove 1 method
        c.addMethod("attr1");
        assertEquals("Methods ArrayList has size 1", c.getMethods().size(), 1);
        assertTrue("Deleting method succeeds", c.deleteAttr("attr1", "method"));
        assertEquals("Methods ArrayList has size 0", c.getMethods().size(), 0);

        // Remove from the empty list
        assertTrue("deleteAttr returns false on nonexsitant attrs", !c.deleteAttr("attr1", "method"));
        assertTrue("Relationships ArrayList is empty", c.getRels().isEmpty());

        // Add 2 methods and remove the 2nd
        c.addMethod("attr1");
        c.addMethod("attr2");
        assertEquals("Methods ArrayList has size 2", c.getMethods().size(), 2);
        assertTrue("Deleting method succeeds", c.deleteAttr("attr2", "method"));
        assertEquals("Methods ArrayList has size 1", c.getMethods().size(), 1);
        assertEquals("Method stays in the list when another is deleted", c.getMethods().get(0).get(0), "attr1");

        // Add 2 more methods and remove the 1st from the previous test
        c.addMethod("attr3");
        c.addMethod("attr4");
        assertEquals("Methods ArrayList has size 3", c.getMethods().size(), 3);
        assertTrue("Deleting method succeeds", c.deleteAttr("attr1", "method"));
        assertEquals("Methods ArrayList has size 2", c.getMethods().size(), 2);
        assertEquals("Method stays in the list when another is deleted", c.getMethods().get(0).get(0), "attr3");
        assertEquals("Method stays in the list when another is deleted", c.getMethods().get(1).get(0), "attr4");

        // Add 100 more methods and delete all but one
        for (int i = 5; i < 105; ++i) {
            c.addMethod("attr" + i);
        }
        assertEquals("Methods ArrayList has size 102", c.getMethods().size(), 102);
        assertEquals("First method is correct after adding many methods", c.getMethods().get(0).get(0), "attr3");
        assertEquals("Last method is correct after adding many methods", c.getMethods().get(101).get(0), "attr104");

        for (int i = 3; i < 104; ++i) {
            assertTrue("Deleting Method succeeds", c.deleteAttr("attr" + i, "method"));
        }
        assertEquals("Methods ArrayList has size 1", c.getMethods().size(), 1);
        assertEquals("First attr is correct after deleting many attrs", c.getMethods().get(0).get(0), "attr104");
    }

    @Test
    public void renameAttrTest() {
        UMLClass c = new UMLClass("className");

        // FIELD TESTS

        // Try to rename on empty field list
        assertTrue("Renaming method/field fails", !c.renameAttr("attr1", "attr2", "field"));
        assertEquals("Fields ArrayList has size 0", c.getFields().size(), 0);

        // Add and rename one field
        c.addField("attr1");
        assertEquals("Fields ArrayList has size 1", c.getFields().size(), 1);
        assertTrue("Renaming method/field succeeds", c.renameAttr("attr1", "attr2", "field"));
        assertEquals("Fields ArrayList has size 1 after renaming", c.getFields().size(), 1);
        assertEquals("First field is correct after renaming", c.getFields().get(0), "attr2");

        // Add 2 more fields and rename the last one
        c.addField("attr3");
        c.addField("attr4");
        assertEquals("Fields ArrayList has size 3", c.getFields().size(), 3);
        assertTrue("Renaming method/field succeeds", c.renameAttr("attr4", "attr5", "field"));
        assertEquals("Fields ArrayList has size 3 after rename", c.getFields().size(), 3);
        assertEquals("First field is correct after renaming third", c.getFields().get(0), "attr2");
        assertEquals("Second field is correct after renaming others", c.getFields().get(1), "attr3");
        assertEquals("Third field is correct after renaming", c.getFields().get(2), "attr5");

        // Delete a renamed field
        c.deleteAttr("attr5", "field");
        assertEquals("Fields ArrayList has size 2", c.getFields().size(), 2);

        // Delete all fields
        c.deleteAttr("attr2", "field");
        c.deleteAttr("attr3", "field");
        assertEquals("Fields ArrayList has size 0 after deleting", c.getFields().size(), 0);

        // Attempt to rename deleted field
        assertTrue("Deleted field cannot be renamed", !c.renameAttr("attr3", "attr6", "field"));
        assertEquals("Fields ArrayList has size 0", c.getFields().size(), 0);

        // Add >100 fields and rename the first and last
        for (int i = 0; i < 107; ++i) {
            c.addField("attr" + i);
        }
        assertEquals("Fields ArrayList has size 107", c.getFields().size(), 107);
        assertTrue("First field can be renamed when fields > 100", c.renameAttr("attr0", "coolAttr", "field"));
        assertTrue("Last field can be renamed when fields > 100", c.renameAttr("attr106", "coolerAttr", "field"));
        assertEquals("First field is correct after renaming when fields > 100", c.getFields().get(0), "coolAttr");
        assertEquals("Last field is correct after renaming when fields > 100", c.getFields().get(106), "coolerAttr");

        // METHOD TESTS

        // Try to rename on empty method list
        assertTrue("Renaming method/field fails", !c.renameAttr("attr1", "attr2", "method"));
        assertEquals("Methods ArrayList has size 0", c.getMethods().size(), 0);

        // Add and rename one method
        c.addMethod("attr1");
        assertEquals("Methods ArrayList has size 1", c.getMethods().size(), 1);
        assertTrue("Renaming method/field succeeds", c.renameAttr("attr1", "attr2", "method"));
        assertEquals("Methods ArrayList has size 1 after renaming", c.getMethods().size(), 1);
        assertEquals("First method is correct after renaming", c.getMethods().get(0).get(0), "attr2");

        // Add 2 more methods and rename the last one
        c.addMethod("attr3");
        c.addMethod("attr4");
        assertEquals("Methods ArrayList has size 3", c.getMethods().size(), 3);
        assertTrue("Renaming method/field succeeds", c.renameAttr("attr4", "attr5", "method"));
        assertEquals("Methods ArrayList has size 3 after rename", c.getMethods().size(), 3);
        assertEquals("First method is correct after renaming third", c.getMethods().get(0).get(0), "attr2");
        assertEquals("Second method is correct after renaming others", c.getMethods().get(1).get(0), "attr3");
        assertEquals("Third method is correct after renaming", c.getMethods().get(2).get(0), "attr5");

        // Delete a renamed method
        c.deleteAttr("attr5", "method");
        assertEquals("methods ArrayList has size 2", c.getMethods().size(), 2);

        // Delete all methods
        c.deleteAttr("attr2", "method");
        c.deleteAttr("attr3", "method");
        assertEquals("methods ArrayList has size 0 after deleting", c.getMethods().size(), 0);

        // Attempt to rename deleted method
        assertTrue("Deleted method cannot be renamed", !c.renameAttr("attr3", "attr6", "method"));
        assertEquals("methods ArrayList has size 0", c.getMethods().size(), 0);

        // Add >100 methods and rename the first and last
        for (int i = 0; i < 107; ++i) {
            c.addMethod("attr" + i);
        }
        assertEquals("methods ArrayList has size 107", c.getMethods().size(), 107);
        assertTrue("First method can be renamed when methods > 100", c.renameAttr("attr0", "coolAttr", "method"));
        assertTrue("Last method can be renamed when methods > 100", c.renameAttr("attr106", "coolerAttr", "method"));
        assertEquals("First method is correct after renaming when methods > 100", c.getMethods().get(0).get(0), "coolAttr");
        assertEquals("Last method is correct after renaming when methods > 100", c.getMethods().get(106).get(0), "coolerAttr");
    }

    @Test
    public void addParamTest() {
        UMLClass c = new UMLClass("className");
        c.addMethod("method");
    }

    @Test
    public void deleteParamTest() {
        UMLClass c = new UMLClass("className");
    }

    @Test
    public void deleteAllParamsTest() {
        UMLClass c = new UMLClass("className");
    }

    @Test
    public void changeParamTest() {
        UMLClass c = new UMLClass("className");
    }

    @Test
    public void changeAllParamsTest() {
        UMLClass c = new UMLClass("className");
    }

    @Test
    public void saveClassTest() {
        UMLClass c = new UMLClass("className");
    }
}