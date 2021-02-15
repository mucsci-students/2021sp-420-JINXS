package org.jinxs.umleditor;

import java.util.ArrayList;

// For writing out to a file when saving
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// For the JSON array of classes to be written to file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import jdk.internal.joptsimple.internal.Classes;
//import jdk.tools.jaotc.collect.ClassSearch;

public class UMLEditor {
    
    private ArrayList<UMLClass> classes;

    public UMLEditor() {
        classes = new ArrayList<UMLClass>();
    }

    // Adds a class to the list of classes given a new class name that is not already in use
    public void addClass(String className) {
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
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i).name.equals(oldName)){
                classes.get(i).name = newName;
            }
            else {
                System.out.println("Class \"" + oldName + "\" was not found");
            }
        }
    }
    

    // Adds a relationship between class1 and class2 where class1 is the source
    // and class2 is the destination
    public void addRel(String class1, String class2) {
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

        // Ensure a relationship between class1 and class2 does not already exist
        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).get(0).equals(class2)) {
                // Notify user of already existing relationship
                System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" already exists");
                return;
            }
        }

        // Add the relationship to both class's rel lists
        if (c1 != null && c2 != null) {
            c1.addRel(class2, true);
            c2.addRel(class1, false);
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

    // Adds an attribute (attrName) to a given class (className) if it exists,
    // and the class does not currently have that attribute
    public void addAttr(String className, String attrName) {
        // Will store whether the given "className" exists
        boolean classExists = false;
        // Will store the result of attempting to add an attribute to the class
        boolean attrAdded = false;

        // Search through the class list to find the desired class
        for (int i = 0; i < classes.size(); ++i) {
            // If the class is found, attempt to add the desired attribute
            if (classes.get(i).name.equals(className)) {
                classExists = true;

                UMLClass currClass = classes.get(i);
                // True if added succesfully, false if duplicate
                attrAdded = currClass.addAttr(attrName);
            }
        }

        // Notify the user of the resuls of the attribute addition

        if (!classExists) {
            System.out.println("Class \"" + className + "\" does not exist");
            return;
        }

        if (attrAdded) {
            System.out.println("Attribute \"" + attrName + "\" added to class \"" + className + "\" succesfully");
        } else {
            System.out.println("Attribute \"" + attrName + "\" is already an attribute of class \"" + className);
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
    public void delAttr(String className, String attributes){
        boolean attrExist = false;

    // Searches through arraylist classes, searching for className
        for (int i = 0; i < classes.size(); i++){
            if (className.equals(classes.get(i).name)){
                UMLClass currClass = classes.get(i);
                attrExist = currClass.deleteAttr(attributes);
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
    public void renameAttr(String className, String oldAttr, String newAttr) {
        // Ensure the new attribute name is different than the one being renamed
        if (oldAttr.equals(newAttr)) {
          System.out.println("The new attribute name must be different than the one being changed");
          return;
        }
        
        // Find the class that will have an attribute renamed
        for (int i = 0; i < classes.size(); ++i) {
          // If the class exists, attempt to rename the provided attribute name to the new name
          if (classes.get(i).name.equals(className)) {
            boolean success = classes.get(i).renameAttr(oldAttr, newAttr);
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

        // finds the class of the specified names
        UMLClass printClass = null;
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                printClass = classes.get(i);
                break;
            }
        }

        // returns if the class does not exsist and prints an error
        if (printClass == null) {
            System.out.println("Class does not exist");
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

        // Write out the JSON class array to the desired filename and catch
        // IOExceptions if they occur (which will result in a stack trace)
        try (FileWriter file = new FileWriter(fileName + ".json")) {
            file.write(classJArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName){
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();
        
        // Attempt to read the filename specified by the user or catch resulting exceptions
        // if/when that fails
        try (FileReader reader = new FileReader(fileName + ".json")) {
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

            // Loop through each class object in the array again to add the relationships and
            // attributes for each class
            for (int classNum = 0; classNum < classList.size(); ++classNum)  {
                JSONObject singleClass = (JSONObject) classList.get(classNum);

                // Save the values of the name, relationships, and attributes from the class object
                String className = (String) singleClass.get("name");
                JSONArray Rel = (JSONArray) singleClass.get("relationships");
                JSONArray att = (JSONArray) singleClass.get("attributes");

                // Loop through each relationship in the relationship JSON array
                // and add each relationship to the current class
                for(int relNum = 0; relNum < Rel.size(); ++relNum) {
                    JSONObject relation = (JSONObject) Rel.get(relNum);

                    // Save the status of the relationship relative to the current class (src or dest)
                    String relStatus = (String) relation.get("src/dest");

                    // Add the relationship in the correct order based on whether the
                    // current class is the source or destination
                    if (relStatus.equals("src")){
                        this.addRel(className, (String) relation.get("className"));
                    } else { // status == dest
                        this.addRel((String) relation.get("className"), className);
                    }
                }

                // Loop through the attributes JSON array and add each attribute
                for(int attrNum = 0; attrNum < att.size(); ++attrNum) {
                    this.addAttr(className, (String) att.get(attrNum));
                }
            }
        // Relevant exception catching: each results in a stack trace
        } catch (Exception e){
            e.printStackTrace();
        } /* catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        } */
    }
}
