package org.jinxs.umleditor;

import java.util.ArrayList;
import java.util.Iterator;
import javax.lang.model.SourceVersion;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UMLEditor {
    
    private ArrayList<UMLClass> classes;
    private Memento undoMeme;
    private Memento redoMeme;

    public UMLEditor() {
        classes = new ArrayList<UMLClass>();
        undoMeme = new Memento();
        redoMeme = new Memento();
    }

    // Adds a class to the list of classes given a new class name that is not already in use
    public boolean addClass(String className) {
        // Ensure the class name does not start with a number
        if (!validName(className)) {
            System.out.println("The class name \"" + className + "\" is not valid");
            return false;
        }

        classes.ensureCapacity(classes.size() + 1);
        // Loop through the list of classes to ensure that a duplicate class name is not trying to be inserted
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                System.out.println("The requested class name already exists");
                return false;
            }
        }
        // Add the new class to the list of classes
        return classes.add(new UMLClass(className));
    }

    // Deletes a class from the list of classes given a class name that exists
    public void deleteClass(String className) {
        // Loop through the list of classes to ensure that the requested class to delete exists
        for (int i = 0; i < classes.size(); ++i) {
            if (classes.get(i).name.equals(className)) {
                // Save the requested class to delete in order to delete relationships first
                UMLClass deletedClass = classes.get(i);
                // Save the list of relationships
                ArrayList<UMLRel> deletedRels = deletedClass.getRels();
                // Loop through the list of relationships and delete each one from the related class
                for (int j = 0; j < deletedRels.size(); ++j) {
                    delRel(deletedClass.name, deletedRels.get(j).partner);
                }
                // Finally remove the requested class to delete from the class list
                classes.remove(i);
                return;
            }
        }
        System.out.println("The requested class to delete does not exist");
    }

    // Rename's a class with the oldName to the newName
    // Returns true if successful, false if not
    public boolean renameClass(String oldName, String newName) {
        // Ensure the class name does not contain special characters (except for '_')
        // By matching with a regex
        if (!validName(newName)) {
            System.out.println("The class name \"" + newName + "\" is not valid");
            return false;
        }

        // Check if the new name is already a class that exists
        UMLClass classWithNewName = null;
        Iterator<UMLClass> iter = classes.iterator();
        while (iter.hasNext()) {
            classWithNewName = iter.next();
            if (classWithNewName.name.equals(newName)) {
                System.out.println("Class with the name \"" + newName + "\" already exists");
                return false;
            }
        }

        // Find the class that is being renamed and make sure it exists
        UMLClass classToRename = classExists(oldName);

        if (classToRename == null) {
            return false;
        } else {
            classToRename.name = newName;
            return true;
        }
    }

    // Duplicates the requested class and gives it a new name
    public boolean copyClass(String oldClass, String newClass) {
        UMLClass copyBase = classExists(oldClass);
        if (copyBase == null) {
            System.out.println("The class \"" + oldClass + "\" cannot be copied since it doesn't exist");
            return false;
        }

        // Check if the new name is already a class that exists
        UMLClass classWithNewName = null;
        Iterator<UMLClass> iter = classes.iterator();
        while (iter.hasNext()) {
            classWithNewName = iter.next();
            if (classWithNewName.name.equals(newClass)) {
                System.out.println("Class with the name \"" + newClass + "\" already exists");
                return false;
            }
        }

        classes.add(new UMLClass(copyBase, newClass));
        for (UMLRel rel : copyBase.getRels()) {
            if (rel.sOd.equals("src")) {
                addRel(rel.partner, newClass, rel.type);
            } else {
                addRel(newClass, rel.partner, rel.type);
            }
            
        }
        return true;
    }

    // Adds a relationship between class1 and class2 where class1 is the source
    // and class2 is the destination
    public void addRel(String class1, String class2, String type) {
        // Make sure each given class name is unique
        if (class1.equals(class2)) {
            System.out.println("Class names must be different");
            return;
        }

        if (type.equals("inheritance") || type.equals("aggregation") || type.equals("composition") ||
            type.equals("realization")){

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
        ArrayList<UMLRel> c1Rels = c1.getRels();

        // Ensure a relationship between class1 and class2 does not already exist
        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).partner.equals(class2) && c1Rels.get(i).sOd.equals("dest")) {
                // Notify user of already existing relationship
                System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" already exists");
                return;
            }
        }

        // Add the relationship to both class's rel lists
        // true = src, false = dest
        if (c1 != null && c2 != null) {
            c1.addRel(class2, false, type);
            c2.addRel(class1, true, type);
        }
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
        ArrayList<UMLRel> c1Rels = c1.getRels();

        boolean found = false;

        for (int i = 0; i < c1Rels.size(); ++i) {
            if (c1Rels.get(i).partner.equals(class2)){
                found = true;
            }
        }

        if (found == false){
            System.out.println("Relationship between \"" + class1 + "\" and \"" + class2 + "\" does not exsist");
            return;
        }

        else {
            c1.deleteRel(c2.name, "dest");
            c2.deleteRel(c1.name, "src");
        }
    }

    /********************************************************************************
    * changeRelType
    * Changes the relationship type of the given relationship
    ********************************************************************************/
    public void changeRelType(String class1, String class2, String newType) {
        UMLClass c1 = classExists(class1);
        UMLClass c2 = classExists(class2);

        if (c1 == null || c2 == null) {
            return;
        }

        /******************* Place holder type check *******************/
        if (newType.equals("inheritance") || newType.equals("aggregation") || newType.equals("composition")
                || newType.equals("realization")) {

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
    public boolean addAttr(String className, String attrName, String attrType, String type) {
        if (!attrType.equals("field") && !attrType.equals("method")) {
            return false;
        }
        
        boolean attrAdded = false;

        UMLClass currClass = classExists(className);

        if (currClass == null) {
            return false;
        }

        if (!validName(attrName)) {
            System.out.println("The " + attrType + " name \"" + attrName + "\" is not valid");
            return false;
        }
        if (!validName(type)) {
            System.out.println("The " + attrType + " type \"" + type + "\" is not valid");
            return false;
        }

        // True if added succesfully, false if duplicate
        if (attrType.equals("field")){
            attrAdded = currClass.addField(attrName, type);
        }
        else if (attrType.equals("method")){
            attrAdded = currClass.addMethod(attrName, type);
        }

        // Notify the user of the resuls of the attribute addition
        if (!attrAdded) {
            if (attrType.equals("field")){
                System.out.println("Field \"" + attrName + "\" is already a field of class \"" + className);
            }else{
                System.out.println("Method \"" + attrName + "\" is already a method of class \"" + className);
            }
            return false;
        }
        return true;
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
        }
    }
    
    // Renames an attribute (oldAttr) of the given className to newAttr
    public boolean renameAttr(String className, String oldAttr, String newAttr, String type) {
        // Ensure the new attribute name is different than the one being renamed
        if (oldAttr.equals(newAttr)) {
            System.out.println("The new attribute name must be different than the one being changed");
            return false;
        }

        if (!validName(newAttr)) {
            System.out.println("The " + type + " name \"" + newAttr + "\" is not valid");
            return false;
        }
        
        UMLClass classToRename = classExists(className);

        if (classToRename == null) {
            return false;
        }

        return classToRename.renameAttr(oldAttr, newAttr, type);
    }

    public boolean changeFieldType(String className, String fieldName, String newType) {
        UMLClass foundClass = classExists(className);
        if (foundClass == null) {
            return false;
        }

        if (!validName(newType)) {
            System.out.println("The type \"" + newType + "\" is not valid");
            return false;
        }

        return foundClass.changeFieldType(fieldName, newType);
    }

    public boolean changeMethodType(String className, String methodName, String newType) {
        UMLClass foundClass = classExists(className);
        if (foundClass == null) {
            return false;
        }

        if (!validName(newType)) {
            System.out.println("The type \"" + newType + "\" is not valid");
            return false;
        }

        return foundClass.changeMethodType(methodName, newType);
    }

    public boolean addParam(String className, String methName, String paramName, String paramType){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return false;
        }

        if (!validName(paramType)) {
            System.out.println("The type \"" + paramType + "\" is not valid");
            return false;
        }

        return foundClass.addParam(methName, paramName, paramType);
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

    public boolean changeParamName(String className, String methName, String paramName, String newParamName){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return false;
        }

        if (!validName(newParamName)) {
            System.out.println("The param name \"" + newParamName + "\" is not valid");
            return false;
        }

        return foundClass.changeParamName(methName, paramName, newParamName);
    }

    public boolean changeParamType(String className, String methName, String paramName, String newType) {
        UMLClass foundClass = classExists(className);
        if (foundClass == null) {
            return false;
        }

        if (!validName(newType)) {
            System.out.println("The type \"" + newType + "\" is not valid");
            return false;
        }

        return foundClass.changeParamType(methName, paramName, newType);
    }

    public boolean changeAllParams(String className, String methName, ArrayList<String> params, ArrayList<String> paramTypes){
        UMLClass foundClass = classExists(className);
        if (foundClass == null){
            return false;
        }

        for (String name : params) {
            if (!validName(name)) {
                System.out.println("The param name \"" + name + "\" is not valid");
                return false;
            }
        }
        for (String type : paramTypes) {
            if (!validName(type)) {
                System.out.println("The param name \"" + type + "\" is not valid");
                return false;
            }
        }

        return foundClass.changeAllParams(methName, params, paramTypes);
    }

    public UMLClass classExists(String className){
        if (!validName(className)) {
            System.out.println("The class name \"" + className + "\" is not valid");
            return null;
        }
        UMLClass foundClass = null;
        Iterator<UMLClass> iter = classes.iterator();
        while (iter.hasNext()) {
            foundClass = iter.next();
            if (foundClass.name.equals(className)) {
                return foundClass;
            }
        }

        System.out.println("Class \"" + className + "\" does not currently exist");
        return null;
    }

    public boolean validName(String name) {
        return SourceVersion.isIdentifier(name);
    }



    // Prints the relationships of a given classname 
    public void printRel(String className){
        //intialize an empty arraylist which will hold our relationships
        ArrayList<UMLRel> rels = null; 

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
            if(rels.get(i).sOd.equals("dest")){
                System.out.println("\t" + rels.get(i).partner + " - Type: " + rels.get(i).type);
            }
        }
        System.out.println(className + " is a destination for these classes: ");
    
        // Determines if our class is destination and prints out the relationships
        for(int i = 0; i < rels.size(); ++i){
            if(rels.get(i).sOd.equals("src")){
                System.out.println("\t" + rels.get(i).partner + " - Type: " + rels.get(i).type);
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

        // prints out the class's fields
        System.out.println("Fields: ");
        ArrayList<UMLField> fields = printClass.getFields();
        for (int i = 0; i < fields.size(); ++i) {
            System.out.println("    " + fields.get(i).type + " " + fields.get(i).name);
        }

        // prints out the class's methods
        System.out.println("Methods: ");
        ArrayList<UMLMethod> meths = printClass.getMethods();
        for (int i = 0; i < meths.size(); ++i) {
            UMLMethod targetMeth = meths.get(i);
            System.out.println("    " + targetMeth.type + " " + targetMeth.name);
            System.out.println("        Params: " );
            for (int x = 0; x < targetMeth.params.size(); ++x){
                System.out.println("            " + targetMeth.params.get(x).type + " " + targetMeth.params.get(x).name);
            }
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

    public void save(String fileName, String filePath) {
        // Create a JSON array to hold all of the classes
        JSONArray classJArray = new JSONArray();

        // Loop through the list of classes to save each class and add its
        // resulting JSON object to the JSON class array
        for (int i = 0; i < classes.size(); ++i) {
            classJArray.add(classes.get(i).saveClass());
        }

        // Write out the JSON class array to the desired filename and put it in the "saves" directory
        // and catch IOExceptions if they occur (which will result in a stack trace)
        if (filePath != null) {
            filePath += fileName;
        } else {
            filePath = fileName;
        }
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(classJArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // boolean meme should be true for undo and false for redo
    public void saveToMeme(boolean meme) {
        // Create a JSON array to hold all of the classes
        JSONArray classJArray = new JSONArray();

        // Loop through the list of classes to save each class and add its
        // resulting JSON object to the JSON class array
        for (int i = 0; i < classes.size(); ++i) {
            classJArray.add(classes.get(i).saveClass());
        }

        if (meme) {
            undoMeme.saveState(classJArray.toJSONString());
            redoMeme.clear();
        }
        else {
            if (undoMeme.numStates() != 0) {
                redoMeme.saveState(classJArray.toJSONString());
            }
        }
    }

    public void load(String fileName, String filePath){
        classes.clear();
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();

        if (filePath != null) {
            filePath += fileName;
        } else {
            filePath = fileName;
        }
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        try (FileReader reader = new FileReader(filePath)) {
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
                    if (relStatus.equals("dest")){
                        this.addRel(className, (String) relation.get("className"), relType);
                    }
                }

                // Loop through the fields JSON array and add each field
                for(int fieldNum = 0; fieldNum < fields.size(); ++fieldNum) {
                    this.addAttr(className, (String) ((JSONObject) fields.get(fieldNum)).get("name"), "field", 
                            (String) ((JSONObject) fields.get(fieldNum)).get("type"));
                }

                // Loop through the methods JSON array and add each method
                for(int methodNum = 0; methodNum < methods.size(); ++methodNum) {
                    JSONObject method = (JSONObject) methods.get(methodNum);
                    
                    // Add the method to the class
                    this.addAttr(className, (String) method.get("name"), "method", (String) method.get("type"));

                    // Add all params for the method to the class
                    JSONArray params = (JSONArray) method.get("params");
                    for(int paramNum = 0; paramNum < params.size(); ++paramNum) {
                        this.addParam(className, (String) method.get("name"), (String) ((JSONObject) params.get(paramNum)).get("name"),
                                (String) ((JSONObject) params.get(paramNum)).get("type"));
                    }
                }
            }
        // Relevant exception catching: each results in a stack trace
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // boolean meme should be true for undo and false for redo
    public void loadFromMeme(boolean meme) {
        JSONParser jPar = new JSONParser();
        try {
            String state = meme ? undoMeme.loadState() : redoMeme.loadState();
            if (state == null) {
                return;
            }
            classes.clear();
            Object obj = jPar.parse(state);
            JSONArray classList = (JSONArray) obj;

            // Loop through each class object in the JSON array and add each class to the
            // UMLEditor
            // This ensures that each relationship can be added for each class during a
            // second loop
            // as addRel will fail if one or both classes do no already exist
            for (int i = 0; i < classList.size(); ++i) {
                JSONObject singleClass = (JSONObject) classList.get(i);
                String className = (String) singleClass.get("name");
                this.addClass(className);
            }

            // Loop through each class object in the array again to add the relationships,
            // methods, and fields for each class
            for (int classNum = 0; classNum < classList.size(); ++classNum) {
                JSONObject singleClass = (JSONObject) classList.get(classNum);

                // Save the values of the name, relationships, and attributes from the class
                // object
                String className = (String) singleClass.get("name");
                JSONArray rels = (JSONArray) singleClass.get("relationships");
                JSONArray methods = (JSONArray) singleClass.get("methods");
                JSONArray fields = (JSONArray) singleClass.get("fields");

                // Loop through each relationship in the relationship JSON array
                // and add each relationship to the current class
                for (int relNum = 0; relNum < rels.size(); ++relNum) {
                    JSONObject relation = (JSONObject) rels.get(relNum);

                    // Save the status of the relationship relative to the current class (src or
                    // dest)
                    String relStatus = (String) relation.get("src/dest");

                    // Save the type of the relationship
                    String relType = (String) relation.get("type");

                    // Add the relationship in the correct order based on whether the
                    // current class is the source or destination
                    if (relStatus.equals("src")) {
                        this.addRel(className, (String) relation.get("className"), relType);
                    } else { // status == dest
                        this.addRel((String) relation.get("className"), className, relType);
                    }
                }

                // Loop through the fields JSON array and add each field
                for (int fieldNum = 0; fieldNum < fields.size(); ++fieldNum) {
                    this.addAttr(className, (String) ((JSONObject) fields.get(fieldNum)).get("name"), "field",
                            (String) ((JSONObject) fields.get(fieldNum)).get("type"));
                }

                // Loop through the methods JSON array and add each method
                for (int methodNum = 0; methodNum < methods.size(); ++methodNum) {
                    JSONObject method = (JSONObject) methods.get(methodNum);

                    // Add the method to the class
                    this.addAttr(className, (String) method.get("name"), "method", (String) method.get("type"));

                    // Add all params for the method to the class
                    JSONArray params = (JSONArray) method.get("params");
                    for (int paramNum = 1; paramNum < method.size(); ++paramNum) {
                        this.addParam(className, (String) method.get("name"),
                                (String) ((JSONObject) params.get(paramNum)).get("name"),
                                (String) ((JSONObject) params.get(paramNum)).get("type"));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        saveToMeme(false);
        loadFromMeme(true);
    }

    public void redo() {
        loadFromMeme(false);
    }

    public void removeLastSave() {
        undoMeme.loadState();
    }

    // No-op function to ensure that the UMLEditor did not encounter issues while being constructed
    public boolean noop() {
        return true;
    }

    // Getter function for testing editor functionality
    public ArrayList<UMLClass> getClasses() {
        return classes;
    }

    public void clear() {
        classes.clear();
    }
}
