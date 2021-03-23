package org.jinxs.umleditor;

import javax.swing.JOptionPane;
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
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

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
     
    // handleDrag global coordinates
    int x;
    int y;


    

    public static void umlWindow(){
 
        window = new JFrame("Graphical User Interface");
        window.setLayout(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800,700);
        

        menu = new JMenuBar(); 
        createFileMenu(menu); 
        createClassMenu(menu); 
        createRelationshipMenu(menu); 
        createFieldMenu(menu);  
        createMethodMenu(menu);
        createParamMenu(menu); 

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

            /* Old relationship builder: not needed once arrows are implemented
            // Create a panel for each relationship in the project
            ArrayList<ArrayList<String>> rels = classes.get(i).getRels();

            for(int j = 0; j < rels.size(); ++j){
                String dest = rels.get(j).get(0);

                // Save the type of the relationship
                String type = rels.get(j).get(2);

                JTextArea relDest = new JTextArea("Class Destination: " + dest);
                JTextArea relType = new JTextArea("Type: " + type);
                relDest.setEditable(false);
                relType.setEditable(false);

                panel.add(relDest);
                panel.add(relType); 

		        Border bdRel = BorderFactory.createLineBorder(Color.RED);
                relDest.setBorder(bdRel);
                relType.setBorder(bdRel);
            } 
            */

            // Store the fields for the current class
            ArrayList<String> fields = classes.get(i).getFields();

            // If fields exist in the project for the current class, add them to a textarea with 
            // each on their own line
            // The size of the panel will also adjust its height and width to hold each new field
            if(fields.size() > 0){
                String str = ""; 
                for(int j = 0; j < fields.size(); j++){
                    // Make the panel 20 pixels taller for each field
                    panel.setSize(panel.getWidth(), (panel.getHeight() + 20));

                    String field = fields.get(j);
                    // Fence-post problem: only add a new line character for each
                    // field beyond the first one
                    if (j != 0){
                        str += "\n"; 
                    } 
                    str += field;
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
            ArrayList<ArrayList<String>> methods = classes.get(i).getMethods();

            // If methods exist in the project for this class, add them to a textarea with each on 
            // their own line 
            // The size of the panel will also adjust its height and width to hold each new method
            if (methods.size() > 0) {
                String methodString = "";

                for(int j = 0; j < methods.size(); j++) {
                    // Make the panel 20 pixels taller for each method
                    panel.setSize(panel.getWidth(), (panel.getHeight() + 20));
                    
                    String methodName = methods.get(j).get(0);
                    // Fencepost problem: only add a newline before every method
                    // after the first one
                    if (j != 0) {
                        methodString += "\n";
                    }

                    // Put the fields for the current method in parentheses like an actual method
                    methodString += methodName + "(";

                    for(int k = 1; k < methods.get(j).size(); ++k){
                        String param = methods.get(j).get(k);
                        // Fencepost problem: only add a comma if another param exists
                        // beyond the first
                        if (k != 1) {
                            methodString += ", ";
                        }
                        methodString += param;
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

            // Put a black border around the entire class panel so its boundaries
            // are visible
            Border bdPanel = BorderFactory.createLineBorder(Color.BLACK);
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
		JMenuItem exit = new JMenuItem("Exit");

        JMenuItem[] fileArray = {save,load,exit}; 
        String[] labelText = {"Save","Load","Exit"}; 
             

        for(int i = 0; i < 3; ++i)
		{
			file.add(fileArray[i]);
			fileArray[i].setToolTipText(labelText[i]);
			
		}

        menu.add(file); 
    }

    public static void createClassMenu(JMenuBar menu){
        JMenu classes = new JMenu("Class"); 
        JMenuItem addClass = new JMenuItem("Create class");
		JMenuItem deleteClass = new JMenuItem("Delete class");
		JMenuItem renameClass = new JMenuItem("Rename class");

        JMenuItem[] classArray = {addClass,deleteClass, renameClass}; 
        String[] labelText = {"addClass", "deleteClass" , "renameClass"}; 
        String[] commands = {"addClass", "deleteClass", "renameClass"}; 
             

        for(int i = 0; i < 3; ++i)
		{
			classes.add(classArray[i]);
			classArray[i].setToolTipText(labelText[i]);
            classArray[i].setActionCommand(commands[i]);
		}

        menu.add(classes); 
    }

    public static void createRelationshipMenu(JMenuBar menu){
        JMenu relationship = new JMenu("Relationship"); 
        JMenuItem in = new JMenuItem("Inheritance");
        JMenuItem ag = new JMenuItem("Aggregation");
        JMenuItem comp = new JMenuItem("Composition");
        JMenuItem gen = new JMenuItem("Realization");
        JMenuItem change = new JMenuItem("Change Relation Type");
        JMenuItem del = new JMenuItem("Delete Relationship");


		

        JMenuItem[] relArray = {in,ag,comp,gen,change,del}; 
        String[] labelText = {"Inheritance", "Aggregation", "Composition", "Realization", 
                                    "Change Relation Type", "Delete Relationship"};      

        for(int i = 0; i < 6; ++i)
		{
			relationship.add(relArray[i]);
			relArray[i].setToolTipText(labelText[i]);
		}

        menu.add(relationship); 
    }

    public static void createFieldMenu(JMenuBar menu){
        JMenu field = new JMenu("Field"); 

        JMenuItem addField = new JMenuItem("Add Field");
        JMenuItem deleteField = new JMenuItem("Delete Field");
        JMenuItem renameField = new JMenuItem("Rename Field");


        JMenuItem[] fieldArray = {addField, deleteField, renameField}; 
        String[] labelText = {"Add Field","Delete Field","Rename Field"}; 
             

        for(int i = 0; i < 3; ++i)
		{
			field.add(fieldArray[i]);
			fieldArray[i].setToolTipText(labelText[i]);
			
		}

        menu.add(field); 
    }

    public static void createMethodMenu(JMenuBar menu){
        JMenu method = new JMenu("Method"); 

        JMenuItem addMethod = new JMenuItem("Add Method");
        JMenuItem deleteMethod = new JMenuItem("Delete Method");
        JMenuItem renameMethod = new JMenuItem("Rename Method");
	

        JMenuItem[] methodArray = {addMethod, deleteMethod, renameMethod}; 
        String[] labelText = {"Add Method","Delete Method","Rename Method"}; 
             

        for(int i = 0; i < 3; ++i)
		{
			method.add(methodArray[i]);
			methodArray[i].setToolTipText(labelText[i]);
			
		}

        menu.add(method); 
    }

    public static void createParamMenu(JMenuBar menu){
        JMenu param = new JMenu("Parameter"); 

        JMenuItem addParam = new JMenuItem("Add Parameter");
        JMenuItem deleteParam = new JMenuItem("Delete Parameter");
        JMenuItem deleteAllParams = new JMenuItem("Delete All Parameters");
        JMenuItem renameParam = new JMenuItem("Rename Parameter");
        JMenuItem renameAllParams = new JMenuItem("Rename All Parameters");
	

        JMenuItem[] paramArray = {addParam, deleteParam, deleteAllParams, renameParam, renameAllParams}; 
        String[] labelText = {"Add Parameter","Delete Parameter","Delete All Parameters", "Rename Parameter", "Rename All Parameters"}; 
             

        for(int i = 0; i < 5; ++i)
		{
			param.add(paramArray[i]);
			paramArray[i].setToolTipText(labelText[i]);
			
		}

        menu.add(param); 
    }

    private String getText(String string)
	{
		String str = JOptionPane.showInputDialog(window, string, JOptionPane.PLAIN_MESSAGE);

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

    public UMLGUI() {
        umlWindow();

        for (int i = 0; i < menu.getMenuCount(); ++i) {
            JMenu singleMenu = menu.getMenu(i);
            for (int j = 0; j < singleMenu.getItemCount(); ++j) {
                JMenuItem menuItem = singleMenu.getItem(j);
                menuItem.addActionListener(this);
            }
        }

    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();

        // CLASS COMMANDS
        if(command.equals("addClass")){
            String classToAdd = getText("Class to Add: ");
            project.addClass(classToAdd); 
            getFromProject(project); 
            refresh();
               
        }
        if(command.equals("deleteClass")){
            String classToDelete = getText("Class to Delete: ");
            project.deleteClass(classToDelete);
            getFromProject(project); 
            refresh(); 
        }
        if(command.equals("renameClass")){
            String class1 = getText("Class to rename: ");
            String class2 = getText("New name: ");
            project.renameClass(class1, class2);
            getFromProject(project);
            refresh();
        }
        // RELATIONSHIP COMMANDS
        if (command.equals("Inheritance")){
            String relToAdd = getText("Source Class: "); 
            String relToAdd2 = getText("Destination Class: "); 
            project.addRel(relToAdd, relToAdd2, "inheritance");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Aggregation")){
            String relToAdd = getText("Source Class: "); 
            String relToAdd2 = getText("Destination Class: "); 
            project.addRel(relToAdd, relToAdd2, "aggregation");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Composition")){
            String relToAdd = getText("Source Class: "); 
            String relToAdd2 = getText("Destination Class: "); 
            project.addRel(relToAdd, relToAdd2, "composition");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Realization")){
            String relToAdd = getText("Source Class: "); 
            String relToAdd2 = getText("Destination Class: "); 
            project.addRel(relToAdd, relToAdd2, "realization");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Change Relation Type")){
            String class1 = getText("Class 1: "); 
            String class2 = getText("Class 2: ");
            String type = getText("New type: ");
            project.changeRelType(class1, class2, type);
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Delete Relationship")){
            String class1 = getText("Class 1: "); 
            String class2 = getText("Class 2: "); 
            project.delRel(class1, class2);
            getFromProject(project);
            refresh();
        } 
        // FIELD COMMANDS
        if (command.equals("Add Field")){
            String classToAdd = getText("Class: "); 
            String fieldToAdd = getText("Field Name: "); 
            project.addAttr(classToAdd, fieldToAdd, "field");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Delete Field")){
            String className = getText("Class: "); 
            String fieldToDel = getText("Field to Delete: "); 
            project.delAttr(className, fieldToDel, "field");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Rename Field")){
            String className = getText("Class: "); 
            String oldField = getText("Field to Rename: ");
            String newField = getText("New Field Name: ");
            project.renameAttr(className, oldField, newField, "field");
            getFromProject(project);
            refresh();
        } 
        // METHOD COMMANDS
        if (command.equals("Add Method")){
            String classToAdd = getText("Class: "); 
            String methodToAdd = getText("Method Name: "); 
            project.addAttr(classToAdd, methodToAdd, "method");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Delete Method")){
            String className = getText("Class: "); 
            String methodToDelete = getText("Method to Delete: "); 
            project.delAttr(className, methodToDelete, "method");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Rename Method")){
            String className = getText("Class: "); 
            String methodToRename = getText("Method to Rename: "); 
            String newMethodName = getText("New Method Name: "); 
            project.renameAttr(className, methodToRename, newMethodName, "method");
            getFromProject(project);
            refresh();
        } 
        // PARAMETER COMMANDS
        if (command.equals("Add Parameter")){
            String classToAdd = getText("Class: "); 
            String methodToAdd = getText("Method Name: "); 
            String paramToAdd = getText("Parameter Name: "); 
            project.addParam(classToAdd, methodToAdd, paramToAdd);
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Delete Parameter")){
            String className = getText("Class: "); 
            String methodName = getText("Method Name: "); 
            String paramName = getText("Parameter to Delete: "); 
            project.deleteParam(className, methodName, paramName);
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Delete All Parameters")){
            String className = getText("Class: "); 
            String methodName = getText("Method to delete all params from: ");  
            project.deleteAllParams(className, methodName);
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Rename Parameter")){
            String className = getText("Class: "); 
            String methodName = getText("Method Name: "); 
            String oldParam = getText("Parameter to Change: "); 
            String newParam = getText("New Parameter Name: ");
            project.changeParam(className, methodName, oldParam, newParam);
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Rename All Parameters")){
            String className = getText("Class: "); 
            String methodName = getText("Name of method to change all params: "); 

            // parseInt will fail if given a non-number, so the user
            // will be continuously asked for a number of parameters
            // until they provide a number
            boolean inputIsInt = false;
            int paramNum = 0;
            while (!inputIsInt) {
                String numOfParams = getText("How many params to add: ");
                try {
                    inputIsInt = true;
                    paramNum = Integer.parseInt(numOfParams);
                } catch (Exception numberFormatException) {
                    inputIsInt = false;
                }
            }

            ArrayList<String> params = new ArrayList<String>(paramNum);
            while (paramNum > 0){
                String paramName = getText("New param name: ");
                params.add(paramName);
                --paramNum;
            }
            project.changeAllParams(className, methodName, params);
            getFromProject(project);
            refresh();
        } 
        // SAVE/LOAD COMMANDS
        if (command.equals("Save")){
            String saveName = getText("Save Name: "); 
            saveWithLocations(saveName);
        } 
        if (command.equals("Load")){
            String loadName = getText("Name of save to load: "); 
            loadWithLocations(loadName);
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
        project.save(saveName);

        // Save the current locations of the panels so they can be
        // added to the JSON file that was just made
        saveLocations();

        // Make a new parser to read back through the JSON file
        JSONParser jPar = new JSONParser();
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + saveName + ".json")) {
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
            try (FileWriter file = new FileWriter(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + saveName + ".json")) {
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
        project.load(loadName);
        getFromProject(project);

        // Create a JSON parser
        JSONParser jPar = new JSONParser();

        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + loadName + ".json")) {
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
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

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
                x = me.getX();
                y = me.getY();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                me.translatePoint(me.getComponent().getLocation().x-x, me.getComponent().getLocation().y-y);
                panel.setLocation(me.getX(), me.getY());
            }
        });
    }



   public static void main(String[] args) throws IOException{
       new UMLGUI();
    }
}

