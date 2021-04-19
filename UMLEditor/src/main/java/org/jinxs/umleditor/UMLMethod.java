package org.jinxs.umleditor;

import java.util.ArrayList;

public class UMLMethod implements UMLAttr{
    
    public String name;
    public String type;
    public ArrayList<UMLParam> params;

    public UMLMethod(String mName, String retType) {
        name = mName;
        type = retType;
        params = new ArrayList<UMLParam>(100);
    }

    public UMLMethod(String mName, String retType, ArrayList<String> pNames, ArrayList<String> pTypes) {
        if (pNames.size() != pTypes.size()) {
            return;
        }
        name = mName;
        type = retType;
        params = new ArrayList<UMLParam>(100);
        for (int i = 0; i < pNames.size(); i++) {
            params.add(new UMLParam(pNames.get(i), pTypes.get(i)));
        }
    }

    // Returns false if pName or pType is empty
    public boolean addParam(String pName, String pType) {
        if (pName.isEmpty() || pType.isEmpty()) {
            return false;
        }
        return params.add(new UMLParam(pName, pType));
    }

    // Returns false if the pNames array is not the same size as the pTypes array
    public boolean addParams(ArrayList<String> pNames, ArrayList<String> pTypes) {
        if (pNames.size() != pTypes.size()) {
            return false;
        }
        for (int i = 0; i < pNames.size(); i++) {
            params.add(new UMLParam(pNames.get(i), pTypes.get(i)));
        }
        return true;
    }

    // Returns false if one or more of the requested params to delete did not exist
    public boolean deleteParams(ArrayList<String> pNames) {
        for (int i = 0; i < params.size(); i++) {
            if (pNames.contains(params.get(i).name)) {
                pNames.remove(params.get(i).name);
                params.remove(i);
            }
        }
        return !pNames.isEmpty();
    }

    public void deleteAllParams() {
        params.clear();
    }

    // Returns false if the requested param to rename could not be found
    public boolean changeParamName(String oldName, String newName) {
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).name.equals(oldName)) {
                params.get(i).name = newName;
                return true;
            }
        }
        return false;
    }

    // Returns false if the requested param to rename could not be found
    public boolean changeParamType(String pName, String newType) {
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).name.equals(pName)) {
                params.get(i).type = newType;
                return true;
            }
        }
        return false;
    }

}
