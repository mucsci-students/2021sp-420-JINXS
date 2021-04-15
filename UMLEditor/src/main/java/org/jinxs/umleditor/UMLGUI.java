package org.jinxs.umleditor;

import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.awt.geom.*; 

// Save and load imports
// For writing out to a file when saving
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// For the JSON array of classes to be written to file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Mouse detection imports
// Xavier & Nate
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;



public class UMLGUI implements ActionListener{
 
    private static JFrame window; 
    private static JMenuBar menu; 

    private static UMLEditor project = new UMLEditor(); 

    private static JPanel panel;
    private static ArrayList<JPanel> panels = new ArrayList<JPanel>();
    private static ArrayList<ArrayList<String>> classLocations = new ArrayList<ArrayList<String>>(); 

    // Undo/Redo momento variables
    private Memento undoMeme;
    private Memento redoMeme;
     
    // handleDrag global coordinates
    int x;
    int y;

     //draw arrow globals
     final static float THICKNESS = 3;
     final static float DASH_ARRAY[] = {10};
     final static BasicStroke SOLID_STROKE = new BasicStroke(THICKNESS);
     final static BasicStroke DASHED_STROKE = new BasicStroke(THICKNESS, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, DASH_ARRAY, 0);
 
     final static int CAP_MAJOR_LENGTH = 16;
     final static int CAP_MINOR_LENGTH = 8;
 
     private static JPanel panelFrom;
     private static JPanel panelTo;
     private static Point from = new Point();
     private static Point to = new Point();
     private static boolean isHorizontal;
     private static boolean isReverse;

    // Constructs the GUI by building and adding the menus
    public UMLGUI() {
        umlWindow();
        undoMeme = new Memento();
        redoMeme = new Memento();

        // Adds an action listener to each menu option so the correct
        // action can be performed when they are clicked
        for (int i = 0; i < menu.getMenuCount(); ++i) {
            JMenu singleMenu = menu.getMenu(i);
            for (int j = 0; j < singleMenu.getItemCount(); ++j) {
                JMenuItem menuItem = singleMenu.getItem(j);
                menuItem.addActionListener(this);
            }
        }
    }

    // Creates the GUI window and adds each menu list to the menu bar
     // Creates the GUI window and adds each menu list to the menu bar
     public static void umlWindow(){
        // Gives the window a name
        window = new JFrame("Graphical UML Editor"){
            @Override
            public void paint (Graphics g){
        
                super.paint(g); // paints background
        
                Graphics2D g2d = (Graphics2D) g;
        
                // Create a panel for each relationship in the project
        
                ArrayList<UMLClass> classes = project.getClasses(); 
                for (int i = 0; i < classes.size(); ++i) {
                    ArrayList<UMLRel> rels = classes.get(i).getRels();
                    for(int j = 0; j < rels.size(); ++j){
                        String partner = rels.get(j).partner; //needs first element of rels
                        String srcDes = rels.get(j).sOd; //needs second element of rels
                        String type = rels.get(j).type;  //needs third element of rels
        
                        JPanel curr = findPanel(classes.get(i).name);
                        JPanel otherClass = findPanel(partner);

                        panelFrom = curr; 
                        panelTo = otherClass; 

                        if(srcDes.equals("src")){
                            calculateEndpoints(); 

                                g2d.setColor(Color.PINK);
                                if (type.equals("realization"))
                                {
                                g2d.setStroke(DASHED_STROKE);
                                }
                                else
                                {
                                g2d.setStroke(SOLID_STROKE);
                                }

                                boolean isDiamond = (type.equals("aggregation") || type.equals("composition"));
                                int capSize = isDiamond ? CAP_MAJOR_LENGTH * 2 : CAP_MAJOR_LENGTH;
                                boolean shouldDrawCap;

                                if (curr == otherClass)
                                {
                                    int offsetX = from.x + panelFrom.getWidth() / 2;
                                    int offsetY = to.y - panelFrom.getHeight() / 2;
                                    g2d.draw(new Line2D.Float(from.x, from.y + 50, offsetX, from.y));
                                    g2d.draw(new Line2D.Float(offsetX, from.y + 50, offsetX, offsetY));
                                    g2d.draw(new Line2D.Float(offsetX, offsetY, to.x, offsetY));
                                    g2d.draw(new Line2D.Float(to.x, offsetY, to.x, to.y));
                                    shouldDrawCap = (Math.abs(to.y - offsetY) > capSize);
                                }
                                else{
                                    if (isHorizontal)
                                    {
                                        int midwayX = (from.x + to.x) / 2;
                                        g2d.draw(new Line2D.Float(from.x, from.y  + 50, midwayX, from.y));
                                        g2d.draw(new Line2D.Float(midwayX, from.y + 50, midwayX, to.y));
                                        g2d.draw(new Line2D.Float(midwayX, to.y, to.x, to.y));
                                        shouldDrawCap = (Math.abs(to.x - midwayX) > capSize);
                                    }
                                    else
                                    {
                                        int midwayY = (from.y + to.y) / 2;
                                        g2d.draw(new Line2D.Float(from.x, from.y + 50, from.x, midwayY));
                                        g2d.draw(new Line2D.Float(from.x, midwayY, to.x, midwayY));
                                        g2d.draw(new Line2D.Float(to.x, midwayY, to.x, to.y));
                                        shouldDrawCap = (Math.abs(to.y - midwayY) > capSize);
                                    }
                                }

                                if (shouldDrawCap && !isInverse())
                                {
                                    g2d.setStroke(SOLID_STROKE);
                                    drawCap(g2d, isDiamond, type);
                                }
                        }
        
                    } 
                }
            }
        };

        // The layout for the window is set to null to allow the user to move classes to
        // any desired location on the GUI
        window.setLayout(null);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800,700);
        
        // Creates the menu bar and creates each menu option dropdown
        // for each class functionality
        menu = new JMenuBar(); 
        createFileMenu(menu); 
        createClassMenu(menu); 
        menu.add(new JMenu("Relationship"));
        menu.add(new JMenu("Field")); 
        menu.add(new JMenu("Method"));
        menu.add(new JMenu("Parameter"));

        window.setJMenuBar(menu);

