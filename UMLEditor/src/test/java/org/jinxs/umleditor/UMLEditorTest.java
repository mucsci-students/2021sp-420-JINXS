package org.jinxs.umleditor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * Unit test for UMLEditor.
 */
public class UMLEditorTest {
    
    // EDITOR TESTS: CONSTRUCTOR
    // --------------------------------------------
    @Test
    public void testConstructor() {
        UMLEditor editor = new UMLEditor();

        assertTrue("Testing UMLEditor constructor", editor.noop());
        assertTrue("Class list should be empty", editor.getClasses().isEmpty());
    }

    // CLASS TESTS: ADD, DELETE, RENAME
    // --------------------------------------------
    @Test
    public void addOneClassTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");

        assertEquals("Class list should contain one class", editor.getClasses().size(), 1);
        assertEquals("Class should be named class1", editor.getClasses().get(0).name, "class1");
    }

    // Depends on addClass working
    @Test
    public void addInvalidClassNameTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("1class");

        assertTrue("Class list should be empty (add was rejected)", editor.getClasses().isEmpty());

        editor.addClass("_validName");

        assertEquals("Class list should contain one class", editor.getClasses().size(), 1);
        assertEquals("Class should be named _validName", editor.getClasses().get(0).name, "_validName");

        editor.addClass("1nvalidName");

        assertEquals("Class list should contain one class (add was rejected)", editor.getClasses().size(), 1);

        editor.addClass("invalidName!");

        assertEquals("Class list should contain one class (add was rejected)", editor.getClasses().size(), 1);
    }

    // Depends on addClass working
    @Test
    public void DeleteOneClassTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.deleteClass("class1");

        assertTrue("Class list should be empty", editor.getClasses().isEmpty());
    }

    // Depends on addClass and deleteClass working
    @Test
    public void addAndDeleteManyClassesTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");

        assertEquals("Class list should contain one class", editor.getClasses().size(), 1);

        editor.addClass("class2");

        assertEquals("Class list should contain two classes", editor.getClasses().size(), 2);

        editor.deleteClass("class2");

        assertEquals("Class list should contain one class", editor.getClasses().size(), 1);

        editor.addClass("class3");

        assertEquals("Class list should contain two classes", editor.getClasses().size(), 2);

        editor.deleteClass("class1");
        editor.deleteClass("class3");

        assertTrue("Class list should be empty", editor.getClasses().isEmpty());
    }

    // Depends on addClass working
    @Test
    public void renameClassTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("className");
        editor.renameClass("className", "newName");

        assertEquals("Class list should contain one class", editor.getClasses().size(), 1);
        assertEquals("Class name should be newName", editor.getClasses().get(0).name, "newName");
    }

    // RELATIONSHIP TESTS: ADD, DELETE, CHANGETYPE
    // --------------------------------------------
    @Test
    public void addRelTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addClass("class2");
        editor.addRel("class1", "class2", "aggregation");

        assertEquals("class1 should have one relationship", editor.getClasses().get(0).getRels().size(), 1);
        assertEquals("class2 should have one relationship", editor.getClasses().get(1).getRels().size(), 1);
    }

    // Depends on addRel working
    @Test
    public void invalidRelTypeTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addClass("class2");
        editor.addRel("class1", "class2", "invalidType");

        assertTrue("class1 should have no relationships (request was rejected)", editor.getClasses().get(0).getRels().isEmpty());
        assertTrue("class2 should have no relationships (request was rejected)", editor.getClasses().get(1).getRels().isEmpty());

        editor.addRel("class1", "class2", "association");

        assertEquals("class1 should have one relationship", editor.getClasses().get(0).getRels().size(), 1);
        assertEquals("class2 should have one relationship", editor.getClasses().get(1).getRels().size(), 1);
        assertEquals("Relationship types should be the same for both classes", editor.getClasses().get(0).getRels().get(0).get(2), editor.getClasses().get(1).getRels().get(0).get(2));
    }

    // Depends on addRel working
    @Test
    public void delRelTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addClass("class2");
        editor.addRel("class1", "class2", "association");

        assertEquals("class1 should have one relationship", editor.getClasses().get(0).getRels().size(), 1);
        assertEquals("class2 should have one relationship", editor.getClasses().get(1).getRels().size(), 1);
        assertEquals("Relationship types should be the same for both classes", editor.getClasses().get(0).getRels().get(0).get(2), editor.getClasses().get(1).getRels().get(0).get(2));

        editor.delRel("class1", "class2");

        assertTrue("class1 should have zero relationships", editor.getClasses().get(0).getRels().isEmpty());
        assertTrue("class2 should have zero relationships", editor.getClasses().get(1).getRels().isEmpty());
    }

    // Depends on addRel working
    @Test
    public void changeRelTypeTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addClass("class2");
        editor.addRel("class1", "class2", "association");
        editor.changeRelType("class1", "class2", "aggregation");

        assertEquals("class1 should have one relationship", editor.getClasses().get(0).getRels().size(), 1);
        assertEquals("class2 should have one relationship", editor.getClasses().get(1).getRels().size(), 1);
        assertEquals("Relationship types should be the same for both classes", editor.getClasses().get(0).getRels().get(0).get(2), editor.getClasses().get(1).getRels().get(0).get(2));
    }

    // Depends on changeRelType working
    @Test
    public void changeRelInvalidTypeTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addClass("class2");
        editor.addRel("class1", "class2", "association");
        editor.changeRelType("class1", "class2", "invalidType");

        assertEquals("Relationship type for both classes should remain unchanged", editor.getClasses().get(0).getRels().get(0).get(0), "association");
        assertEquals("Relationship for both classes should still be association", editor.getClasses().get(0).getRels().get(0).get(0), editor.getClasses().get(1).getRels().get(0).get(0));
    }

    // FIELD TESTS: ADD, DELETE, RENAME
    // --------------------------------------------
    @Test
    public void addFieldAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newField", "field");

        assertEquals("class1 should have one field", editor.getClasses().get(0).getAttrs().size(), 1);
        assertEquals("Field named newField should have been added to class1", editor.getClasses().get(0).getAttrs().get(0), "newField");
    }

    // Depends on addAttr working
    @Test
    public void delFieldAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.delAttr("class1", "field1", "field");

        assertTrue("class1 should have zero fields", editor.getClasses().get(0).getAttrs().isEmpty());
    }
    // Depends on addAttr working
    @Test
    public void addAndDeleteManyFieldAttrsTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.addAttr("class1", "field2", "field");

        assertEquals("class1 should have two fields", editor.getClasses().get(0).getAttrs().size(), 2);

        editor.delAttr("class1", "field1", "field");

        assertEquals("class1 should have one field", editor.getClasses().get(0).getAttrs().size(), 1);

        editor.delAttr("class1", "field2", "field");

        assertTrue("class1 should have zero fields", editor.getClasses().get(0).getAttrs().isEmpty());
    }

    // Depends on addAttr working
    @Test
    public void renameFieldAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.renameAttr("class1", "field1", "field2", "field");

        assertEquals("class1 should have one field named field2", editor.getClasses().get(0).getAttrs().get(0), "field2");
    }

    // METHOD TESTS: ADD, DELETE, RENAME
    // --------------------------------------------
    
}
