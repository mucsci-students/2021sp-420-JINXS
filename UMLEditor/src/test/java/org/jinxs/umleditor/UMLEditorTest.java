package org.jinxs.umleditor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

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

        assertEquals("class1 should have one field", editor.getClasses().get(0).getFields().size(), 1);
        assertEquals("Field named newField should have been added to class1", editor.getClasses().get(0).getFields().get(0), "newField");
    }

    // Depends on addAttr working
    @Test
    public void delFieldAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.delAttr("class1", "field1", "field");

        assertTrue("class1 should have zero fields", editor.getClasses().get(0).getFields().isEmpty());
    }
    // Depends on addAttr working
    @Test
    public void addAndDeleteManyFieldAttrsTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.addAttr("class1", "field2", "field");

        assertEquals("class1 should have two fields", editor.getClasses().get(0).getFields().size(), 2);

        editor.delAttr("class1", "field1", "field");

        assertEquals("class1 should have one field", editor.getClasses().get(0).getFields().size(), 1);

        editor.delAttr("class1", "field2", "field");

        assertTrue("class1 should have zero fields", editor.getClasses().get(0).getFields().isEmpty());
    }

    // Depends on addAttr working
    @Test
    public void renameFieldAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "field1", "field");
        editor.renameAttr("class1", "field1", "field2", "field");

        assertEquals("class1 should have one field named field2", editor.getClasses().get(0).getFields().get(0), "field2");
    }

    // METHOD TESTS: ADD, DELETE, RENAME
    // --------------------------------------------

    @Test
    public void addMethodAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");

        assertEquals("class1 should have one method", editor.getClasses().get(0).getMethods().size(), 1);
        assertEquals("Method named newMethod should have been added to class1", editor.getClasses().get(0).getMethods().get(0).get(0), "newMethod");
    }
    
    // Depends on addAttr working
    @Test
    public void delMethodAttrTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.delAttr("class1", "newMethod", "method");

        assertTrue("class1 should have zero methods", editor.getClasses().get(0).getMethods().isEmpty());
    }

        // Depends on addAttr working
    @Test
    public void delMethodWithParamsTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");
        editor.delAttr("class1", "newMethod", "method");

        assertTrue("class1 should have zero methods", editor.getClasses().get(0).getMethods().isEmpty());
    }

     // Depends on addAttr working
     @Test
     public void addAndDeleteManyMethodAttrsTest() {
         UMLEditor editor = new UMLEditor();
         editor.addClass("class1");
         editor.addAttr("class1", "method1", "method");
         editor.addAttr("class1", "method2", "method");
 
         assertEquals("class1 should have two methods", editor.getClasses().get(0).getMethods().size(), 2);
 
         editor.delAttr("class1", "method1", "method");
 
         assertEquals("class1 should have one method", editor.getClasses().get(0).getMethods().size(), 1);
 
         editor.delAttr("class1", "method2", "method");
 
         assertTrue("class1 should have zero methods", editor.getClasses().get(0).getMethods().isEmpty());
     }

     // Depends on addAttr working
     @Test
     public void renameMethodAttrTest() {
         UMLEditor editor = new UMLEditor();
         editor.addClass("class1");
         editor.addAttr("class1", "newMethod", "method");
         editor.renameAttr("class1", "newMethod", "newName", "method");
 
         assertEquals("class1 should have one method named newName", editor.getClasses().get(0).getMethods().get(0).get(0), "newName");
     }

     // PARAMETER TESTS: ADD, DELETE, RENAME
    // --------------------------------------------

     // Depends on addAttr working
    @Test
    public void addParamTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");

        assertEquals("newMethod should have one parameter", editor.getClasses().get(0).getMethods().size(), 1);
        assertEquals("Parameter named param1 should have been added to newMethod", editor.getClasses().get(0).getMethods().get(0).get(1), "param1");
    }

    boolean deleteParamCheck(ArrayList<String> method){
        if (method.size() != 1){
            return false;
        }

        return true;
    }

    // Depends on addAttr working
    @Test
    public void delParamTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");
        editor.deleteParam("class1", "newMethod", "param1");

        assertTrue("newMethod should have zero parameters",  deleteParamCheck(editor.getClasses().get(0).getMethods().get(0)));
    }

    // Depends on addAttr working
    @Test
    public void delAllParamsTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");
        editor.addParam("class1", "newMethod", "param2");
        editor.addParam("class1", "newMethod", "param3");
        editor.deleteAllParams("class1", "newMethod");

        assertTrue("newMethod should have zero parameters",  deleteParamCheck(editor.getClasses().get(0).getMethods().get(0)));
    }

      // Depends on addAttr working
      @Test
      public void delAllParamsTest() {
          UMLEditor editor = new UMLEditor();
          editor.addClass("class1");
          editor.addAttr("class1", "newMethod", "method");
          editor.addParam("class1", "newMethod", "param1");
          editor.addParam("class1", "newMethod", "param2");
          editor.addParam("class1", "newMethod", "param3");
          editor.deleteAllParams("class1", "newMethod");
  
          assertTrue("newMethod should have zero parameters",  deleteParamCheck(editor.getClasses().get(0).getMethods().get(0)));
      }

    // Depends on addAttr working
    @Test
    public void changeParamTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");
        editor.changeParam("class1", "newMethod", "param1", "newName");

        assertEquals("newMethod should have one param named newName", editor.getClasses().get(0).getMethods().get(0).get(1), "newName");
    }

    // Depends on addAttr working
    @Test
    public void changeAllParamsTest() {
        UMLEditor editor = new UMLEditor();
        editor.addClass("class1");
        editor.addAttr("class1", "newMethod", "method");
        editor.addParam("class1", "newMethod", "param1");
        editor.addParam("class1", "newMethod", "param2");
        ArrayList<String> str = new ArrayList<String>();
        str.add("one");
        str.add("two");
        str.add("Three");
        editor.changeAllParams("class1", "newMethod", str);

        assertEquals("newMethod should have one param named one", editor.getClasses().get(0).getMethods().get(0).get(1), "one");
    }

}
