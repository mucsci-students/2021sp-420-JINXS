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
            createClassPanel(classes.get(i).name);
        }

        // Create a panel for each relationship in the project
        for (int i = 0; i < classes.size(); ++i) {
            ArrayList<ArrayList<String>> rels = classes.get(i).getRels();

            for(int j = 0; j < rels.size(); j++){
                String relStatus = rels.get(j).get(1);

                // Save the type of the relationship
                String relType = rels.get(j).get(2);

                // Add the relationship in the correct order based on whether the
                // current class is the source or destination
                if (relStatus.equals("src")){
                    createRelPanel(classes.get(i).name, rels.get(j).get(0), relType);
                } else { // status == dest
                    createRelPanel(rels.get(j).get(0), classes.get(i).name, relType);
                }
            } 
        }

        // Create a panel for each field in the project for each class
        for (int i = 0; i < classes.size(); ++i) {
            ArrayList<String> fields = classes.get(i).getFields();

            for(int j = 0; j < fields.size(); j++){
                //createFieldPanel(fields.get(j));
            } 
        }

        // Create a panel for each method in the project for each class
        for (int i = 0; i < classes.size(); ++i) {
            ArrayList<ArrayList<String>> methods = classes.get(i).getMethods();

            //createMethodPanel(methods.get(i).get(0));
            for(int j = 1; j < methods.size(); j++){
                //createParamPanel(methods.get(i).get(j));
            }
        } 
    }

    private static void createClassPanel(String className){ 
        //window.remove(panel);

        panel = new JPanel();
        panel.setSize(30, 250);
        panel.setVisible(true);
        window.add(panel); 
        
		JTextArea classTxt = new JTextArea("Classname: " + className);
		classTxt.setEditable(false);


		panel.add(classTxt);
        repaintPanel();
        panels.add(panel); 

		Border bd = BorderFactory.createLineBorder(Color.GREEN);
		classTxt.setBorder(bd);
        
    }

 

    public static void createRelPanel(String src, String dest, String type){
        JPanel relPanel = new JPanel();
        relPanel.setSize(30, 250);
        relPanel.setVisible(true);
        window.add(relPanel); 
        
		JTextArea relSrc = new JTextArea("Class Source: " + src);
        JTextArea relDest = new JTextArea("Class Destination: " + dest);
        JTextArea relType = new JTextArea("Type: " + type);
		relSrc.setEditable(false);
        relDest.setEditable(false);
        relType.setEditable(false);

        // Check to see if the relationship was already added
        // from another class. If it was, don't add it to the GUI again
        String rel = src + dest + type;
        for(int i = 0; i < rels.size(); i++){
            if(rels.get(i).equals(rel)){
                return; 
            }
        }
        rels.add(rel);
        

		relPanel.add(relSrc);
        relPanel.add(relDest);
        relPanel.add(relType);

    

        

        //classList.put(className, classPanel); 

		Border bd = BorderFactory.createLineBorder(Color.RED);
		relSrc.setBorder(bd);
        relDest.setBorder(bd);
        relType.setBorder(bd);
       
        refresh(); 
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
    
    }
    



   public static void main(String[] args) throws IOException{

       //if(args.length == 1 && args[0].equals("--cli")){
           //UMLInterface console = new UMLInterface(); 
       //}
       //else{
       new UMLGUI(); 
        //}   
    //}
    }
}

