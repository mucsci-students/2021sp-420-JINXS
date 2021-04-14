package org.jinxs.umleditor;

public class UMLRel {

    public String partner;
    public String sOd;
    public String type;

    public UMLRel(String relatedClass, String srcOrDest, String relType) {
        partner = relatedClass;
        sOd = srcOrDest;
        type = relType;
    }
}
