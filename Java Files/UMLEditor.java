import java.util.ArrayList;

public class UMLEditor {

  private ArrayList<UMLClass> classes;

  public static void main(String[] args) {
    classes = new ArrayList<UMLClass>();
    while (true) {
      repl();
    }
  }

  // Adds a class to the list of classes given a new class name that is not already in use
  public void addClass(String className) {
    classes.ensureCapacity(classes.size() + 1);
    // Loop through the list of classes to ensure that a duplicate class name is not trying to be inserted
    for (int i = 0; i < classes.size(); ++i) {
      if (classes.get(i).name == className)
        System.out.println("The requested class name already exists");
    }
    // Add the new class to the list of classes
    classes.add(new UMLClass(className));
    System.out.println("Class \"" + className + "\" was added successfully");
  }

  // Deletes a class from the list of classes given a class name that exists
  public void deleteClass(className) {
    // Loop through the list of classes to ensure that the requested class to delete exists
    for (int i = 0; i < classes.size(); ++i) {
      if (classes.get(i).name == className) {
        // Save the requested class to delete in order to delete relationships first
        UMLClass deletedClass = classes.get(i);
        // Save the list of relationships
        ArrayList<ArrayList<String>> deletedRels = deletedClass.getRels();
        // Loop through the list of relationships and delete each one from the related class
        for (int j = 0; j < deletedRels.size(); ++j) {
          deletedClass.delRel(deletedClass.name, deletedRels.get(j).name);
        }
        // Finally remove the requested class to delete from the class list
        classes.remove(i);
        System.out.println("Class \"" + className + "\" was deleted successfully");
      }
    }
    System.out.println("The requested class to delete does not exist");
  }

  public void renameClass();

  // Remember to use ensureCapacity before adding
  public void addRel();

  public void delRel(String class1, String class2);

  // Remember to use ensureCapacity before adding
  public void addAttr();

  public void delAttr();

  public void renameAttr();

  private repl() {
    // Interface stuff goes here
    // Check for the user's command input here
  }

}