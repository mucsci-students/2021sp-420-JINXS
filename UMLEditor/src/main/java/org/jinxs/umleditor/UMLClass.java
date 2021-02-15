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
    // List of attributes of this class
    private ArrayList<String> attributes;

    // Constructs this new UMLClass given a class name
    public UMLClass(String className) {
        final int defaultSize = 100;
        name = className;
        relationships = new ArrayList<ArrayList<String>>(defaultSize);
        attributes = new ArrayList<String>(defaultSize);
    }

    // Returns the list of relationships
    public ArrayList<ArrayList<String>> getRels() {
        return relationships;
    }

    // Add a relationship given the name of the other class and a boolean
    // Boolean should be true if the other class is the source, else should be
    // false if this class is the source (the other class is the destination)
    public boolean addRel(String className, boolean isSrc) {
        // Ensure there is space for the new relationship
        relationships.ensureCapacity(relationships.size() + 1);

        // Convert isSrc to a string
        String ext = isSrc ? "src" : "dest";

        // Create a new relationship ArrayList to hold the class name and src/dest
        // status
        ArrayList<String> rel = new ArrayList<String>(2);
        rel.add(className);
        rel.add(ext);

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

    // Return the list of attributes
    public ArrayList<String> getAttrs() {
        return attributes;
    }

    // Add an attribute given a name
    public boolean addAttr(String attrName) {
        // Look through the attribute list for the name to make sure it doesn't exist
        for (int i = 0; i < attributes.size(); ++i) {
            // If the attrName is found in the list, then the attribute already exists
            if (attributes.get(i).equals(attrName)) {
                return false;
            }
        }

        // Ensure there is space in the ArrayList for the attribute
        attributes.ensureCapacity(attributes.size() + 1);

        return attributes.add(attrName);
    }

    // Delete an attribute given a name
    public boolean deleteAttr(String attrName) {
        return attributes.remove(attrName);
    }

    // Renames an attribute given the old name and a new name for the attribute
    public boolean renameAttr(String oldName, String newName) {
        // Make sure the new name is not already an attribute for this class
        for (int i = 0; i < attributes.size(); ++i) {
          if (attributes.get(i).equals(newName)) {
            System.out.println("Attribute \"" + newName + "\" is already an attribute of class\"" + name + "\"");
            return false;
          }
        }
    
        // Look through the attributes ArrayList for the old attribute
        for (int i = 0; i < attributes.size(); ++i) {
          if (attributes.get(i).equals(oldName)) {
            attributes.set(i, newName);
            return true;
          }
        }
    
        // If control reaches this point, the old attribute does not exist for this class
        System.out.println("Attribute \"" + oldName + "\" is not an attribute of class\"" + name + "\"");
        return false;
    }

    // Saves the contents of the class into a JSONObject
    // The name of the class is a single pair while the relationships
    // and attributes are saved as arrays. Due to the structure of the relationships,
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

        // Add all attributes for the class into a JSON array
        JSONArray attrsJArray = new JSONArray();
        for (int i = 0; i < attributes.size(); ++i) {
            attrsJArray.add(attributes.get(i));
        }
        
        // Add the array of attributes to the class object
        classJObject.put("attributes", attrsJArray);

        // Everything is added, so the class object is finished
        return classJObject;
    }
}
