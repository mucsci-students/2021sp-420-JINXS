package org.jinxs.umleditor;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * Unit tests for UMLInterface.
 */
public class UMLInterfaceTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
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

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
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
        final String testString = "add class class1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ ";

        assertEquals("Adding a simple class succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupClass() {
        final String testString = "add class class1\n" + "add class class1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ The requested class name already exists\n"
                + "$ ";

        assertEquals("Adding a duplicate class fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addNumClass() {
        final String testString = "add class 1class\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class name cannot start with a number\n"
                + "$ ";

        assertEquals("Adding a class starting with a number fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addSpecCharClass() {
        final String testString = "add class class^\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ The class name cannot contain special characters or spaces\n"
                + "$ ";

        assertEquals("Adding a class with a special character fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add rel" tests
    // ------------------------------------

    @Test
    public void addSimpleRel() {
        final String testString = "add class class1\n" + "add class class2\n" + "add rel class1 class2 inheritance\n"
                + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Class \"class2\" was added successfully\n"
                + "$ Relationship between \"class1\" and \"class2\" added successfully\n"
                + "$ ";

        assertEquals("Adding a simple relationship succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupRel() {
        final String testString = "add class class1\n" + "add class class2\n" + "add rel class1 class2 inheritance\n"
                + "add rel class1 class2 inheritance\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Class \"class2\" was added successfully\n"
                + "$ Relationship between \"class1\" and \"class2\" added successfully\n"
                + "$ Relationship between \"class1\" and \"class2\" already exists\n"
                + "$ ";

        assertEquals("Adding a duplicate relationship fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addInvalidRel() {
        final String testString = "add class class1\n" + "add class class2\n" + "add rel class1 class2 coolreltype\n"
                + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Class \"class2\" was added successfully\n"
                + "$ Missing a Type, Please try again\n"
                + "$ ";

        assertEquals("Adding an invalid rel fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addMissingRelType() {
        final String testString = "add class class1\n" + "add class class2\n" + "add rel class1 class2\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Class \"class2\" was added successfully\n"
                + "$ Too few Arguments for addRel command\n"
                + "$ ";

        assertEquals("Adding a rel with no type fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addRelNonexistantClass() {
        final String testString = "add rel class1 class2 inheritance\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class does not exsist\n"
                + "$ ";

        assertEquals("Adding a duplicate relationship fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add field" tests
    // ------------------------------------

    @Test
    public void addSimpleField() {
        final String testString = "add class class1\n" + "add field class1 field1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ Field \"field1\" added to class \"class1\" succesfully\n"
                + "$ ";

        assertEquals("Adding a simple field succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupField() {
        final String testString = "add class class1\n" + "add field class1 field1\n" + "add field class1 field1\n"
                + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ Field \"field1\" added to class \"class1\" succesfully\n"
                + "$ field \"field1\" is already a field of class \"class1\n"
                + "$ ";

        assertEquals("Adding a duplicate field fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addFieldNonexistantClass() {
        final String testString = "add field class1 field1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" does not currently exist$ ".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));

        assertEquals("Adding a field to a nonexistant class fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add method" tests
    // ------------------------------------

    @Test
    public void addSimpleMethod() {
        final String testString = "add class class1\n" + "add method class1 method1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ method \"method1\" added to class \"class1\" succesfully\n"
                + "$ ";

        assertEquals("Adding a simple method succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupMethod() {
        final String testString = "add class class1\n" + "add method class1 method1\n" + "add method class1 method1\n"
                + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ method \"method1\" added to class \"class1\" succesfully\n"
                + "$ method \"method1\" is already a method of class \"class1\n"
                + "$ ";

        assertEquals("Adding a duplicate method fails", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addMethodNonexistantClass() {
        final String testString = "add method class1 method1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" does not currently exist$ ".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));

        assertEquals("Adding a method to a nonexistant class fails", expected, getOutput().replaceAll("\r", ""));
    }

    // "add param" tests
    // ------------------------------------

    @Test
    public void addSimpleParam() {
        final String testString = "add class class1\n" + "add method class1 method1\n"
                + "add param class1 method1 param1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ method \"method1\" added to class \"class1\" succesfully\n"
                + "$ Parameter \"param1\" was added successfully\n"
                + "$ ";

        assertEquals("Adding a simple param succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void addDupParam() {
        final String testString = "add class class1\n" + "add method class1 method1\n"
                + "add param class1 method1 param1\n" + "add param class1 method1 param1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ method \"method1\" added to class \"class1\" succesfully\n"
                + "$ Parameter \"param1\" was added successfully\n"
                + "$ Parameter already exists\n"
                + "$ ";

        assertEquals("Adding a simple param succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete" command tests
    // **********************************************************************

    // "delete class" tests
    // ------------------------------------

    @Test
    public void deleteSimpleClass() {
        final String testString = "add class class1\n" + "delete class class1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ Class \"class1\" was deleted successfully\n"
                + "$ ";

        assertEquals("Deleting a simple class succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantClass() {
        final String testString = "delete class class1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ The requested class to delete does not exist\n"
                + "$ ";

        assertEquals("Deleting a nonexistant class does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete rel" tests
    // ------------------------------------

    @Test
    public void deleteSimpleRel() {
        final String testString = "add class class1\n" + "add class class2\n" + "add rel class1 class2 association\n"
                + "delete rel class1 class2\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ Class \"class2\" was added successfully\n"
                + "$ Relationship between \"class1\" and \"class2\" added successfully\n"
                + "$ Relationship deleted\n"
                + "$ ";

        assertEquals("Deleting a simple relationship succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantRel() {
        final String testString = "add class class1\n" + "add class class2\n" + "delete rel class1 class2\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Class \"class2\" was added successfully\n"
                + "$ Relationship between \"class1\" and \"class2\" does not exsist\n"
                + "$ ";

        assertEquals("Deleting a nonexistant relationship does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // "delete field" tests
    // ------------------------------------

    @Test
    public void deleteSimpleField() {
        final String testString = "add class class1\n" + "add field class1 field1\n" + "delete field class1 field1\n"
                + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n"
                + "$ Field \"field1\" added to class \"class1\" succesfully\n"
                + "$ Attribute \"field1\" was deleted successfully\n"
                + "$ ";

        assertEquals("Deleting a simple field succeeds", expected, getOutput().replaceAll("\r", ""));
    }

    @Test
    public void deleteNonexistantField() {
        final String testString = "add class class1\n" + "delete field class1 field1\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Class \"class1\" was added successfully\n" + "$ Attribute \"field1\" does not exist\n"
                + "$ ";

        assertEquals("Deleting a nonexistant field does nothing", expected, getOutput().replaceAll("\r", ""));
    }

    // Miscellaneous command tests
    // **********************************************************************

    @Test
    public void testHelp() {
        final String testString = "help\n" + "quit";

        provideInput(testString);

        UMLInterface.main(new String[] { "--cli" });

        String expected = "$ Command/shortcut	          	                 Description\n"
                + "-------------------------	                     --------------\n" 
                + "add\n"
                + "    class <class1>                               Creates a new UML class\n"
                + "    rel <class1, class2, type>                   Adds a relationship between the two given classes\n"
                + "    valid relationship types:\n"
                + "    \"inheritance\"\n"
                + "    \"realization\"\n"
                + "    \"aggregation\"\n"
                + "    \"composition\"\n"
                + "    field <class1, field1>                       Adds a field to a given class\n"
                + "    method  <class1, method1>                    Adds a method to a given class\n"
                + "    param <class1, method1, param1>              Adds a parameter to a given method\n"
                + "delete\n"
                + "    class <class1>                               Deletes the specified class\n"
                + "    rel  <class1, class2>                        Deletes a relationship between the two given classes\n"
                + "    field  <class1>                              Deletes the given field from the specified class\n"
                + "    method  <class1>                             Deletes the given method from the specified class\n"
                + "    param <class1,method1, param1>               Deletes the given parameter from the specified class\n"
                + "    allParams <class1,method1>                   Deletes all parameters from the specified class\n\n"
                + "rename\n"
                + "    class <class1, newName>                      Renames a class to a new specified class name\n"
                + "    relType <class1, class2, newType>            Changes the type of relationship\n"
                + "    field <class1, field1, newfieldName>         Renames the given field name from the specified class\n"
                + "    method <class1, method1, newMethodName>      Renames the given field name from the specified class\n"
                + "    param <class1,method1, param1, newParamName> Renames the given parameter from the specified class\n"
                + "    allParams <class1, method1, params>          Changes all the parameters from the specified class\n\n" +

                "printList                                        Prints the names of all existing classes\n\n" +

                "printContents <class1>                           Prints the contents of a given class\n\n" +

                "printRel  <class1>                               Prints the all the relationships between classes\n\n" +

                "help                                             Prints a help document with all viable commands\n\n" +

                "quit                                             Exits the the program\n\n" +

                "save  <fileName>                                 Saves the project into a JSON file\n\n" +

                "load  <fileName>                                 Loads a project from a JSON file\n"
                + "$ ";

        assertEquals("Help command prints the help doc", expected, getOutput().replaceAll("\r", ""));
    }
}
