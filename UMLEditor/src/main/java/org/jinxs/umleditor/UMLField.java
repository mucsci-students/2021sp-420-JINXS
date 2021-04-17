package org.jinxs.umleditor;

public class UMLField implements UMLAttr{
    
    public String name;
    public String type;
    
    public UMLField(String fName, String fType) {
        name = fName;
        type = fType;
    }
}
