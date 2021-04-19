package org.jinxs.umleditor.Builder;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import java.awt.Color;

public class GUIClassPanel extends JPanel {
    
    public GUIClassPanel(ClassText classTextArea, FieldText fieldTextArea, MethodText methodTextArea) {
        // Each panel uses a vertical box layout so that the class name comes first
        // followed by the fields then methods in a vertical display
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(classTextArea);

        int height = classTextArea.getPreferredSize().height;
        int width = classTextArea.getPreferredSize().width;

        // Only add the fieldText if fields exist
        if (!fieldTextArea.getText().equals("")) {
            this.add(fieldTextArea);
            height += fieldTextArea.getPreferredSize().height;
            width = Math.max(width, fieldTextArea.getPreferredSize().width);
        }
        // Only add the methodText if methods exist
        if (!methodTextArea.getText().equals("")) {
            this.add(methodTextArea);
            height += methodTextArea.getPreferredSize().height;
            width = Math.max(width, methodTextArea.getPreferredSize().width);
        }

        // Set the panel's size to the computed height and width from the textAreas
        // that it contains
        this.setSize(width, height);

        // Set the size 8 pixels greater all around to make room for the panel border
        this.setSize(this.getWidth() + 8, this.getHeight() + 8);

        // Put a black border around the entire class panel so its boundaries
        // are visible
        Border bdPanel = BorderFactory.createLineBorder(Color.BLACK, 4);
        this.setBorder(bdPanel);
    }


}
