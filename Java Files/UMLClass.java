import java.util.ArrayList;

public class UMLClass {

  // Name of this class
  public String name;
  // List of related classes
  // {
  //    {"empty", ""}, // default state
  //    {"name", "src/dest"},
  //    {"name", "src/dest"},
  //    {"name", "src/dest"}
  // }
  private ArrayList<ArrayList<String>> relationships;
  // List of attributes of this class
  private ArrayList<String> attributes;

  // Constructs this new UMLClass given a class name
  public UMLClass(String className) {
    const int defaultSize = 100;
    name = className;
    relationships = new ArrayList<ArrayList<String>(2)>(defaultSize);
    attributes = new ArrayList<String>(defaultSize);
  }

  // Returns the list of relationships
  public ArrayList<ArrayList<String>> getRels() {
    return relationships;
  }

  // Add a relationship given the name of the other class and a boolean
  // Boolean should be true if the other class is the source, else should be
  //    false if this class is the source (the other class is the destination)
  public boolean addRel(String className, boolean isSrc) {
    relationships.ensureCapacity(relationships.size() + 1);
    String ext = isSrc ? "src" : "dest";
    return relationships.add({className, ext});
  }

  // Delete a relationship given the name of the other class
  public boolean deleteRel(String className) {
    for (int i = 0; i < relationships.size(); ++i) {
      if (relationships.get(i).get(0) == className) {
        return relationships.set(i, new ArrayList<>(Arrays.asList({"empty", ""})));
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
    attributes.ensureCapacity(attributes.size() + 1);
    // Add code here to ensure that the requested attribute name to add doesn't already exist (return false if so)
    return attributes.add(attrName);
  }

  // Delete an attribute given a name
  public boolean deleteAttr(String attrName) {
    return attributes.remove(attrName);
  }

  // Renames an attribute given the old name and a new name for the attribute
  public boolean renameAttr(String oldName, newName) {
    for (int i = 0; i < attributes.size(); ++i) {
      if (attributes.get(i).get(0) == oldName) {
        attributes.get(i).set(0, newName);
        return true;
      }
    }
    return false;
  }
}