package org.jinxs.umleditor;

import java.util.ArrayList;

// For writing out to a file when saving
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// For the JSON array of classes to be written to file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// import jdk.internal.joptsimple.internal.Classes;
// import jdk.tools.jaotc.collect.ClassSearch;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UMLEditor {
    
    private ArrayList<UMLClass> classes;

    public UMLEditor() {
        classes = new ArrayList<UMLClass>();
    }

    // Adds a class to the list of classes given a new class name that is not already in use
    public void addClass(String className) {
        // Ensure the class name does not start with a number
        if (Character.isDigit(className.charAt(0))) {
            System.out.println("Class name cannot start with a number");
            return;
        }

        // Ensure the class name does not contain special characters (except for '_')
        // By matching with a regex
        Pattern p = Pattern.compile("[^a-z0-9_ ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(className);
        boolean containsSpecChars = m.find();

        if (containsSpecChars) {
            System.out.println("The class name cannot contain special characters or spaces");
            return;
        }

        classes.ensureCapacity(classes.size() + 1);
        // Loop through the list of classes to ensure that a duplicate class name is not trying to be inserted
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                System.out.println("The requested class name already exists");
                return;
            }
        }
        // Add the new class to the list of classes
        classes.add(new UMLClass(className));
        System.out.println("Class \"" + className + "\" was added successfully");
    }

    // Deletes a class from the list of classes given a class name that exists
    public void deleteClass(String className) {
        // Loop through the list of classes to ensure that the requested class to delete exists
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                // Save the requested class to delete in order to delete relationships first
                UMLClass deletedClass = classes.get(i);
                // Save the list of relationships
                ArrayList<ArrayList<String>> deletedRels = deletedClass.getRels();
                // Loop through the list of relationships and delete each one from the related class
                for (int j = 0; j < deletedRels.size(); ++j) {
                    delRel(deletedClass.name, deletedRels.get(j).get(0));
                }
                // Finally remove the requested class to delete from the class list
                classes.remove(i);
                System.out.println("Class \"" + className + "\" was deleted successfully");
                return;
            }
        }
        System.out.println("The requested class to delete does not exist");
    }

    /*
    * 
    */
    public void renameClass(String oldName, String newName) {
        // Ensure the class name does not contain special characters (except for '_')
        // By matching with a regex
        Pattern p = Pattern.compile("[^a-z0-9_ ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(newName);
        boolean containsSpecChars = m.find();

        if (containsSpecChars) {
            System.out.println("The class name cannot contain special characters or spaces");
            return;
        }

        UMLClass newNameClass = classExists(oldName);
        if (newNameClass != null) {
            newNameClass.name = newName;
        }
    }
    

    // Adds a relationship between class1 and class2 where class1 is the source
    // and class2 is the destination
    public void addRel(String class1, String class2, String type) {
        // Make sure each given class name is unique
        if (class1.equals(class2)) {
            System.out.println("Class names must be different");
            return;
        }
        /*******************Place holder type check*******************/
        if (type.equals("association") || type.equals("aggregation") || type.equals("composition") ||
            type.equals("generalization")){

        }else if (!(type.equals(""))){
            System.out.println("Missing a Type, Please try again");
            return;
        }else {
            System.out.println("Type is invalid");
            return;
        }

        // Store classes when found in the class list
        UMLClass c1 = null;
        UMLClass c2 = null;

        // Look through the class list for both classes
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(class1) && c1 == null) {
                c1 = classes.get(i);
            }
            if (classes.get(i).name.equals(class2) && c2 == null) {
                c2 = classes.get(i);
            }
        }

        if (c1 == null || c2 == null){
            System.out.println("Class does not exsist");
            return;
        }

        // Get the current relationships from class1
        ArrayList<ArrayList<String>> c1Rels = c1.getRels();

        // Ensure a relationship between class1 and class2 does not already exist
        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).get(0).equals(class2)) {
                // Notify user of already existing relationship
                System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" already exists");
                return;
            }
        }

        // Add the relationship to both class's rel lists
        // true = src, false = dest
        if (c1 != null && c2 != null) {
            c1.addRel(class2, true, type);
            c2.addRel(class1, false, type);
        }
        // Notify user of successful relationship addition
        System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" added successfully");
        }

    public void delRel(String class1, String class2){
        // Make sure each given class name is unique
        if (class1.equals(class2)) {
            System.out.println("Class names must be different");
            return;
        }

        // Store classes when found in the class list
        UMLClass c1 = null;
        UMLClass c2 = null;

        // Look through the class list for both classes
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(class1) && c1 == null) {
                c1 = classes.get(i);
            }
            if (classes.get(i).name.equals(class2) && c2 == null) {
                c2 = classes.get(i);
            }
        }

        if (c1 == null || c2 == null){
            System.out.println("Class does not exsist");
            return;
        }

        // Get the current relationships from class1
        ArrayList<ArrayList<String>> c1Rels = c1.getRels();

        boolean found = false;

        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).get(0).equals(class2)){
                found = true;
            }
        }

        if (found == false){
            System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" does not exsist");
            return;
        }

        else {
            c1.deleteRel(c2.name);
            c2.deleteRel(c1.name);
            System.out.println("Relationship deleted");
        }
    }

    /********************************************************************************
    * changeRelType
    * Changes the relationship type of the given relationship
    ********************************************************************************/
    public void changeRelType(String class1, String class2, String newType) {
        UMLClass c1 = classExists(class1);
        UMLClass c2 = classExists(class2);

        /******************* Place holder type check *******************/
        if (newType.equals("association") || newType.equals("aggregation") || newType.equals("composition")
                || newType.equals("generalization")) {

        } else if (!(newType.equals(""))) {
            System.out.println("Missing a Type, Please try again");
            return;
        } else {
            System.out.println("Type is invalid");
            return;
        }

        boolean res = c1.changeRelType(class2, newType) && c2.changeRelType(class1, newType);
        if (!res) {
            System.out.println("Changing the relationship type failed");
        }
    }

    /*******************************************************************************
    * addAttr will add the given attribtue to the current class as a field or method
    * Variables:
    * - className: The class the attribute will be added to
    * - attrName = name of the attribute that will be added
    * - type = determines if the attribute is a field or method
    * - classExists = boolean to check and see if the class already exist
    * - attrAdded = boolean that checks if the attribute was added succesfully
    ********************************************************************************/
    public void addAttr(String className, String attrName, String type) {
        
        boolean attrAdded = false;

        UMLClass currClass = classExists(className);

        if (currClass == null) {
            return;
        }

        // True if added succesfully, false if duplicate
        if (type.equals("field")){
            attrAdded = currClass.addField(attrName);
        }
        else if (type.equals("method")){
            attrAdded = currClass.addMethod(attrName);
        }

        // Notify the user of the resuls of the attribute addition

        if (attrAdded) {
            if (type.equals("field")){
                System.out.println("Field \"" + attrName + "\" added to class \"" + className + "\" succesfully");
            }else{
                System.out.println("method \"" + attrName + "\" added to class \"" + className + "\" succesfully");
            }
            
        } else {
            if (type.equals("field")){
                System.out.println("field \"" + attrName + "\" is already a field of class \"" + className);
            }else{
                System.out.println("method \"" + attrName + "\" is already a method of class \"" + className);
            }
            
        }
    }

  /*****************************************************************************************
  * DelAttr will delete the given attribute form the specified class
  * Variables:
  * - className = name of class to be accessed
  * - attributes = name of attribute to be removed
  * - attrExist = True means successfully deleted, False means attribute dose not exist
  * - currClass = name called to access the methods in UMLClass
  *
  * This method calls the deleteAttr method from the UMLClass file. It searches through the
  * Array List "classes" to find the given name and then passes the class name to currClass.
  * currClass is then used to call the deleteAttr method and will attempt to delete the 
  * attribtue. Changing the boolean variable "attrExist" to ture if it does.
  *****************************************************************************************/
    public void delAttr(String className, String attributes, String type){
        boolean attrExist = false;

    // Searches through arraylist classes, searching for className
        for (int i = 0; i < classes.size(); i++){
            if (className.equals(classes.get(i).name)){
                UMLClass currClass = classes.get(i);
                attrExist = currClass.deleteAttr(attributes, type);
            }
            // if false, then delete attempt has failed. attribute does not exist
            if (!attrExist){
                System.out.println ("Attribute \"" + attributes + "\" does not exist");
            }
            else {
                System.out.println ("Attribute \"" + attributes + "\" was deleted successfully");
            }
        }
    }
    
    // Renames an attribute (oldAttr) of the given className to newAttr
    public void renameAttr(String className, String oldAttr, String newAttr, String type) {
        // Ensure the new attribute name is different than the one being renamed
        if (oldAttr.equals(newAttr)) {
            System.out.println("The new attribute name must be different than the one being changed");
            return;
        }
        
        // Find the class that will have an attribute renamed
        for (int i = 0; i < classes.size(); ++i) {
          // If the class exists, attempt to rename the provided attribute name to the new name
            if (classes.get(i).name.equals(className)) {
            boolean success = classes.get(i).renameAttr(oldAttr, newAttr, type);
            if (success) {
                System.out.print("Attribute \"" + oldAttr + "\" for the class \"" + className + 
                                "\" successfully renamed to \"" + newAttr + "\"");
            }
            // else the class function renameAttr will notify the user if the new attribute was a duplicate
            // or if the old attribute was not found
            return;
            }
        }
    
        // If the for loop cycles all the way through without returning, the class does not exist
        System.out.print("Class \"" + className + "\" does not currently exist");
    }

    public void addParam(String className, String methName, String paramName){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return;
        }

        foundClass.addParam(methName, paramName);
    }

    public void deleteParam(String className, String methName, String paramName){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return;
        }

        foundClass.deleteParam(methName, paramName);
    }

    public void deleteAllParams(String className, String methName){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return;
        }

        foundClass.deleteAllParams(methName);
    }

    public void changeParam(String className, String methName, String paramName){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return;
        }

        foundClass.changeParam(methName, paramName);
    }

    public void changeAllParams(String className, String methName, ArrayList<String> params){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return;
        }
        foundClass.changeAllParams(methName, params);
    }

    public UMLClass classExists(String className){
        UMLClass foundClass = null;
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                foundClass = classes.get(i);
                break;
            }
        }

        if (foundClass == null){
            System.out.print("Class \"" + className + "\" does not currently exist");
        }

        return foundClass;
    }




    // Prints the relationships of a given classname 
    public void printRel(String className){
        //intialize an empty arraylist which will hold our relationships
        ArrayList<ArrayList<String>> rels = null; 

        /*
        loop will go through the class list, find the given classname and populatates our relationship
        arraylist with relationships from the given classname
        */
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                rels = classes.get(i).getRels();
                break;  
            }
        }
        System.out.println(className + " is a source to these classes: ");
        
        // Determines if our class is source prints out the relationships
        for(int i = 0; i < rels.size(); ++i){
            if(rels.get(i).get(1).equals("src")){
                System.out.println(rels.get(i).get(0));
            }
        }
        System.out.println(className + " is a destination for these classes: ");
    
        // Determines if our class is destination and prints out the relationships
        for(int i = 0; i < rels.size(); ++i){
            if(rels.get(i).get(1).equals("dest")){
                System.out.println(rels.get(i).get(0));
            }
        }
    }

    public void printClassContents(String className) {

        UMLClass printClass = classExists(className);
        if (printClass == null){
            return;
        }

        // prints out the class's name
        System.out.println("Class: " + className);

        // prints out the class's attributes
        System.out.println("Attributes: ");
        ArrayList<String> attrs = printClass.getAttrs();
        for (int i = 0; i < attrs.size(); ++i) {
            System.out.println(attrs.get(i));
        }

        // prints out the class's relationships
        printRel(className);

        return;
    }

    public void printClassList() {
        // Prints out the name of each class
        for (int i = 0; i < classes.size(); ++i) {
            System.out.println(classes.get(i).name);
        }
    }

    public void save(String fileName) {
        // Create a JSON array to hold all of the classes
        JSONArray classJArray = new JSONArray();

        // Loop through the list of classes to save each class and add its
        // resulting JSON object to the JSON class array
        for (int i = 0; i < classes.size(); ++i) {
            classJArray.add(classes.get(i).saveClass());
        }

        // Write out the JSON class array to the desired filename and put it in the "saves" directory
        // and catch IOExceptions if they occur (which will result in a stack trace)
        String filePath = new File("").getAbsolutePath();
        // Make a "saves" directory in the umleditor to hold JSON save files
        new File(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves").mkdirs();
        try (FileWriter file = new FileWriter(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + fileName + ".json")) {
            file.write(classJArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName){
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + fileName + ".json")) {
            // Save the JSON array from the parser
            Object obj = jPar.parse(reader);
            JSONArray classList = (JSONArray) obj;
            
            // Loop through each class object in the JSON array and add each class to the UMLEditor
            // This ensures that each relationship can be added for each class during a second loop
            // as addRel will fail if one or both classes do no already exist
            for (int i = 0; i < classList.size(); ++i)  {
                JSONObject singleClass = (JSONObject)classList.get(i);
                String className = (String)singleClass.get("name");
                this.addClass(className);
            }

            // Loop through each class object in the array again to add the relationships,
            // methods, and fields for each class
            for (int classNum = 0; classNum < classList.size(); ++classNum)  {
                JSONObject singleClass = (JSONObject) classList.get(classNum);

                // Save the values of the name, relationships, and attributes from the class object
                String className = (String) singleClass.get("name");
                JSONArray rels = (JSONArray) singleClass.get("relationships");
                JSONArray methods = (JSONArray) singleClass.get("methods");
                JSONArray fields = (JSONArray) singleClass.get("fields");

                // Loop through each relationship in the relationship JSON array
                // and add each relationship to the current class
                for(int relNum = 0; relNum < rels.size(); ++relNum) {
                    JSONObject relation = (JSONObject) rels.get(relNum);

                    // Save the status of the relationship relative to the current class (src or dest)
                    String relStatus = (String) relation.get("src/dest");

                    // Save the type of the relationship
                    String relType = (String) relation.get("type");

                    // Add the relationship in the correct order based on whether the
                    // current class is the source or destination
                    if (relStatus.equals("src")){
                        this.addRel(className, (String) relation.get("className"), relType);
                    } else { // status == dest
                        this.addRel((String) relation.get("className"), className, relType);
                    }
                }

                // Loop through the fields JSON array and add each field
                for(int fieldNum = 0; fieldNum < fields.size(); ++fieldNum) {
                    this.addAttr(className, (String) fields.get(fieldNum), "field");
                }

                // Loop through the methods JSON array and add each method
                for(int methodNum = 0; methodNum < methods.size(); ++methodNum) {
                    JSONArray method = (JSONArray) methods.get(methodNum);
                    
                    // Add the method to the class
                    this.addAttr(className, (String) method.get(0), "method");

                    // Add all params for the method to the class
                    for(int paramNum = 1; paramNum < method.size(); ++paramNum) {
                        this.addParam(className, (String) method.get(0), (String) method.get(paramNum));
                    }
                }
            }
        // Relevant exception catching: each results in a stack trace
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // No-op function to ensure that the UMLEditor did not encounter issues while being constructed
    public boolean noop() {
        return true;
    }

    // Getter function for testing editor functionality
    public ArrayList<UMLClass> getClasses() {
        return classes;
    }
}
