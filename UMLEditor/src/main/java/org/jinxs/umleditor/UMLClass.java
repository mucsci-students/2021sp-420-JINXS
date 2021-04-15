package org.jinxs.umleditor;

import java.util.ArrayList;

// For building a JSON object for the class
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class UMLClass {
    // Name of this class
    public String name;
    // List of related classes
    // "src" or "dest" defines whether className is the source or destination to
    // this class, type defines the type of relationship between the classes
    // {
    // {"className1", "src/dest", "type"},
    // {"className2", "src/dest", "type"},
    // {"className3", "src/dest", "type"}
    // }
    private ArrayList<UMLRel> relationships;
    // List of fields of this class
    private ArrayList<UMLField> fields;
    // List of methods of this class
    // The first item of each method's arraylist contains its name, any succeding
    // strings in the method's arraylist are the names of its parameters
    // {
    // {"methodName1", "param1", "param2", ...},
    // {"methodName2", "param1", "param2", ...},
    // {"methodName3", "param1", "param2", ...}
    // }
    private ArrayList<UMLMethod> methods;

    // Constructs this new UMLClass given a class name
    public UMLClass(String className) {
        final int defaultSize = 100;
        name = className;
        relationships = new ArrayList<UMLRel>(defaultSize);
        fields = new ArrayList<UMLField>(defaultSize);
        methods = new ArrayList<UMLMethod>(defaultSize);
    }

    // Returns the list of relationships
    public ArrayList<UMLRel> getRels() {
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
        UMLRel rel = new UMLRel(className, ext, type);

        // Add the relationship to the list
        return relationships.add(rel);
    }

    // Delete a relationship given the name of the other class
    public boolean deleteRel(String className, String srcDest) {
        for (int i = 0; i < relationships.size(); ++i) {
            if (relationships.get(i).partner.equals(className) && relationships.get(i).sOd.equals(srcDest)) {
                relationships.remove(i);
                return true;
            }
        }
        return false;
    }

    // Change a relationship type
    public boolean changeRelType(String otherClass, String newType) {
        for (int i = 0; i < relationships.size(); ++i) {
            if (relationships.get(i).partner.equals(otherClass)) {
                relationships.get(i).type = newType;
                return true;
            }
        }
        return false;
    }

    // Return the list of fields
    public ArrayList<UMLField> getFields() {
        return fields;
    }

    // Return the list of methods
    public ArrayList<UMLMethod> getMethods() {
        return methods;
    }

    // Add an attribute given a name
    public boolean addField(String fieldName, String type) {
        // Look through the attribute list for the name to make sure it doesn't exist
        for (int i = 0; i < fields.size(); ++i) {
            // If the attrName is found in the list, then the attribute already exists
            if (fields.get(i).name.equals(fieldName)) {
                return false;
            }
        }

        // Ensure there is space in the ArrayList for the attribute
        fields.ensureCapacity(fields.size() + 1);
        
        return fields.add(new UMLField(fieldName, type));
    }

    // Returns false if the requested method name already exists, or if the method name or
    //  method type were empty
    public boolean addMethod(String methodName, String methodType){
        if (methodName.isEmpty() || methodType.isEmpty()) {
            return false;
        }
        // Look through the method list for the name to make sure it doesn't exist
        for (int i = 0; i < methods.size(); ++i) {
            // If the attrName is found in the list, then the attribute already exists
            if (methods.get(i).name.equals(methodName)) {
                return false;
            }
        }

        // Ensure there is space for the new method
        methods.ensureCapacity(methods.size() + 1);

        return methods.add(new UMLMethod(methodName, methodType));
    }

    // Delete an attribute given a name
    public boolean deleteAttr(String attrName, String type) {
        if (type.equals("field")){
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).name.equals(attrName)) {
                    fields.remove(i);
                    return true;
                }
            }
        }
        else if (type.equals("method")) {
            for (int i = 0; i < methods.size(); i++) {
                if (methods.get(i).name.equals(attrName)) {
                    methods.remove(i);
                    return true;
                }
            }
        }
        return false;
        
    }

    // Renames an attribute given the old name and a new name for the attribute
    public boolean renameAttr(String oldName, String newName, String type) {
        // Make sure the new name is not already an attribute for this class
        if (type.equals("field")){
            for (int i = 0; i < fields.size(); ++i) {
                if (fields.get(i).name.equals(newName)) {
                    System.out.println("field \"" + newName + "\" is already an field of class \"" + name + "\"");
                    return false;
                }
            }

            for (int i = 0; i < fields.size(); ++i) {
                if (fields.get(i).name.equals(oldName)) {
                    fields.get(i).name = newName;
                    return true;
                }
            }

        }else if (type.equals("method")){
            for (int i = 0; i < methods.size(); ++i) {
                if (methods.get(i).name.equals(newName)) {
                    System.out.println("Method \"" + newName + "\" is already an method of class \"" + name + "\"");
                    return false;
                }
            }

            for (int i = 0; i < methods.size(); ++i) {
                if (methods.get(i).name.equals(oldName)) {
                    methods.get(i).name = newName;
                    return true;
                }
            }
        }

    
        // If control reaches this point, the old attribute does not exist for this class
        System.out.println("Attribute \"" + oldName + "\" is not an attribute of class \"" + name + "\"");
        return false;
    }

    public boolean changeFieldType(String fName, String newType) {
        for (int i = 0; i < fields.size(); ++i) {
            if (fields.get(i).name.equals(fName)) {
                fields.get(i).type = newType;
                return true;
            }
        }

        System.out.println("Field does not exist");
        return false;
    }

    public boolean changeMethodType(String methName, String newType) {
        for (int i = 0; i < methods.size(); ++i) {
            if (methods.get(i).name.equals(methName)) {
                methods.get(i).type = newType;
                return true;
            }
        }

        System.out.println("Method does not exist");
        return false;
    }

    public boolean addParam(String methName, String paramName, String paramType){

        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).name.equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 0; i < targetMethod.params.size(); ++i){
            if (targetMethod.params.get(i).name.equals(paramName)){
                System.out.println("Parameter already exists");
                return false;
            }
        }

        if (!targetMethod.addParam(paramName, paramType)) {
            System.out.println("The parameter name and parameter type cannot be empty");
            return false;
        }
        return true;
    }

    public boolean deleteParam(String methName, String paramName){
        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).name.equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 0; i < targetMethod.params.size(); ++i){
            if (targetMethod.params.get(i).name.equals(paramName)){
                targetMethod.params.remove(i);
                return true;
            }
        }

        System.out.println("Parameter does not exist");
        return false;
    }

    public boolean deleteAllParams(String methName){
        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).name.equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        targetMethod.deleteAllParams();
        return true;
    }

    public boolean changeParamName(String methName, String oldName, String newName){
        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).name.equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 0; i < targetMethod.params.size(); ++i){
            if (targetMethod.params.get(i).name.equals(oldName)){
                targetMethod.params.get(i).name = newName;
                return true;
            }
        }

        System.out.println("Parameter does not exist");
        return false;
    }

    public boolean changeParamType(String methName, String pName, String newType) {
        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i) {
            if (methods.get(i).name.equals(methName)) {
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null) {
            System.out.println("Method does not exist");
            return false;
        }

        for (int i = 0; i < targetMethod.params.size(); ++i) {
            if (targetMethod.params.get(i).name.equals(pName)) {
                targetMethod.params.get(i).type = newType;
                return true;
            }
        }

        System.out.println("Parameter does not exist");
        return false;
    }

    public boolean changeAllParams(String methName, ArrayList<String> pNames, ArrayList<String> pTypes){
        UMLMethod targetMethod = null;
        for (int i = 0; i < methods.size(); ++i){
            if (methods.get(i).name.equals(methName)){
                targetMethod = methods.get(i);
            }
        }

        if (targetMethod == null){
            System.out.println("Method does not exist");
            return false;
        }

        if (pNames.size() != pTypes.size()) {
            System.out.println("There should be the same number of parameter names as types");
            return false;
        }

        targetMethod.deleteAllParams();
        ArrayList<UMLParam> params = new ArrayList<UMLParam>();
        for (int i = 0; i < pNames.size(); i++) {
            params.add(new UMLParam(pNames.get(i), pTypes.get(i)));
        }
        return targetMethod.params.addAll(params);
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
            // 3: the type of the relationship: "aggregation", "association", "composition", "generalization"
            JSONObject relJObject = new JSONObject();
            relJObject.put("className", relationships.get(i).partner);
            relJObject.put("src/dest", relationships.get(i).sOd);
            relJObject.put("type", relationships.get(i).type);
            
            // Add each relationship object to the relationship array
            relsJArray.add(relJObject);
        }

        // Add the rel array to the class object
        classJObject.put("relationships", relsJArray);

        // Add all fields for the class into a JSON array
        JSONArray fieldsJArray = new JSONArray();
        for (int i = 0; i < fields.size(); ++i) {
            JSONObject fieldObj = new JSONObject();
            fieldObj.put("name", fields.get(i).name);
            fieldObj.put("type", fields.get(i).type);
            fieldsJArray.add(fieldObj);
        }
        
        // Add the array of fields to the class object
        classJObject.put("fields", fieldsJArray);

        // Add all methods for the class into a JSON array
        JSONArray methodsJArray = new JSONArray();
        for (int method = 0; method < methods.size(); ++method) {
            // Add each method's name, then loop through and add all params associated
            // with the method
            JSONObject methodJObj = new JSONObject();
            methodJObj.put("name", methods.get(method).name);
            methodJObj.put("type", methods.get(method).type);
            JSONArray paramsJArray = new JSONArray();
            for (int param = 0; param < methods.get(method).params.size(); ++param) {
                JSONObject paramJObj = new JSONObject();
                paramJObj.put("name", methods.get(method).params.get(param).name);
                paramJObj.put("type", methods.get(method).params.get(param).type);
                paramsJArray.add(paramJObj);
            }
            methodJObj.put("params", paramsJArray);

            
            // Add each relationship object to the relationship array
            methodsJArray.add(methodJObj);
        }

        // Add the methods array to the class object
        classJObject.put("methods", methodsJArray);

        // Everything is added, so the class object is finished
        return classJObject;
    }
}
