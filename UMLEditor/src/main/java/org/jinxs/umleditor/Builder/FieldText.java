package org.jinxs.umleditor.Builder;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import java.awt.Color;
import org.jinxs.umleditor.UMLClass;
import org.jinxs.umleditor.UMLField;

public class FieldText extends JTextArea {
    
    public FieldText(UMLClass umlClass) {
        // Store the fields for the current class
        ArrayList<UMLField> fields = umlClass.getFields();

        // If fields exist in the project for the current class, add them to a textarea with 
        // each on their own line
        // The size of the panel will also adjust its height and width to hold each new field
        if(fields.size() > 0){
            String str = ""; 
            for(int j = 0; j < fields.size(); j++){
                String field = fields.get(j).name;
                String fType = fields.get(j).type;

                // Fence-post problem: only add a new line character for each
                // field beyond the first one
                if (j != 0){
                    str += "\n"; 
                    // Update the height of this textArea for each additional field
                    // because it does not update itself
                    this.setSize(this.getWidth(), this.getHeight()+20);
                } 
                str += fType + " " + field;
            }

            // Add all of the fields to the JTextArea
            this.setText(str);
            this.setEditable(false);

            // Give the fields a blue border
            Border bdField = BorderFactory.createLineBorder(Color.BLUE);
            this.setBorder(bdField);
        } else {
            // There are no fields in this class, so set the text to nothing
            this.setText("");
        }
    }

}
