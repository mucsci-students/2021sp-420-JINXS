package org.jinxs.umleditor;

import java.util.ArrayList;

// For building a JSON object for the class
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UMLClass {
    // Name of this class
    public String name;
    // List of related classes
    // {
    // "src" or "dest" defines whether className is the source or destination to
    // this class
    // {"className1", "src/dest"},
    // {"className2", "src/dest"},
    // {"className3", "src/dest"}
    // }
    private ArrayList<ArrayList<String>> relationships;
    // List of fields of this class
    private ArrayList<String> fields;
    private ArrayList<ArrayList<String>> methods;

    // Constructs this new UMLClass given a class name
    public UMLClass(String className) {
        final int defaultSize = 100;
        name = className;
        relationships = new ArrayList<ArrayList<String>>(defaultSize);
        fields = new ArrayList<String>(defaultSize);
        methods = new ArrayList<ArrayList<String>>(defaultSize);
    }

    // Returns the list of relationships
    public ArrayList<ArrayList<String>> getRels() {
        return relationships;
    }

    // Add a relationship given the name of the other class and a boolean
    // Boolean should be true if the other class is the source, else should be
    // false if this class is the source (the other class is the destination)
    public boolean addRel(String className, boolean isSrc, String type) {
        // Ensure there is space for the new relationship
        relationships.ensureCapacity(relationships.size() + 1);

        // Convert isSrc to a string
        String ext = isSrc ? "src" : "dest";

        // Create a new relationship ArrayList to hold the class name and src/dest
        // status
        ArrayList<String> rel = new ArrayList<String>(2);
        rel.add(className);
        rel.add(ext);
        rel.add(type);

        // Add the relationship to the list
        return relationships.add(rel);
    }

    // Delete a relationship given the name of the other class
    public boolean deleteRel(String className) {
        for (int i = 0; i < relationships.size(); ++i) {
            if (relationships.get(i).get(0).equals(className)) {
                relationships.remove(i);
                return true;
            }
        }
        return false;
    }

    // Return the list of fields
    public ArrayList<String> getAttrs() {
        return fields;
    }

    // Return the list of methods
    public ArrayList<ArrayList<String>> getMethods() {
        return methods;
    }

    // Add an attribute given a name
    public boolean addField(String fieldName) {
        // Look through the attribute list for the name to make sure it doesn't exist
        for (int i = 0; i < fields.size(); ++i) {
            // If the attrName is found in the list, then the attribute already exists
            if (fields.get(i).equals(fieldName)) {
                return false;
            }
        }

        // Ensure there is space in the ArrayList for the attribute
        fields.ensureCapacity(fields.size() + 1);

        return fields.add(fieldName);
    }

    public boolean addMethod(String methodName){
        
        // Ensure there is space for the new method
        methods.ensureCapacity(methods.size() + 1);


        ArrayList<String> meth = new ArrayList<String>();
        meth.add(methodName);
        

        return methods.add(meth);
    }

    // Delete an attribute given a name
    public boolean deleteAttr(String attrName, String type) {
        if (type == "field"){
            return fields.remove(attrName);
        }
        for(int i = 0; i < methods.size(); i++){
            if(methods.get(i).get(0).equals(attrName)){
                return methods.remove(methods.get(i));
            }
        }

        return false;
        
    }

    // Renames an attribute given the old name and a new name for the attribute
    public boolean renameAttr(String oldName, String newName, String type) {
        // Make sure the new name is not already an attribute for this class
        if (type == "field"){
            for (int i = 0; i < fields.size(); ++i) {
                if (fields.get(i).equals(newName)) {
                System.out.println("field \"" + newName + "\" is already an field of class \"" + name + "\"");
                return false;
                }
            }

            for (int i = 0; i < fields.size(); ++i) {
                if (fields.get(i).equals(oldName)) {
                fields.set(i, newName);
                return true;
                }
            }

        }else if (type == "method"){
            for (int i = 0; i < methods.size(); ++i) {
                if (methods.get(i).get(0).equals(newName)) {
                System.out.println("Method \"" + newName + "\" is already an method of class \"" + name + "\"");
                return false;
                }
            }

            for (int i = 0; i < methods.size(); ++i) {
                if (methods.get(i).get(0).equals(oldName)) {
                methods.get(i).set(0, newName);
                return true;
                }
            }
        }

    
        // If control reaches this point, the old attribute does not exist for this class
        System.out.println("Attribute \"" + oldName + "\" is not an attribute of class \"" + name + "\"");
        return false;
    }

    public boolean addParam(String methName, String paramName){

        ArrayList<String> targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).get(0).equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 1; i < targetMethod.size(); ++i){
            if (targetMethod.get(i).equals(paramName)){
                System.out.println("Parameter already exists");
                return false;
            }
        }

        targetMethod.add(paramName);
        return true;
        
    }

    public boolean deleteParam(String methName, String paramName){
        ArrayList<String> targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).get(0).equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 1; i < targetMethod.size(); ++i){
            if (targetMethod.get(i).equals(paramName)){
               targetMethod.remove(i);
               return true;
            }
        }

        System.out.println("Parameter does not exist");
        return false;
    }

    public boolean deleteAllParams(String methName){
        ArrayList<String> targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).get(0).equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 1; i < targetMethod.size(); ++i){
            deleteParam(methName, targetMethod.get(i));
        }

        return true;
    }

    public boolean changeParam(String methName, String paramName){
        ArrayList<String> targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).get(0).equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 1; i < targetMethod.size(); ++i){
            if (targetMethod.get(i).equals(paramName)){
               targetMethod.set(i, paramName);
               return true;
            }
        }

        System.out.println("Parameter does not exist");
        return false;

    }

    public boolean changeAllParams(String methName, ArrayList<String> params){
        ArrayList<String> targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).get(0).equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        deleteAllParams(methName);

        return targetMethod.addAll(params);
    }

    // Saves the contents of the class into a JSONObject
    // The name of the class is a single pair while the relationships
    // and fields are saved as arrays. Due to the structure of the relationships,
    // each individual relationship is saved as an object of two pairs
    public JSONObject saveClass() {
        // Create the class object and add the name of the class
        JSONObject classJObject = new JSONObject();
        classJObject.put("name", name);

        // Add all relationships for the class into a JSON array
        JSONArray relsJArray = new JSONArray();
        for (int i = 0; i < relationships.size(); ++i) {
            // Put each relationship into its own object containing two pairs:
            // 1: the name of the other class in the relationship named "className"
            // 2: the status of the relationship, either "src" or "dest" name "src/dest"
            JSONObject relJObject = new JSONObject();
            relJObject.put("className", relationships.get(i).get(0));
            relJObject.put("src/dest", relationships.get(i).get(1));
            
            // Add each relationship object to the relationship array
            relsJArray.add(relJObject);
        }

        // Add the rel array to the class object
        classJObject.put("relationships", relsJArray);

        // Add all fields for the class into a JSON array
        JSONArray attrsJArray = new JSONArray();
        for (int i = 0; i < fields.size(); ++i) {
            attrsJArray.add(fields.get(i));
        }
        
        // Add the array of fields to the class object
        classJObject.put("fields", attrsJArray);

        // Everything is added, so the class object is finished
        return classJObject;
    }
}
