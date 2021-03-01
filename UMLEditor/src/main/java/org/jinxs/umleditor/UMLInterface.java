package org.jinxs.umleditor;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Scanner;

public class UMLInterface {
    
    // fields that we will use
    static BufferedReader brHelpDoc; 
    static boolean helpfile = true; // asks if the helpfile is present


    /* 
    Repersents the command line for our project which uses switch cases to hold the values for the commands
    and executes them using the UMLEditor(which holds the actual implementation of the methods)
    */
    public static void commandInterface(UMLEditor project) {
        Scanner UserInput = new Scanner(System.in);
        while (true) {
            System.out.print("$ "); // Represents the start of our terminal input, maybe can change to "> " instead
            String command = UserInput.nextLine();
            ArrayList<String> commands = parseLine(command);
                if (commands == null){
                    continue;
                }
                
                //Gets the first element in the Arraylist which is our chosen command
                switch (commands.get(0)) { 

                    //Exits the program
                    case "quit":
                        UserInput.close();
                    return;

                    //Displays the help document that was written
                    case "help":
                        try {
                            readHelpDoc(); 
                        } 
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;

                    //Add a class to the project
                    case "add":
                            switch(commands.get(1)){
                                case "class":
                                if (commands.size() < 3) {
                                    System.out.println("Too few Arguments for addClass command");
                                }
                                else if (commands.size() > 3) {
                                    System.out.println("Too many Arguments for addClass command");
                                }
                                else{
                                    project.addClass(commands.get(2));
                                }
                                break;

                                case "rel":
                                if (commands.size() < 5) {
                                    System.out.println("Too few Arguments for addRel command");
                                }
                                else if (commands.size() > 5) {
                                    System.out.println("Too many Arguments for addRel command");
                                }
                                else{
                                project.addRel(commands.get(2), commands.get(3), commands.get(4));
                                }
                                break; 

                                case "field":
                                if (commands.size() < 4) {
                                    System.out.println("Too few Arguments for addfield command");
                                }
                                else if (commands.size() > 4) {
                                    System.out.println("Too many Arguments for addfield command");
                                }
                                else{
                                project.addAttr(commands.get(2), commands.get(3), commands.get(1));
                                }
                                break;

                                case "method":
                                if (commands.size() < 4) {
                                    System.out.println("Too few Arguments for addMethod command");
                                }
                                project.addAttr(commands.get(2), commands.get(3),commands.get(1));
                                for(int i = 4 ; i < commands.size(); i++){
                                    project.addParam(commands.get(2), commands.get(3), commands.get(i)); 
                                }
                                break; 
                                
                                case "param":
                                if (commands.size() < 5) {
                                    System.out.println("Too few Arguments for addParam command");
                                }
                                for(int i = 4 ; i < commands.size(); i++){
                                    project.addParam(commands.get(2), commands.get(3), commands.get(i)); 
                                }
                                break; 

                                default:
                                System.out.println("Add + " + commands.get(1) + "is not a valid command"); 
                                break;

                            }     
                    break;

                    //Deletes a class from the project
                    case "delete":
                    switch(commands.get(1)){
                        case "class":
                        if (commands.size() < 3) {
                            System.out.println("Too few Arguments for deleteClass command");
                        }
                        else if (commands.size() > 3) {
                            System.out.println("Too many Arguments for deleteClass command");
                        }
                        else{
                            project.deleteClass(commands.get(2));
                        }
                        break;

                        case "rel":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deleteRel command");
                        }
                        else if (commands.size() > 4) {
                            System.out.println("Too many Arguments for deleteRel command");
                        }
                        else{
                        project.delRel(commands.get(2), commands.get(3));
                        }
                        break; 

                        case "field":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deletefield command");
                        }
                        else if (commands.size() > 4) {
                            System.out.println("Too many Arguments for deletefield command");
                        }
                        else{
                        project.delAttr(commands.get(2), commands.get(3),commands.get(1));
                        }
                        break;

                        case "method":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deleteMethod command");
                        }
                        project.delAttr(commands.get(2), commands.get(3),commands.get(1));
                      
                        break; 
                        
                        case "param":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for addMethod command");
                        }
                            project.deleteParam(commands.get(2), commands.get(3), commands.get(4)); 
                        break; 

