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
import java.awt.*; 
import java.util.HashMap;
import java.util.Map;




public class UMLGUI implements ActionListener{
 
    private static JFrame window; 
    private static JMenuBar menu; 
    //private static Map<String,JPanel> classList; 

    public static void umlWindow(UMLEditor project){
 
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

        window.setJMenuBar(menu);



        window.setVisible(true);   

    }

    private static void createClass(String className){

        classPanel(className); 
        refresh(); 
    }

    public static void classPanel(String className){
        JPanel classPanel = new JPanel();
        classPanel.setSize(30, 250);
        classPanel.setVisible(true);
        window.add(classPanel); 
        
		JTextArea classTxt = new JTextArea(className);
		classTxt.setEditable(false);
		classPanel.add(classTxt);
       

		Border bd = BorderFactory.createLineBorder(Color.RED);
		classTxt.setBorder(bd);
		window.add(classPanel);

    }

    private static void refresh(){
        window.repaint(); 
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
        String[] commands = {"Add", "Delete", "Rename"}; 
             

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
        JMenuItem gen = new JMenuItem("Generalization");


		

        JMenuItem[] relArray = {in,ag,comp,gen}; 
        String[] labelText = {"Inheritance", "Aggregation", "Composition", "Generalization"};      

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

    private String getText(String string)
	{
		String str = "Classname: " + JOptionPane.showInputDialog(window, string, JOptionPane.PLAIN_MESSAGE);

		return str;
	}

    public UMLGUI(UMLEditor project) {
        umlWindow(project);

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
        UMLEditor project = new UMLEditor();
        if(command.equals("Add")){
            String classToAdd = getText("Class to Add: ");
            project.addClass(classToAdd);  
            createClass(classToAdd); 
            refresh(); 
               
        }
        if(command.equals("Delete")){
            String classToDelete = getText("Class to Delete: ");
            project.deleteClass(classToDelete);
            refresh(); 
        }
    
    }
    



   public static void main(String[] args) {
       UMLEditor project = new UMLEditor(); 
       new UMLGUI(project);
    }   
}

