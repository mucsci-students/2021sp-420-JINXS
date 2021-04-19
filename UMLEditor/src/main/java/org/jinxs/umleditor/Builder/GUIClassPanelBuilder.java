package org.jinxs.umleditor.Builder;

// Implements the builder interface to construct GUIClassPanels from the given textAreas
public class GUIClassPanelBuilder implements Builder {
    private ClassText classTextArea;
    private FieldText fieldTextArea;
    private MethodText methodTextArea;

    public GUIClassPanelBuilder(ClassText classTextArea, FieldText fieldTextArea, MethodText methodTextArea) {
        this.classTextArea = classTextArea;
        this.fieldTextArea = fieldTextArea;
        this.methodTextArea = methodTextArea;
    }

    public void setClassTextArea(ClassText classText) {
        this.classTextArea = classText;
    }

    public void setFieldTextArea(FieldText fieldText) {
        this.fieldTextArea = fieldText;
    }

    public void setMethodTextArea(MethodText methodText) {
        this.methodTextArea = methodText;
    }

    // Returns the built panel
    public GUIClassPanel getResult() {
        return new GUIClassPanel(classTextArea, fieldTextArea, methodTextArea);
    }
}
