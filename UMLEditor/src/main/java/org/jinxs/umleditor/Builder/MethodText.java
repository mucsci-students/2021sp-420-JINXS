package org.jinxs.umleditor.Builder;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import java.awt.Color;

import org.jinxs.umleditor.UMLClass;
import org.jinxs.umleditor.UMLMethod;
import org.jinxs.umleditor.UMLParam;

public class MethodText extends JTextArea {

    public MethodText(UMLClass umlClass) {
        // Get and store the methods from the project
        ArrayList<UMLMethod> methods = umlClass.getMethods();

        // If methods exist in the project for this class, add them to a textarea with each on 
        // their own line 
        // The size of the panel will also adjust its height and width to hold each new method
        if (methods.size() > 0) {
            String methodString = "";

            for(int j = 0; j < methods.size(); j++) {
                String methodName = methods.get(j).name;
                String methodType = methods.get(j).type;

                // Fencepost problem: only add a newline before every method
                // after the first one
                if (j != 0) {
                    methodString += "\n";
                    // Update the height of this textArea for each additional method
                    // because it does not update itself
                    this.setSize(this.getWidth(), this.getHeight()+20);
                }

                // Put the fields for the current method in parentheses like an actual method
                methodString += methodType + " " + methodName + "(";

                ArrayList<UMLParam> params = methods.get(j).params;

                for(int k = 0; k < params.size(); ++k){
                    String param = params.get(k).name;
                    String type = params.get(k).type;
                    // Fencepost problem: only add a comma if another param exists
                    // beyond the first
                    if (k != 0) {
                        methodString += ", ";
                    }
                    methodString += type + " " + param;
                }
                // Complete the param part of the string with a paren and semicolon like a real method
                methodString += ");";
            }
            // Build a textarea from the methods string that was constructed
            this.setText(methodString);
            this.setEditable(false);

            // Give the methods/params an orange border
            Border bdMethod = BorderFactory.createLineBorder(Color.ORANGE);
            this.setBorder(bdMethod);
        } else {
            // There are no methods in this class, so set the text to nothing
            this.setText("");
        }
    }
}