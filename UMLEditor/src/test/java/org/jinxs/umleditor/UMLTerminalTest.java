package org.jinxs.umleditor;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * Unit tests for Main.
 */
public class UMLTerminalTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    // Thanks to Antonio Vinicius Menezes Medei on stack overflow
    // for the helper methods that redirect Strings to System.in
    // in order to simulate input from a user on our command-line
    // interface
    // https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    // All "expected" strings use .replaceAll with the System.getProperty
    // method to properly replace newline characters depending on the platform
    // the tests are being run from. Running the tests on Windows requires "\r"
    // to be placed in front of every newline character while Linux and Mac do
    // not.

    // "add" command tests
    // **********************************************************************

    // "add class" tests
    // ------------------------------------

    @Test
    public void addSimpleClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class"); 
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        assertEquals("Adding a simple class succeeds", "", getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "The requested class name already exists\n";

        assertEquals("Adding a duplicate class fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addNumClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("1class");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "The class name \"1class\" is not valid\n";

        assertEquals("Adding a class starting with a number fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addSpecCharClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class^");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "The class name \"class^\" is not valid\n";

        assertEquals("Adding a class with a special character fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add rel" tests
    // ------------------------------------

    @Test
    public void addSimpleRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("inheritance");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Adding a simple relationship succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("inheritance");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("inheritance");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Relationship between \"class1\" and \"class2\" already exists\n";

        assertEquals("Adding a duplicate relationship fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addInvalidRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("coolreltype");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Missing a Type, Please try again\n";

        assertEquals("Adding an invalid rel fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addMissingRelType() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "Too few Arguments for addRel command\n";

        assertEquals("Adding a rel with no type fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addRelNonexistantClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("inheritance");

        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Class does not exsist\n";

        assertEquals("Adding a duplicate relationship fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add field" tests
    // ------------------------------------

    @Test
    public void addSimpleField() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("field");
        testString.add("class1");
        testString.add("int");
        testString.add("field1");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Adding a simple field succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupField() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("field");
        testString.add("class1");
        testString.add("int");
        testString.add("field1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("field");
        testString.add("class1");
        testString.add("int");
        testString.add("field1");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "Field \"field1\" is already a field of class \"class1\n";

        assertEquals("Adding a duplicate field fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addFieldNonexistantClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("field");
        testString.add("class1");
        testString.add("int");
        testString.add("field1");

        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Class \"class1\" does not currently exist\n";

        assertEquals("Adding a field to a nonexistant class fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add method" tests
    // ------------------------------------

    @Test
    public void addSimpleMethod() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Adding a simple method succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupMethod() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected ="Method \"method1\" is already a method of class \"class1\n";

        assertEquals("Adding a duplicate method fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addMethodNonexistantClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Class \"class1\" does not currently exist\n";

        assertEquals("Adding a method to a nonexistant class fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add param" tests
    // ------------------------------------

    @Test
    public void addSimpleParam() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("param");
        testString.add("class1");
        testString.add("method1");
        testString.add("int");
        testString.add("param1");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Adding a simple param succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupParam() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("method");
        testString.add("class1");
        testString.add("int");
        testString.add("method1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("param");
        testString.add("class1");
        testString.add("method1");
        testString.add("int");
        testString.add("param1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("param");
        testString.add("class1");
        testString.add("method1");
        testString.add("int");
        testString.add("param1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Parameter already exists\n";

        assertEquals("Adding a simple param succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete" command tests
    // **********************************************************************

    // "delete class" tests
    // ------------------------------------

    @Test
    public void deleteSimpleClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("delete");
        testString.add("class");
        testString.add("class1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Deleting a simple class succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantClass() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("delete");
        testString.add("class");
        testString.add("class1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "The requested class to delete does not exist\n";

        assertEquals("Deleting a nonexistant class does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete rel" tests
    // ------------------------------------

    @Test
    public void deleteSimpleRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("realization");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("delete");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Deleting a simple relationship succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("delete");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Relationship between \"class1\" and \"class2\" does not exsist\n";

        assertEquals("Deleting a nonexistant relationship does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete field" tests
    // ------------------------------------

    @Test
    public void deleteSimpleField() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("field");
        testString.add("class1");
        testString.add("int");
        testString.add("field1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("delete");
        testString.add("field");
        testString.add("class1");
        testString.add("field1");
    
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Deleting a simple field succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantField() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("delete");
        testString.add("field");
        testString.add("class1");
        testString.add("field1");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Attribute \"field1\" does not exist\n";

        assertEquals("Deleting a nonexistant field does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // Miscellaneous command tests
    // **********************************************************************

    @Test
    public void testHelp() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("help");

        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Command/shortcut	          	                        Description\n" +
            "-------------------------	                            --------------\n" +
            "add\n" +
            "    class <class1>                                      Creates a new UML class\n" +
            "    rel <class1, class2, relType>                       Adds a relationship between the two given classes\n" +
            "    valid relationship types:\n" +
            "    \"inheritance\", \"realization\", \"aggregation\", \"composition\"\n" +
            "    field <class1, dataType, field1>                    Adds a field to a given class\n" +
            "    method <class1, returnType, method1>                Adds a method to a given class\n" +
            "    param <class1, method1, dataType, param1>           Adds a parameter to a given method\n" +
            "\n" +
            "delete\n" +
            "    class <class1>                                      Deletes the specified class\n" +
            "    rel <class1, class2>                                Deletes a relationship between the two given classes\n" +
            "    field <class1>                                      Deletes the given field from the specified class\n" +
            "    method <class1>                                     Deletes the given method from the specified class\n" +
            "    param <class1, method1, param1>                     Deletes the given parameter from the specified class\n" +
            "    allParams <class1, method1>                         Deletes all parameters from the specified class\n" +
            "\n" +
            "rename\n" +
            "    class <class1, newName>                             Renames a class to a new specified class name\n" +
            "    field <class1, field1, newfieldName>                Renames the given field name from the specified class\n" +
            "    method <class1, method1, newMethodName>             Renames the given field name from the specified class\n" +
            "    param <class1, method1, param1, newParamName>       Renames the given parameter from the specified class\n" +
            "    allParams <class1, method1, type1, param1, ...>     Changes all the parameters from the specified class\n" +
            "\n" +
            "retype\n" +
            "    rel <class1, class2, newRelType>                Changes the type of relationship\n" +
            "    field <class1, field1, newDataType>             Changes the data type of a field\n" +
            "    method <class1, method1, newReturnType>         Changes the return type of a method\n" +
            "    param <class1, method1, param1, newDataType>    Changes the data type of a parameter\n" +
            "\n" +
            "\n" +
            "copy <className, newClassName>  Copies an existing class and names it to a new name\n" +
            "printList                       Prints the names of all existing classes\n" +
            "printContents <class1>          Prints the contents of a given class\n" +
            "printRel <class1>               Prints the all the relationships between classes\n" +
            "help                            Prints a help document with all viable commands\n" +
            "quit                            Exits the the program\n" +
            "save <fileName>                 Saves the project into a JSON file\n" +
            "load <fileName>                 Loads a project from a JSON file\n" +
            "undo                            Restores the previous state before the last command called\n" +
            "redo                            Restores the state reversed by calling undo\n";

        assertEquals("Help command prints the help doc", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void undoTest() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("undo");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Undo should print nothing", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void undoWithExtraArgs() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("undo");
        testString.add("test");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Too many Arguments for undo command\n";

        assertEquals("Command should not have been recognized", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void redoTest() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("redo");

        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("Redo should print nothing", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void redoWithExtraArgs() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("redo");
        testString.add("test");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "Too many Arguments for redo command\n";

        assertEquals("Command should not have been recognized", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void undoOne() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("undo");

        terminal.interpreter(testString);
        testString.clear();
 
        testString.add("printList");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "";

        assertEquals("There shouldn't be any classes", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void undoRel() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("class");
        testString.add("class2");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("add");
        testString.add("rel");
        testString.add("class1");
        testString.add("class2");
        testString.add("inheritance");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("undo");
        
        terminal.interpreter(testString);
        testString.clear();
 
        testString.add("printList");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "class1\nclass2\n";

        assertEquals("There should be two classes with no relationship", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void redoOne() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();
        
        testString.add("undo");
        
        terminal.interpreter(testString);
        testString.clear();
 
        testString.add("redo");
        
        terminal.interpreter(testString);
        testString.clear();
 
        testString.add("printList");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "class1\n";

        assertEquals("There should only be one class", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void redoMoreThanUndo() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();
        
        testString.add("undo");
        
        terminal.interpreter(testString);
        testString.clear();

        testString.add("redo");
        
        terminal.interpreter(testString);
        testString.clear();

        testString.add("redo");
        
        terminal.interpreter(testString);
        testString.clear();

        testString.add("printList");
        
        terminal.interpreter(testString);
        testString.clear();

        String expected = "class1\n";

        assertEquals("There should only be one class", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void copyClassTest() {
        ArrayList<String> testString = new ArrayList<String>();

        UMLTerminal terminal = new UMLTerminal();

        testString.add("add");
        testString.add("class");
        testString.add("class1");

        terminal.interpreter(testString);
        testString.clear();

        testString.add("copy");
        testString.add("class1");
        testString.add("class2");

        String expected = "";

        assertEquals("Adding a simple relationship succeeds", expected, getOutput().replaceAll("\r", ""));
    }
}