                        case "allParams":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deleteAllParams command");
                        }
                            project.deleteAllParams(commands.get(2), commands.get(3)); 
                        break; 

                        default:
                        System.out.println("Delete + " + commands.get(1) + " is not a valid command"); 
                        break;

                    }  
                    break;

                    //Renames a class in the project to a new name
                    case "rename":
                    switch(commands.get(1)){
                        case "class":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for renameClass command");
                        }
                        else if (commands.size() > 4) {
                            System.out.println("Too many Arguments for renameClass command");
                        }
                        else{
                            project.renameClass(commands.get(2),commands.get(3));
                        }
                        break;

                        case "relType":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for renameRelType command");
                        }
                        else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for renameRelType command");
                        }
                        else{
                        project.changeRelType(commands.get(2), commands.get(3), commands.get(4));
                        }
                        break; 

                        case "field":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for renamefield command");
                        }
                        else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for renamefield command");
                        }
                        else{
                        project.renameAttr(commands.get(2), commands.get(3), commands.get(4), commands.get(1));
                        }
                        break;

                        case "method":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for renameMethod command");
                        }
                        else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for renameMethod command");
                        }
                        project.renameAttr(commands.get(2), commands.get(3),commands.get(4),commands.get(1));
                        break; 
                        
                        case "param":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for renameParam command");
                        }
                            project.changeParam(commands.get(2), commands.get(3), commands.get(4)); 
                        break; 

                        case "allParams":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for renameAllParams command");
                        }
                        ArrayList<String> params = new ArrayList<String>();
                        for(int i = 4; i < commands.size(); i++){
                            params.add(commands.get(i)); 
                        }
                            project.changeAllParams(commands.get(2), commands.get(3), params); 
                        break; 

                        default:
                        System.out.println("Rename + " + commands.get(1) + " is not a valid command"); 
                        break;

                    }  
                   
                    break;


                    //Saves current project into a JSON named file
                    case "save":
                        if(commands.size() < 2){
                            System.out.println("Too few Arguments for save command");
                        }
                        else if(commands.size() > 2){
                            System.out.println("Too many Arguments for save command");
                        }
                        else{
                            project.save(commands.get(1)); 
                        }
                    break;

                    //Loads a project from a named JSON file
                    case "load":
                        if(commands.size() < 2){
                            System.out.println("Too few Arguments for load command");
                        }
                        else if(commands.size() > 2){
                            System.out.println("Too many Arguments for load command");
                        }
                        else{
                            project.load(commands.get(1)); 
                        }
                    break;

                    //Prints all the classes in the project
                    case "printList":
                        project.printClassList();
                    break;

                    //Prints the contents of a given class (i.e Relationships and attributes)
                    case "printContents":
                        if(commands.size() < 2){
                            System.out.println("Too few Arguments for printClassContents command");
                        }
                        else if(commands.size() > 2){
                            System.out.println("Too many Arguments for printClassContents command");
                        }
                        else{
                            project.printClassContents(commands.get(1)); 
                        }
                    break;

                    //Prints the relationships of a given classes and labels the src and destination class
                    case "printRel":
                        if(commands.size() < 2){
                            System.out.println("Too few Arguments for printRel command");
                        }
                        else if(commands.size() > 2){
                            System.out.println("Too many Arguments for printRel command");
                        }
                        else{
                            project.printRel(commands.get(1)); 
                        }
                    break;

                    //If command is not one of the specified commands above then we print an error message
                    default:
                        System.out.println("Error: " + commands.get(0) + " is not a recognized command");

            }
        }
    }

    //Reads in the helpfile if present
    public static void readHelpDoc() throws Exception{
        if(!helpfile){
            System.out.println("helpDocument.txt was not found");
            return;
        }
        int i;
        while((i = brHelpDoc.read()) != -1){
            System.out.print ((char) i);
            brHelpDoc.reset();
            System.out.print("\n"); 
        }
    }

    // Reads input from user and parses it into tokens which is returned as an arrayList of these tokens
    public static ArrayList<String> parseLine(String command) {
        ArrayList<String> commandList = new ArrayList<String>();

        StringTokenizer input = new StringTokenizer(command);

        while (input.hasMoreTokens()) {
            commandList.add(input.nextToken());
        }
        if (commandList.size() < 1) {
            return null;
        }

        return commandList;
    }

    public static void main(String[] args) {
        try{
            brHelpDoc = new BufferedReader(new FileReader("helpDocument.txt")); 
        }
        catch(Exception FileNotFoundException){
            helpfile = false;  
        }

        UMLEditor project = new UMLEditor();
        commandInterface(project);
        System.exit(0);
    }
}
