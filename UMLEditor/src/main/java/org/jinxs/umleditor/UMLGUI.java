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



public class UMLGUI implements ActionListener{
 
    private static JFrame window; 
    private static JMenuBar menu; 
    //private static Map<String,JPanel> classList = new HashMap<String,JPanel>();

    private static UMLEditor project = new UMLEditor(); 
    //private static JTextArea textArea; 
    private static JPanel panel;
    private static ArrayList<JPanel> panels = new ArrayList<JPanel>();
    private static ArrayList<String> rels = new ArrayList<String>(); 
     


    

    public static void umlWindow(){
 
        window = new JFrame("Graphical User Interface");
        window.setLayout(new GridLayout(5,5));
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

    

    public static void getFromProject(UMLEditor project){
        // Delete all panels on the GUI to redraw the current state of the project
        // to the GUI
        panels.clear();

        // Create a panel for each class in the project
        ArrayList<UMLClass> classes = project.getClasses(); 
        for (int i = 0; i < classes.size(); ++i) {
            panel = new JPanel();
            panel.setSize(800, 250);
            panel.setVisible(true);
            window.add(panel); 
        
		    JTextArea classTxt = new JTextArea("Classname: " + classes.get(i).name);
		    classTxt.setEditable(false); 

		    Border bdClass = BorderFactory.createLineBorder(Color.GREEN);
		    classTxt.setBorder(bdClass);

            panel.add(classTxt);

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

            ArrayList<String> fields = classes.get(i).getFields();

            for(int j = 0; j < fields.size(); j++){
                String field = fields.get(j);

                JTextArea fieldText = new JTextArea("Field" + (j+1) +": " + field);
                fieldText.setEditable(false);

                panel.add(fieldText);

                Border bdField = BorderFactory.createLineBorder(Color.BLUE);
                fieldText.setBorder(bdField);
            } 

            ArrayList<ArrayList<String>> methods = classes.get(i).getMethods();

            for(int j = 0; j < methods.size(); j++){
                String methodName = methods.get(j).get(0);

                JTextArea methodText = new JTextArea("Method: " + methodName + ": Params: ");
                methodText.setEditable(false);

                for(int k = 1; k < methods.get(j).size(); ++k){
                    String param = methods.get(j).get(k);
                    methodText.append(param + ", ");
                }

                panel.add(methodText);

                Border bdField = BorderFactory.createLineBorder(Color.ORANGE);
                methodText.setBorder(bdField);
            }

            repaintPanel();
            panels.add(panel);
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


		

        JMenuItem[] relArray = {in,ag,comp,gen}; 
        String[] labelText = {"Inheritance", "Aggregation", "Composition", "Realization"};      

        for(int i = 0; i < 4; ++i)
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
        JMenuItem renameParam = new JMenuItem("Rename parameter");
        JMenuItem renameAllParams = new JMenuItem("Rename All Parameters");
	

        JMenuItem[] paramArray = {addParam, deleteParam, deleteAllParams, renameParam, renameAllParams}; 
        String[] labelText = {"Add Parameter","Delete Parameter","Delete All Parameters", "Rename Parameter", "renameAllParams"}; 
             

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
        if(panels.size() > 0){

        for(int i = 0; i < panels.size(); i++){
            window.remove(panels.get(i));
        }
        panels.clear();
    }

        // CLASS COMMANDS
        if(command.equals("addClass")){
            String classToAdd = getText("Class to Add: ");
            project.addClass(classToAdd); 
            getFromProject(project); 
            repaintPanel(); 
               
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
            project.addRel(relToAdd, relToAdd2, "inheritance");
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
        // METHOD COMMANDS
        if (command.equals("Add Method")){
            String classToAdd = getText("Class: "); 
            String methodToAdd = getText("Method Name: "); 
            project.addAttr(classToAdd, methodToAdd, "method");
            getFromProject(project);
            refresh();
        } 
        if (command.equals("Add Parameter")){
            String classToAdd = getText("Class: "); 
            String methodToAdd = getText("Method Name: "); 
            String paramToAdd = getText("Parameter Name: "); 
            project.addParam(classToAdd, methodToAdd, paramToAdd);
            getFromProject(project);
            refresh();
        } 
        // SAVE/LOAD COMMANDS
        if (command.equals("Save")){
            String saveName = getText("Save Name: "); 
            project.save(saveName);
        } 
        if (command.equals("Load")){
            String loadName = getText("Name of save to load: "); 
            project.load(loadName);
            getFromProject(project);
            refresh();
        } 
    }
    



   public static void main(String[] args) throws IOException{
       new UMLGUI();
    }
}

