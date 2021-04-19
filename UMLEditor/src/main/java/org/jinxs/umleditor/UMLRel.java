package org.jinxs.umleditor;

public class UMLRel {

    public String partner;
    // Describes the status of the partner in relation to the class that holds this relationship
    public String sOd;
    public String type;

    public UMLRel(String relatedClass, String srcOrDest, String relType) {
        partner = relatedClass;
        sOd = srcOrDest;
        type = relType;
    }
}
