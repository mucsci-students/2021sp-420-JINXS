package org.jinxs.umleditor.Builder;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import java.awt.Color;

import org.jinxs.umleditor.UMLClass;

public class ClassText extends JTextArea {

    public ClassText(UMLClass umlClass) {
        // The class textArea only needs the class name
        this.setText(umlClass.name);
        this.setEditable(false);

        // Give the class name a green border
		Border bdClass = BorderFactory.createLineBorder(Color.GREEN);
		this.setBorder(bdClass);
    }
    
}