        window.setVisible(true);
    }
    
    // Builds the GUI view from the state of the underlying project/model
    public void getFromProject(UMLEditor project){
        // Save the locations of each panel so they can be moved back
        // once the panels are rebuilt from the project
        saveLocations();

        // Delete all panels on the GUI to redraw the current state of the project
        // to the GUI
        for(int i = 0; i < panels.size(); i++){
            window.remove(panels.get(i));
        }
        panels.clear();

        // Create a panel for each class in the project
        ArrayList<UMLClass> classes = project.getClasses(); 
        for (int i = 0; i < classes.size(); ++i) {
            panel = new JPanel();

            // Each panel uses a vertical box layout so that the class name comes first
            // followed by the fields then methods in a vertical display
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            window.add(panel); 

            // Create the textarea that holds the name of the class
		    JTextArea classTxt = new JTextArea(classes.get(i).name);
            classTxt.setEditable(false);
           
            // Set the initial size of the panel to just fit the class name
            panel.setSize((int)classTxt.getPreferredSize().getWidth(), 20);

            // Set the name of the panel to the name of the class it holds
            // so it can be identified and its location can be saved later
            panel.setName(classTxt.getText());

            // Give the class name a green border
		    Border bdClass = BorderFactory.createLineBorder(Color.GREEN);
		    classTxt.setBorder(bdClass);

            // Add the textarea to the panel
            panel.add(classTxt);

            // Store the fields for the current class
            ArrayList<UMLField> fields = classes.get(i).getFields();

            // If fields exist in the project for the current class, add them to a textarea with 
            // each on their own line
            // The size of the panel will also adjust its height and width to hold each new field
            if(fields.size() > 0){
                String str = ""; 
                for(int j = 0; j < fields.size(); j++){
                    // Make the panel 20 pixels taller for each field
                    panel.setSize(panel.getWidth(), (panel.getHeight() + 20));

                    String field = fields.get(j).name;
                    String fType = fields.get(j).type;

                    // Fence-post problem: only add a new line character for each
                    // field beyond the first one
                    if (j != 0){
                        str += "\n"; 
                    } 
                    str += fType + " " + field;
                }
                // Add all of the fields to  JTextArea
                JTextArea fieldsText = new JTextArea(str);
                fieldsText.setEditable(false);

                // Change the width of the panel if a field is longer than the current width
                panel.setSize(Math.max((int)fieldsText.getPreferredSize().getWidth(), panel.getWidth()), panel.getHeight());

                // Add the panel to the window
                panel.add(fieldsText);

                // Give the fields a blue border
                Border bdField = BorderFactory.createLineBorder(Color.BLUE);
                fieldsText.setBorder(bdField);
            }

            // Get and store the methods from the project
            ArrayList<UMLMethod> methods = classes.get(i).getMethods();

            // If methods exist in the project for this class, add them to a textarea with each on 
            // their own line 
            // The size of the panel will also adjust its height and width to hold each new method
            if (methods.size() > 0) {
                String methodString = "";

                for(int j = 0; j < methods.size(); j++) {
                    // Make the panel 20 pixels taller for each method
                    panel.setSize(panel.getWidth(), (panel.getHeight() + 20));
                    
                    String methodName = methods.get(j).name;
                    String methodType = methods.get(j).type;

                    // Fencepost problem: only add a newline before every method
                    // after the first one
                    if (j != 0) {
                        methodString += "\n";
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
                JTextArea methodText = new JTextArea(methodString);
                methodText.setEditable(false);

                // Update the panel's width if the length of a method exceeds the current width
                panel.setSize(Math.max((int)methodText.getPreferredSize().getWidth(), panel.getWidth()), panel.getHeight());

                // Add the methods textarea to the panel
                panel.add(methodText);

                // Give the methods/params an orange border
                Border bdField = BorderFactory.createLineBorder(Color.ORANGE);
                methodText.setBorder(bdField);
            }

            panel.setSize(panel.getWidth() + 8, panel.getHeight() + 8);
            
            // Put a black border around the entire class panel so its boundaries
            // are visible
            Border bdPanel = BorderFactory.createLineBorder(Color.BLACK, 4);
            panel.setBorder(bdPanel);

            repaintPanel();

            // Add the panel to the list of panels on the window
            panels.add(panel);

            // Move the panel to its previous location before the editor panels
            // were rebuilt
            resetLocation(panel);

            // Allow the panel to be draggable
            handleDrag(panel);
        }
    }

    private static void refresh(){
        window.repaint(); 
    }

    private static void repaintPanel(){
        panel.revalidate(); 
        panel.repaint(); 
    }

    public static void createFileMenu(JMenuBar menu){
        JMenu file = new JMenu("File"); 
        JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
        JMenuItem export = new JMenuItem("Export as Image");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem redo = new JMenuItem("Redo");
        JMenuItem exit = new JMenuItem("Exit");

        JMenuItem[] fileArray = {save,load,export,undo,redo,exit}; 
        String[] labelText = {"Save","Load","Export","Undo","Redo","Exit"};
             

        for(int i = 0; i < 6; ++i)
		{
			file.add(fileArray[i]);
			fileArray[i].setToolTipText(labelText[i]);
			
		}

        menu.add(file); 
    }

    public static void createClassMenu(JMenuBar menu){
        JMenu classes = new JMenu("Class"); 
        JMenuItem addClass = new JMenuItem("Create Class");

		classes.add(addClass);
		addClass.setToolTipText("Create Class");

        menu.add(classes); 
    }

    // Updates the available dropdowns for the "class" menu based on the state of the
    // underlying project/model
    private void updateClassDropdowns() {
        // If dropdowns exist, delete them to rebuild the menu
        if (menu.getMenu(1).getItemCount() > 1) {
            menu.getMenu(1).remove(1);
            menu.getMenu(1).remove(1);
        }

        ArrayList<UMLClass> currClasses = project.getClasses();

        // If there are no classes, no dropdowns need to be added
        if (currClasses.size() == 0) {
           return;
        }

        // If classes exist, add the option to delete and rename classes
        JMenu deleteSubMenu = new JMenu("Delete Class");
        JMenu renameSubMenu = new JMenu("Rename Class");

        // For each class, add a dropdown to both the delete and rename dropdowns
        // for that class
        for (int i = 0; i < currClasses.size(); ++i) {
            deleteSubMenu.add(new JMenuItem(currClasses.get(i).name));
            renameSubMenu.add(new JMenuItem(currClasses.get(i).name));
            deleteSubMenu.getItem(i).addActionListener(this);
            renameSubMenu.getItem(i).addActionListener(this);
            deleteSubMenu.getItem(i).setActionCommand("DeleteClass" + "*" + currClasses.get(i).name);
            renameSubMenu.getItem(i).setActionCommand("RenameClass" + "*" + currClasses.get(i).name);;
        }

        // Add each sub-dropdown to the main menu option
        menu.getMenu(1).add(deleteSubMenu);
        menu.getMenu(1).add(renameSubMenu);
    }

    // Updates the available dropdowns for the "field" menu based on the state of the
    // underlying project/model
    private void updateFieldDropdowns() {
        // If dropdowns exist, delete them to rebuild the menu
        if (menu.getMenu(3).getItemCount() > 0) {
            menu.getMenu(3).remove(0);
            menu.getMenu(3).remove(0);
            menu.getMenu(3).remove(0);
            menu.getMenu(3).remove(0);
        }

        ArrayList<UMLClass> currClasses = project.getClasses();

        // If there are no classes, no dropdowns need to be added
        if (currClasses.size() == 0) {
           return;
        }

        // If classes exist, add the option to add, delete, and rename fields
        JMenu addSubMenu = new JMenu("Add Field");
        JMenu deleteSubMenu = new JMenu("Delete Field");
        JMenu retypeSubMenu = new JMenu("Retype Field");
        JMenu renameSubMenu = new JMenu("Rename Field");

        for (int i = 0; i < currClasses.size(); ++i) {
            // For each class, add a dropdown to add a field to that class
            addSubMenu.add(new JMenuItem(currClasses.get(i).name)); 
            addSubMenu.getItem(i).addActionListener(this);
            addSubMenu.getItem(i).setActionCommand("AddField" + "*" + currClasses.get(i).name);

            JMenu delClassesMenu = new JMenu(currClasses.get(i).name);

            JMenu retypeClassesMenu = new JMenu(currClasses.get(i).name);

            JMenu renameClassesMenu = new JMenu(currClasses.get(i).name);

            // For each field in the current class, add the option to rename or delete it
            // from its class name dropdown
            ArrayList<UMLField> classFields = currClasses.get(i).getFields();
            for (int j = 0; j < classFields.size(); ++j) {
                delClassesMenu.add(new JMenuItem(classFields.get(j).name));
                retypeClassesMenu.add(new JMenuItem(classFields.get(j).name));
                renameClassesMenu.add(new JMenuItem(classFields.get(j).name));

                delClassesMenu.getItem(j).addActionListener(this);
                retypeClassesMenu.getItem(j).addActionListener(this);
                renameClassesMenu.getItem(j).addActionListener(this);

                delClassesMenu.getItem(j).setActionCommand("DeleteField" + "*" + currClasses.get(i).name + "*" + classFields.get(j).name);
                retypeClassesMenu.getItem(j).setActionCommand("RetypeField" + "*" + currClasses.get(i).name + "*" + classFields.get(j).name);
                renameClassesMenu.getItem(j).setActionCommand("RenameField" + "*" + currClasses.get(i).name + "*" + classFields.get(j).name);
            }
            
            // Add the field dropdowns to each class dropdown
            deleteSubMenu.add(delClassesMenu);
            retypeSubMenu.add(retypeClassesMenu);
            renameSubMenu.add(renameClassesMenu);
        }

        // Add the submenus to the top level dropdowns
        menu.getMenu(3).add(addSubMenu);
        menu.getMenu(3).add(deleteSubMenu);
        menu.getMenu(3).add(retypeSubMenu);
        menu.getMenu(3).add(renameSubMenu);
    }

    // Updates the available dropdowns for the "method" menu based on the state of the
    // underlying project/model
    private void updateMethodDropdowns() {
        // If dropdowns exist, delete them to rebuild the menu
        if (menu.getMenu(4).getItemCount() > 0) {
            menu.getMenu(4).remove(0);
            menu.getMenu(4).remove(0);
            menu.getMenu(4).remove(0);
            menu.getMenu(4).remove(0);
        }

        ArrayList<UMLClass> currClasses = project.getClasses();

        // If there are no classes, no dropdowns need to be added
        if (currClasses.size() == 0) {
           return;
        }

        // If classes exist, add the option to add, delete, and rename methods
        JMenu addSubMenu = new JMenu("Add Method");
        JMenu deleteSubMenu = new JMenu("Delete Method");
        JMenu retypeSubMenu = new JMenu("Retype Method");
        JMenu renameSubMenu = new JMenu("Rename Method");

        for (int i = 0; i < currClasses.size(); ++i) {
            // For each class, add a dropdown to add a method to that class
            addSubMenu.add(new JMenuItem(currClasses.get(i).name)); 
            addSubMenu.getItem(i).addActionListener(this);
            addSubMenu.getItem(i).setActionCommand("AddMethod" + "*" + currClasses.get(i).name);

            JMenu delClassesMenu = new JMenu(currClasses.get(i).name);

            JMenu retypeClassesMenu = new JMenu(currClasses.get(i).name);

            JMenu renameClassesMenu = new JMenu(currClasses.get(i).name);

            // For each method in the current class, add the option to rename or delete it
            // from its class name dropdown
            ArrayList<UMLMethod> classMethods = currClasses.get(i).getMethods();
            for (int j = 0; j < classMethods.size(); ++j) {
                delClassesMenu.add(new JMenuItem(classMethods.get(j).name));
                retypeClassesMenu.add(new JMenuItem(classMethods.get(j).name));
                renameClassesMenu.add(new JMenuItem(classMethods.get(j).name));

                delClassesMenu.getItem(j).addActionListener(this);
                retypeClassesMenu.getItem(j).addActionListener(this);
                renameClassesMenu.getItem(j).addActionListener(this);

                delClassesMenu.getItem(j).setActionCommand("DeleteMethod" + "*" + currClasses.get(i).name + "*" + classMethods.get(j).name);
                retypeClassesMenu.getItem(j).setActionCommand("RetypeMethod" + "*" + currClasses.get(i).name + "*" + classMethods.get(j).name);
                renameClassesMenu.getItem(j).setActionCommand("RenameMethod" + "*" + currClasses.get(i).name + "*" + classMethods.get(j).name);
            }
            
            // Add the method dropdowns to each class dropdown
            deleteSubMenu.add(delClassesMenu);
            retypeSubMenu.add(retypeClassesMenu);
            renameSubMenu.add(renameClassesMenu);
        }

        // Add the submenus to the top level dropdowns
        menu.getMenu(4).add(addSubMenu);
        menu.getMenu(4).add(deleteSubMenu);
        menu.getMenu(4).add(retypeSubMenu);
        menu.getMenu(4).add(renameSubMenu);
    }

    // Updates the available dropdowns for the "parameter" menu based on the state of the
    // underlying project/model
    private void updateParameterDropdowns() {
        // If dropdowns exist, delete them to rebuild the menu
        if (menu.getMenu(5).getItemCount() > 0) {
            menu.getMenu(5).remove(0);
            menu.getMenu(5).remove(0);
            menu.getMenu(5).remove(0);
            menu.getMenu(5).remove(0);
            menu.getMenu(5).remove(0);
            menu.getMenu(5).remove(0);
        }

        ArrayList<UMLClass> currClasses = project.getClasses();

        // If there are no classes, no dropdowns need to be added
        if (currClasses.size() == 0) {
           return;
        }

        // If classes exist, add the parameter options
        JMenu addSubMenu = new JMenu("Add Parameter");
        JMenu deleteSubMenu = new JMenu("Delete Parameter");
        JMenu deleteAllSubMenu = new JMenu("Delete All Parameters");
        JMenu retypeSubMenu = new JMenu("Retype Parameter");
        JMenu renameSubMenu = new JMenu("Rename Parameter");
        JMenu changeAllSubMenu = new JMenu("Change All Parameters");

        for (int i = 0; i < currClasses.size(); ++i) {

            ArrayList<UMLMethod> currMethods =  currClasses.get(i).getMethods();

            // Create a dropdown for each menu that has the class names
            JMenu addClassesMenu = new JMenu(currClasses.get(i).name);
            JMenu delClassesMenu = new JMenu(currClasses.get(i).name);
            JMenu delAllClassesMenu = new JMenu(currClasses.get(i).name);
            JMenu retypeClassesMenu = new JMenu(currClasses.get(i).name);
            JMenu renameClassesMenu = new JMenu(currClasses.get(i).name);
            JMenu changeAllClassesMenu = new JMenu(currClasses.get(i).name);

            // For each method in each class, add a dropdown for its params to
            // its class dropdown
            for (int j = 0; j < currMethods.size(); ++j) {
                // For each method, add a dropdown to add a param to it
                addClassesMenu.add(new JMenuItem(currMethods.get(j).name)); 
                addClassesMenu.getItem(j).addActionListener(this);
                addClassesMenu.getItem(j).setActionCommand("AddParameter" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name);

                // For each param in each method, add a dropdown to delete it
                ArrayList<UMLParam> currParams = currMethods.get(j).params;
                JMenu methodDelParams = new JMenu(currMethods.get(j).name);
                for (int k = 0; k < currParams.size(); ++k) {
                    methodDelParams.add(new JMenuItem(currParams.get(k).name));
                    methodDelParams.getItem(k).addActionListener(this);
                    methodDelParams.getItem(k).setActionCommand("DeleteParameter" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name + "*" + currParams.get(k).name);
                }

                delClassesMenu.add(methodDelParams); 

                // For each method, add a dropdown to delete all parameters from it
                delAllClassesMenu.add(new JMenuItem(currMethods.get(j).name)); 
                delAllClassesMenu.getItem(j).addActionListener(this);
                delAllClassesMenu.getItem(j).setActionCommand("DeleteAllParameters" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name);
            
                // For each param in each method, add a dropdown to retype it
                JMenu methodRetypeParams = new JMenu(currMethods.get(j).name);
                for (int k = 0; k < currParams.size(); ++k) {
                    methodRetypeParams.add(new JMenuItem(currParams.get(k).name));
                    methodRetypeParams.getItem(k).addActionListener(this);
                    methodRetypeParams.getItem(k).setActionCommand("RetypeParameter" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name + "*" + currParams.get(k).name);
                }

                retypeClassesMenu.add(methodRetypeParams);

                // For each param in each method, add a dropdown to rename it
                JMenu methodRenameParams = new JMenu(currMethods.get(j).name);
                for (int k = 0; k < currParams.size(); ++k) {
                    methodRenameParams.add(new JMenuItem(currParams.get(k).name));
                    methodRenameParams.getItem(k).addActionListener(this);
                    methodRenameParams.getItem(k).setActionCommand("RenameParameter" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name + "*" + currParams.get(k).name);
                }

                renameClassesMenu.add(methodRenameParams); 

                // For each method, add a dropdown to change all of its params
                changeAllClassesMenu.add(new JMenuItem(currMethods.get(j).name)); 
                changeAllClassesMenu.getItem(j).addActionListener(this);
                changeAllClassesMenu.getItem(j).setActionCommand("ChangeAllParameters" + "*" + currClasses.get(i).name + "*" + currMethods.get(j).name);
            }

            // Add each class menu to its command submenu
            addSubMenu.add(addClassesMenu);
            deleteSubMenu.add(delClassesMenu);
            deleteAllSubMenu.add(delAllClassesMenu);
            retypeSubMenu.add(retypeClassesMenu);
            renameSubMenu.add(renameClassesMenu);
            changeAllSubMenu.add(changeAllClassesMenu);
        }

        // Add the submenus to the top level dropdowns
        menu.getMenu(5).add(addSubMenu);
        menu.getMenu(5).add(deleteSubMenu);
        menu.getMenu(5).add(deleteAllSubMenu);
        menu.getMenu(5).add(retypeSubMenu);
        menu.getMenu(5).add(renameSubMenu);
        menu.getMenu(5).add(changeAllSubMenu);
    }

    // Updates the available dropdowns for the "parameter" menu based on the state of the
    // underlying project/model
    private void updateRelDropdowns() {
        // If dropdowns exist, delete them to rebuild the menu
        if (menu.getMenu(2).getItemCount() > 0) {
            menu.getMenu(2).remove(0);
            menu.getMenu(2).remove(0);
            menu.getMenu(2).remove(0);
        }

        ArrayList<UMLClass> currClasses = project.getClasses();

        // If there are no classes, no dropdowns need to be added
        if (currClasses.size() == 0) {
           return;
        }

        // If classes exist, add in relationship options
        JMenu add = new JMenu("Add Relationship");
        JMenu change = new JMenu("Change Relation Type");
        JMenu del = new JMenu("Delete Relationship");

        // List each rel type for adding a relationship
        JMenu in = new JMenu("Inheritance");
        JMenu ag = new JMenu("Aggregation");
        JMenu comp = new JMenu("Composition");
        JMenu real = new JMenu("Realization");

        // Make a dropdown for each class in the project/model for inheritance
        for (int i = 0; i < currClasses.size(); ++i) {
            String currClassName = currClasses.get(i).name;
            JMenu currClassMenu = new JMenu(currClassName);

            ArrayList<UMLRel> currClassRels = currClasses.get(i).getRels();
            
            // Keep a list of classes that have a relationship with the current class
            ArrayList<String> relClasses = new ArrayList<String>();
            for (int j = 0; j < currClassRels.size(); ++j) {
                relClasses.add(currClassRels.get(j).partner);
            }

            // Add all other classes that do not have a relationship with the current
            // class as options in that class' menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    if (!relClasses.contains(otherClassName)) {
                        JMenuItem relClass = new JMenuItem(otherClassName);
                        relClass.addActionListener(this);
                        relClass.setActionCommand("AddRelationship" + "*" + currClassName + "*" + otherClassName + "*" + "inheritance");
                        currClassMenu.add(relClass);
                    }
                }
            }

            in.add(currClassMenu);
        }

        // Make a dropdown for each class in the project/model for aggregation
        for (int i = 0; i < currClasses.size(); ++i) {
            String currClassName = currClasses.get(i).name;
            JMenu currClassMenu = new JMenu(currClassName);

            ArrayList<UMLRel> currClassRels = currClasses.get(i).getRels();
            
            // Keep a list of classes that have a relationship with the current class
            ArrayList<String> relClasses = new ArrayList<String>();
            for (int j = 0; j < currClassRels.size(); ++j) {
                relClasses.add(currClassRels.get(j).partner);
            }

            // Add all other classes that do not have a relationship with the current
            // class as options in that class' menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    if (!relClasses.contains(otherClassName)) {
                        JMenuItem relClass = new JMenuItem(otherClassName);
                        relClass.addActionListener(this);
                        relClass.setActionCommand("AddRelationship" + "*" + currClassName + "*" + otherClassName + "*" + "aggregation");
                        currClassMenu.add(relClass);
                    }
                }
            }

            ag.add(currClassMenu);
        }

        // Make a dropdown for each class in the project/model for composition
        for (int i = 0; i < currClasses.size(); ++i) {
            String currClassName = currClasses.get(i).name;
            JMenu currClassMenu = new JMenu(currClassName);

            ArrayList<UMLRel> currClassRels = currClasses.get(i).getRels();
            
            // Keep a list of classes that have a relationship with the current class
            ArrayList<String> relClasses = new ArrayList<String>();
            for (int j = 0; j < currClassRels.size(); ++j) {
                relClasses.add(currClassRels.get(j).partner);
            }

            // Add all other classes that do not have a relationship with the current
            // class as options in that class' menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    if (!relClasses.contains(otherClassName)) {
                        JMenuItem relClass = new JMenuItem(otherClassName);
                        relClass.addActionListener(this);
                        relClass.setActionCommand("AddRelationship" + "*" + currClassName + "*" + otherClassName + "*" + "composition");
                        currClassMenu.add(relClass);
                    }
                }
            }

            comp.add(currClassMenu);
        }

        // Make a dropdown for each class in the project/model for realization
        for (int i = 0; i < currClasses.size(); ++i) {
            String currClassName = currClasses.get(i).name;
            JMenu currClassMenu = new JMenu(currClassName);

            ArrayList<UMLRel> currClassRels = currClasses.get(i).getRels();
            
            // Keep a list of classes that have a relationship with the current class
            ArrayList<String> relClasses = new ArrayList<String>();
            for (int j = 0; j < currClassRels.size(); ++j) {
                relClasses.add(currClassRels.get(j).partner);
            }

            // Add all other classes that do not have a relationship with the current
            // class as options in that class' menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    if (!relClasses.contains(otherClassName)) {
                        JMenuItem relClass = new JMenuItem(otherClassName);
                        relClass.addActionListener(this);
                        relClass.setActionCommand("AddRelationship" + "*" + currClassName + "*" + otherClassName + "*" + "realization");
                        currClassMenu.add(relClass);
                    }
                }
            }

            real.add(currClassMenu);
        }

        // Make a dropdown for each class in the project/model to change or delete its 
        // rel type (if it has any relationships)
        for (int i = 0; i < currClasses.size(); ++i) {
            String currClassName = currClasses.get(i).name;

            ArrayList<UMLRel> currClassRels = currClasses.get(i).getRels();
            
            // Keep a list of classes that have a relationship with the current class
            ArrayList<ArrayList<String>> relClassesDests = new ArrayList<ArrayList<String>>();
            for (int j = 0; j < currClassRels.size(); ++j) {
                if (currClassRels.get(j).sOd.equals("src")) {
                    ArrayList<String> nameType = new ArrayList<String>(2);

                    nameType.add(currClassRels.get(j).partner);
                    nameType.add(currClassRels.get(j).type);

                    relClassesDests.add(nameType);
                }
            }

            // Add all other classes that do have a relationship with the current
            // class as options in that class' menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    // Pair the current class with its destinations
                    for (int k = 0; k < relClassesDests.size(); ++k) {
                        if (relClassesDests.get(k).get(0).equals(otherClassName)) {
                            JMenu relationship = new JMenu(currClassName + " -> " + otherClassName);
    
                            // Add an inheritance option if it's not the current relationship
                            if (!relClassesDests.get(k).get(1).equals("inheritance")) {
                                JMenuItem inher =  new JMenuItem("Inheritance");
                                inher.addActionListener(this);
                                inher.setActionCommand("ChangeRelationType" + "*" + currClassName + "*" + otherClassName + "*" + "inheritance");
                                relationship.add(inher);
                            }

                            // Add an aggregation option if it's not the current relationship
                            if (!relClassesDests.get(k).get(1).equals("aggregation")) {
                                JMenuItem inher =  new JMenuItem("Aggregation");
                                inher.addActionListener(this);
                                inher.setActionCommand("ChangeRelationType" + "*" + currClassName + "*" + otherClassName + "*" + "aggregation");
                                relationship.add(inher);
                            }

                            // Add a composition option if it's not the current relationship
                            if (!relClassesDests.get(k).get(1).equals("composition")) {
                                JMenuItem inher =  new JMenuItem("Compostion");
                                inher.addActionListener(this);
                                inher.setActionCommand("ChangeRelationType" + "*" + currClassName + "*" + otherClassName + "*" + "composition");
                                relationship.add(inher);
                            }

                            // Add a realization option if it's not the current relationship
                            if (!relClassesDests.get(k).get(1).equals("realization")) {
                                JMenuItem inher =  new JMenuItem("Realization");
                                inher.addActionListener(this);
                                inher.setActionCommand("ChangeRelationType" + "*" + currClassName + "*" + otherClassName + "*" + "realization");
                                relationship.add(inher);
                            }

                            change.add(relationship);
                        }
                    }
                }
            }

            // Add a dropdown for each relationship in the delete menu
            for (int j = 0; j < currClasses.size(); ++j) {
                String otherClassName = currClasses.get(j).name;
                if (!otherClassName.equals(currClassName)) {
                    // Pair the current class with its destinations
                    for (int k = 0; k < relClassesDests.size(); ++k) {
                        if (relClassesDests.get(k).get(0).equals(otherClassName)) {
                            JMenuItem relationship = new JMenuItem(currClassName + " -> " + otherClassName);
                            relationship.addActionListener(this);
                            relationship.setActionCommand("DeleteRelationship" + "*" + currClassName + "*" + otherClassName);

                            del.add(relationship);
                        }
                    }
                }
            }
        }

        // Add the type dropdowns to the add relationship menu
        add.add(in);
        add.add(ag);
        add.add(comp);
        add.add(real);

        // Add the menus to the topmost menu
        menu.getMenu(2).add(add);
        menu.getMenu(2).add(change);
        menu.getMenu(2).add(del);
    }

    // Updates all of the dropdowns by updating each one with their specific
    // updater
    private void updateAllDropdowns() {
        updateClassDropdowns();
        updateRelDropdowns();
        updateFieldDropdowns();
        updateMethodDropdowns();
        updateParameterDropdowns();
    }

    private String getText(String string)
	{
		String str = JOptionPane.showInputDialog(window, string, "");

		return str;
	}
    
    public void exitPrompt(ActionEvent e)
    { 
      String ObjButtons[] = {"Yes","No"};
      int PromptResult = JOptionPane.showOptionDialog(null, 
          "Are you sure you want to exit?", "Exit GUI", 
          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
          ObjButtons,ObjButtons[1]);
      if(PromptResult==0)
      {
        System.exit(0);          
      }
    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        String[] args = command.split("\\*");

        // CLASS COMMANDS
        if(args[0].equals("Create Class")){
            String classToAdd = getText("Class to Add: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (classToAdd == null) {
                return;
            }
            saveToMeme(true);
            project.addClass(classToAdd);
            getFromProject(project); 
            updateAllDropdowns();
            refresh();
            return;
        }
        if(args[0].equals("DeleteClass")){
            saveToMeme(true);
            project.deleteClass(args[1]);
            getFromProject(project); 
            updateAllDropdowns();
            refresh(); 
        }
        if(args[0].equals("RenameClass")){
            String newName = getText("New name: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newName == null) {
                return;
            }
            saveToMeme(true);
            project.renameClass(args[1], newName);
            
            // Change the panel's name in the panels array so its
            // location will stay the same when calling resetLocation
            for (int i = 0; i < panels.size(); ++i) {
                if (panels.get(i).getName().equals(args[1])) {
                    panels.get(i).setName(newName);
                    break;
                }
            }

            getFromProject(project);
            updateAllDropdowns();
            refresh();
        }
        // RELATIONSHIP COMMANDS
        if (args[0].equals("AddRelationship")){
            saveToMeme(true);
            project.addRel(args[1], args[2], args[3]);
            getFromProject(project);
            updateRelDropdowns();
            refresh();
        } 
        if (args[0].equals("ChangeRelationType")){
            saveToMeme(true);
            project.changeRelType(args[1], args[2], args[3]);
            getFromProject(project);
            updateRelDropdowns();
            refresh();
        } 
        if (args[0].equals("DeleteRelationship")){
            saveToMeme(true);
            project.delRel(args[1], args[2]);
            getFromProject(project);
            updateRelDropdowns();
            refresh();
        } 
        // FIELD COMMANDS
        if (args[0].equals("AddField")){
            String fieldToAdd = getText("Field Name: "); 
            String fieldType = getText("Field Type: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (fieldToAdd == null || fieldType == null) {
                return;
            }
            saveToMeme(true);
            project.addAttr(args[1], fieldToAdd, "field", fieldType);
            getFromProject(project);
            updateFieldDropdowns();
            refresh();
        } 
        if (args[0].equals("DeleteField")){
            saveToMeme(true);
            project.delAttr(args[1], args[2], "field");
            getFromProject(project);
            updateFieldDropdowns();
            refresh();
        } 
        if (args[0].equals("RetypeField")){
            String newFieldType = getText("New Field Type: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newFieldType == null) {
                return;
            }
            saveToMeme(true);
            project.changeFieldType(args[1], args[2], newFieldType);
            getFromProject(project);
            updateFieldDropdowns();
            refresh();
        } 
        if (args[0].equals("RenameField")){
            String newField = getText("New Field Name: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newField == null) {
                return;
            }
            saveToMeme(true);
            project.renameAttr(args[1], args[2], newField, "field");
            getFromProject(project);
            updateFieldDropdowns();
            refresh();
        } 
        // METHOD COMMANDS
        if (args[0].equals("AddMethod")){
            String methodToAdd = getText("Method Name: "); 
            String methodType = getText("Method Type: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (methodToAdd == null || methodType == null) {
                return;
            }
            saveToMeme(true);
            project.addAttr(args[1], methodToAdd, "method", methodType);
            getFromProject(project);
            updateMethodDropdowns();
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("DeleteMethod")){
            saveToMeme(true);
            project.delAttr(args[1], args[2], "method");
            getFromProject(project);
            updateMethodDropdowns();
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("RetypeMethod")){
            String newMethodType = getText("New Method Type: "); 
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newMethodType == null) {
                return;
            }
            saveToMeme(true);
            project.changeMethodType(args[1], args[2], newMethodType);
            updateMethodDropdowns();
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("RenameMethod")){
            String newMethodName = getText("New Method Name: "); 
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newMethodName == null) {
                return;
            }
            saveToMeme(true);
            project.renameAttr(args[1], args[2], newMethodName, "method");
            updateMethodDropdowns();
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        // PARAMETER COMMANDS
        if (args[0].equals("AddParameter")){
            String paramToAdd = getText("Parameter Name: ");
            String paramType = getText("Parameter Type: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (paramToAdd == null || paramType == null) {
                return;
            }
            saveToMeme(true);
            project.addParam(args[1], args[2], paramToAdd, paramType);
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("DeleteParameter")){
            saveToMeme(true); 
            project.deleteParam(args[1], args[2], args[3]);
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("DeleteAllParameters")){
            saveToMeme(true);
            project.deleteAllParams(args[1], args[2]);
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("RetypeParameter")){
            String newType = getText("New Parameter Type: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newType == null) {
                return;
            }
            saveToMeme(true);
            project.changeParamType(args[1], args[2], args[3], newType);
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("RenameParameter")){
            String newParam = getText("New Parameter Name: ");
            // If cancel is pressed when getting text from the user, the string will
            // be null, so check for it and exit the function if so (do nothing)
            if (newParam == null) {
                return;
            }
            saveToMeme(true);
            project.changeParamName(args[1], args[2], args[3], newParam);
            getFromProject(project);
            updateParameterDropdowns();
            refresh();
        } 
        if (args[0].equals("ChangeAllParameters")){
            // parseInt will fail if given a non-number, so the user
            // will be continuously asked for a number of parameters
            // until they provide a number
            boolean inputIsInt = false;
            boolean asked = false;
            int paramNum = 0;
            while (!inputIsInt) {
                String numOfParams;
                if (!asked) {
                    numOfParams = getText("How many params to add: ");
                    asked = true;
                } else {
                    numOfParams = getText("How many params to add: (Please enter a number)");
                }
                // Exit this command if cancel is pressed
                if (numOfParams == null) {
                    return;
                }
                // Otherwise try to read the integer and re-ask if not an int
                try {
                    inputIsInt = true;
                    paramNum = Integer.parseInt(numOfParams);
                } catch (Exception numberFormatException) {
                    inputIsInt = false;
                }
            }

            // Build an ArrayList of params to be passed to the controller
            ArrayList<String> paramNames = new ArrayList<String>(paramNum);
            ArrayList<String> paramTypes = new ArrayList<String>(paramNum);
            while (paramNum > 0){
                String paramName = getText("New param name: ");
                String paramType = getText("New param type: ");
                if (paramName == null || paramType == null) {
                    return;
                }
                paramNames.add(paramName);
                paramTypes.add(paramType);
                --paramNum;
            }

            saveToMeme(true);
            project.changeAllParams(args[1], args[2], paramNames, paramTypes);
            getFromProject(project);
            refresh();
        } 
        // FILE COMMANDS
        if (command.equals("Save")){
            String saveName = getText("Save Name: "); 
            saveWithLocations(saveName);
        } 
        if (command.equals("Load")){
            String loadName = getText("Name of save to load: "); 
            loadWithLocations(loadName);
            refresh();
        } 
        if (command.equals("Export as Image")) {
            String saveName = getText("Save Name: ");
            saveToImage(saveName);
        }
        if (command.equals("Undo")){
            undo();
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Redo")){
            redo();
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Exit")){
            exitPrompt(e); 
        }
    }

    // Moves the specified panel to the location associated with it
    // in the classLocations ArrayList. If the panel does not have
    // a location saved, it is not moved
    public void resetLocation(JPanel panel) {
        for (int i = 0; i < classLocations.size(); ++i) {
            if (panel.getName().equals(classLocations.get(i).get(0))) {
                panel.setLocation(Integer.parseInt(classLocations.get(i).get(1)), Integer.parseInt(classLocations.get(i).get(2)));
                repaintPanel();
                refresh();
            }
        }
    }

    // Saves the content of the JFrame to a BufferedImage and outputs it to a png
    // file
    // with the specified file name
    public void saveToImage(String fileName) {
        Container content = window.getContentPane();
        BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        content.printAll(g2d);

        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File(fileName + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Saves the x and y coordinates of each panel of the GUI 
    // and puts them in the classLocations ArrayList
    // The current locations saved in classLocations are cleared 
    // before saving new locations
    public void saveLocations() {
        classLocations.clear();
        for (int i = 0; i < panels.size(); ++i) {
            ArrayList<String> coords = new ArrayList<String>(3);
            coords.add(panels.get(i).getName());
            coords.add(Integer.toString(panels.get(i).getX()));
            coords.add(Integer.toString(panels.get(i).getY()));
            classLocations.add(coords);
        }
    }

    public void saveWithLocations (String saveName) {
        // Save all classes to a JSON file excluding their coordinates
        project.save(saveName, null);

        // Save the current locations of the panels so they can be
        // added to the JSON file that was just made
        saveLocations();

        // Make a new parser to read back through the JSON file
        JSONParser jPar = new JSONParser();
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        // String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader("saves/" + saveName + ".json")) {
            // Save the JSON array from the parser
            Object obj = jPar.parse(reader);
            JSONArray classList = (JSONArray) obj;
            
            // Loop through each class object in the JSON array
            for (int i = 0; i < classList.size(); ++i)  {
                JSONObject singleClass = (JSONObject)classList.get(i);
                String className = (String)singleClass.get("name");
                JSONArray coordsArray = new JSONArray();

                // Add the coordinates for each panel that are saved in the classLocations
                // ArrayList to their respective JSON Object in the file
                for (int j = 0; j < classLocations.size(); ++j) {
                    if (className.equals(classLocations.get(j).get(0))) {
                        coordsArray.add(classLocations.get(j).get(1));
                        coordsArray.add(classLocations.get(j).get(2));
                        singleClass.put("coordinates", coordsArray);
                        classList.set(i, singleClass);
                    }
                }
            }
            try (FileWriter file = new FileWriter("saves/" + saveName + ".json")) {
                file.write(classList.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadWithLocations (String loadName) {
        // Load the UML classes into the GUI
        project.load(loadName, null);
        getFromProject(project);
        updateAllDropdowns();

        // Create a JSON parser
        JSONParser jPar = new JSONParser();

        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader("saves/" + loadName + ".json")) {
            // Save the JSON classes array from the parser
            Object obj = jPar.parse(reader);
            JSONArray classList = (JSONArray) obj;
            
            // Loop through each class object in the JSON array
            for (int i = 0; i < classList.size(); ++i)  {
                JSONObject singleClass = (JSONObject)classList.get(i);
                String className = (String)singleClass.get("name");

                // Loop through each panel that was loaded to the GUI to see if it
                // has coordinates saved in the JSON file it was loaded from
                for (int j = 0; j < panels.size(); ++j) {
                    if (className.equals(panels.get(j).getName())) {
                        JSONArray coords = (JSONArray) singleClass.get("coordinates");
                        panel = panels.get(j);
                        // If the panel has coordinates saved, then the panel is moved to that
                        // location in the GUI. If not, it is left at the default location
                        try {
                            panel.setLocation(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)));
                        } catch (Exception e) {
                            continue;
                        }
                        repaintPanel();
                        refresh();
                        updateAllDropdowns();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToMeme(boolean meme) {
        saveLocations();
        // Create a JSON array to hold all of the classes
        JSONArray classJArray = new JSONArray();

        ArrayList<UMLClass> classes = project.getClasses();

        // Loop through the list of classes to save each class and add its
        // resulting JSON object to the JSON class array
        for (int i = 0; i < classes.size(); ++i) {
            JSONObject singleClass = classes.get(i).saveClass();

            String className = (String)singleClass.get("name");

            JSONArray coordsArray = new JSONArray();

            // Add the coordinates for each panel that are saved in the classLocations
            // ArrayList to their respective JSON Object in the file
            for (int j = 0; j < classLocations.size(); ++j) {
                if (className.equals(classLocations.get(j).get(0))) {
                    coordsArray.add(classLocations.get(j).get(1));
                    coordsArray.add(classLocations.get(j).get(2));
                    singleClass.put("coordinates", coordsArray);
                }
            }

            classJArray.add(singleClass);
        }

        if (meme) {
            undoMeme.saveState(classJArray.toJSONString());
            redoMeme.clear();
        }
        else {
            if (undoMeme.numStates() != 0) {
                redoMeme.saveState(classJArray.toJSONString());
            }
        }
    }

    // boolean meme should be true for undo and false for redo
    public void loadFromMeme(boolean meme) {
        JSONParser jPar = new JSONParser();

        try {
            String state = meme ? undoMeme.loadState() : redoMeme.loadState();
            if (state == null) {
                return;
            }
            project.clear();

            Object obj = jPar.parse(state);
            
            JSONArray classList = (JSONArray) obj;

            // Loop through each class object in the JSON array and add each class to the
            // UMLEditor
            // This ensures that each relationship can be added for each class during a
            // second loop
            // as addRel will fail if one or both classes do no already exist
            for (int i = 0; i < classList.size(); ++i) {
                JSONObject singleClass = (JSONObject) classList.get(i);
                String className = (String) singleClass.get("name");
                project.addClass(className);
            }

            // Loop through each class object in the array again to add the relationships,
            // methods, and fields for each class
            for (int classNum = 0; classNum < classList.size(); ++classNum) {
                JSONObject singleClass = (JSONObject) classList.get(classNum);

                // Save the values of the name, relationships, and attributes from the class
                // object
                String className = (String) singleClass.get("name");
                JSONArray rels = (JSONArray) singleClass.get("relationships");
                JSONArray methods = (JSONArray) singleClass.get("methods");
                JSONArray fields = (JSONArray) singleClass.get("fields");
                JSONArray coords = (JSONArray) singleClass.get("coordinates");

                // Loop through each relationship in the relationship JSON array
                // and add each relationship to the current class
                for (int relNum = 0; relNum < rels.size(); ++relNum) {
                    JSONObject relation = (JSONObject) rels.get(relNum);

                    // Save the status of the relationship relative to the current class (src or
                    // dest)
                    String relStatus = (String) relation.get("src/dest");

                    // Save the type of the relationship
                    String relType = (String) relation.get("type");

                    // Add the relationship in the correct order based on whether the
                    // current class is the source or destination
                    if (relStatus.equals("src")) {
                        project.addRel(className, (String) relation.get("className"), relType);
                    } else { // status == dest
                        project.addRel((String) relation.get("className"), className, relType);
                    }
                }

                // Loop through the fields JSON array and add each field
                for (int fieldNum = 0; fieldNum < fields.size(); ++fieldNum) {
                    JSONObject field = (JSONObject) fields.get(fieldNum);
                    project.addAttr(className, (String) field.get("name"), "field", (String) field.get("type"));
                }

                // Loop through the methods JSON array and add each method
                for (int methodNum = 0; methodNum < methods.size(); ++methodNum) {
                    JSONObject method = (JSONObject) methods.get(methodNum);

                    // Add the method to the class
                    project.addAttr(className, (String) method.get("name"), "method", (String) method.get("type"));

                    // Add all params for the method to the class
                    JSONArray params = (JSONArray) method.get("params");
                    for (int paramNum = 0; paramNum < params.size(); ++paramNum) {
                        JSONObject param = (JSONObject) params.get(paramNum);
                        project.addParam(className, (String) param.get("name"), (String) method.get(paramNum), (String) param.get("type"));
                    }
                }

                // Loop through the coordinates JSON array and set each location
                for (int fieldNum = 0; fieldNum < fields.size(); ++fieldNum) {
                    JSONObject field = (JSONObject) fields.get(fieldNum);
                    project.addAttr(className, (String) field.get("name"), "field", (String) field.get("type"));
                }

                getFromProject(project);

                // Loop through each panel that was loaded to the GUI to see if it
                // has coordinates saved in the JSON file it was loaded from
                for (int j = 0; j < panels.size(); ++j) {
                    if (className.equals(panels.get(j).getName())) {
                        panel = panels.get(j);
                        // If the panel has coordinates saved, then the panel is moved to that
                        // location in the GUI. If not, it is left at the default location
                        try {
                            panel.setLocation(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)));
                        } catch (Exception e) {
                            continue;
                        }
                        repaintPanel();
                        refresh();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        saveToMeme(false);
        loadFromMeme(true);
        updateAllDropdowns();
    }

    public void redo() {
        loadFromMeme(false);
        updateAllDropdowns();
    }

    public static JPanel findPanel(String className){

        for(int i = 0; i < panels.size(); ++i){
            if (className.equals(panels.get(i).getName())){
                return panels.get(i);
            }
        }
        return null;
    }

    /************************************************************
    * handleDrag method makes the classes draggable by mouse
    * interaction
    * Xavier & Nate
    ************************************************************/
    public void handleDrag(final JPanel panel){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                saveToMeme(true);
                x = me.getX();
                y = me.getY();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                me.translatePoint(me.getComponent().getLocation().x-x, me.getComponent().getLocation().y-y);
                panel.setLocation(me.getX(), me.getY());
                window.repaint();
            }
        });
    }

    private static void calculateEndpoints()
    {        
        if (panelFrom == panelTo)
        {
            from.x = panelFrom.getX() + panelFrom.getWidth();
            from.y = panelFrom.getY() + (panelFrom.getHeight() / 2);
            to.x = panelFrom.getX() + (panelFrom.getWidth() / 2);
            to.y = panelFrom.getY();
            return;
        }

        int xDist = panelTo.getX() - panelFrom.getX();
        int yDist = panelTo.getY() - panelFrom.getY();

        if(Math.abs(xDist) > Math.abs(yDist))
        {
            //Arrow mostly horizontal
            if(xDist > 0)
            {
                //Left to right
                from.x = panelFrom.getX() + panelFrom.getWidth();
                to.x = panelTo.getX();
            }
            else
            {
                //Right to left
                from.x = panelFrom.getX();
                to.x = panelTo.getX() + panelTo.getWidth();

            }
            from.y = panelFrom.getY() + (panelFrom.getHeight() / 2);
            to.y = panelTo.getY() + (panelTo.getHeight() / 2);
        }
        else
        {
            //Arrow mostly vertical
            if(yDist > 0)
            {
                //Top to bottom 
                from.y = panelFrom.getY() + panelFrom.getHeight();
                to.y = panelTo.getY();
            }
            else
            {
                //Bottom to top
                from.y = panelFrom.getY();
                to.y = panelTo.getY() + panelTo.getHeight();
            }
            from.x = panelFrom.getX() + (panelFrom.getWidth() / 2);
            to.x = panelTo.getX() + (panelTo.getWidth() / 2);
        }
    }

    private static void drawCap(Graphics2D g2d, boolean isDiamond, String type)
    {
        Polygon cap = new Polygon();
        cap.addPoint(to.x, to.y);
        if (isHorizontal)
        {
            if (isReverse)
            {
                cap.addPoint(to.x + CAP_MAJOR_LENGTH, to.y - CAP_MINOR_LENGTH);
                if (isDiamond)
                {
                    cap.addPoint(to.x + (CAP_MAJOR_LENGTH * 2), to.y);
                }
                cap.addPoint(to.x + CAP_MAJOR_LENGTH, to.y + CAP_MINOR_LENGTH);
            }
            else
            {
                cap.addPoint(to.x - CAP_MAJOR_LENGTH, to.y - CAP_MINOR_LENGTH);
                if (isDiamond)
                {
                    cap.addPoint(to.x - (CAP_MAJOR_LENGTH * 2), to.y);
                }
                cap.addPoint(to.x - CAP_MAJOR_LENGTH, to.y + CAP_MINOR_LENGTH);
            }
        }
        else
        {
            if (isReverse)
            {
                cap.addPoint(to.x + CAP_MINOR_LENGTH, to.y + CAP_MAJOR_LENGTH);
                if (isDiamond)
                {
                    cap.addPoint(to.x, to.y + (CAP_MAJOR_LENGTH * 2));
                }
                cap.addPoint(to.x - CAP_MINOR_LENGTH, to.y + CAP_MAJOR_LENGTH);
            }
            else
            {
                cap.addPoint(to.x + CAP_MINOR_LENGTH, to.y - CAP_MAJOR_LENGTH);
                if (isDiamond)
                {
                    cap.addPoint(to.x, to.y - (CAP_MAJOR_LENGTH * 2));
                }
                cap.addPoint(to.x - CAP_MINOR_LENGTH, to.y - CAP_MAJOR_LENGTH);
            }
        }
        g2d.draw(cap);
        if (!type.equals("composition"))
        {
            g2d.setColor(Color.PINK);
        }
        g2d.fill(cap);
    }

    private static boolean isInverse()
    {
        if (panelFrom == panelTo)
        {
            return false;
        }
        if (isHorizontal)
        {
            if (isReverse)
            {
                return from.x < to.x;
            }
            else
            {
                return from.x > to.x;
            }
        }
        else
        {
            if (isReverse)
            {
                return from.y < to.y;
            }
            else
            {
                return from.y > to.y;
            }
        }
    }



   public static void main(String[] args) throws IOException{   
    try{
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }catch(Exception e){
        e.getMessage(); 
    }
       new UMLGUI();
    }
}