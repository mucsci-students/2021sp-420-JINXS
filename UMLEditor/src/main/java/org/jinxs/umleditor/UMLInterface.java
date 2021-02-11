package org.jinxs.umleditor;

// import java.io.FileReader;
// import java.io.FileWriter;
import java.io.File;
// import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
// import java.io.BufferedReader;
import java.util.Scanner;

public class UMLInterface {
    
    // fields that we will use
    // boolean helpfile = true; // asks if the helpfile is present

    public static void commandInterface(UMLEditor project) {
        Scanner UserInput = new Scanner(System.in);
        while (true) {
            System.out.print("$ "); // Represents the start of our terminal input, maybe can change to "> " instead
            String command = UserInput.nextLine();
            ArrayList<String> commands = parseLine(command);
            if (commands == null)
                continue;

            switch (commands.get(0)) {
                case "quit":
                    UserInput.close();
                    return;

                case "help":
                    try {
                        File helpFile = new File("helpDocument.txt");
                        Scanner helpReader = new Scanner(helpFile);
                        while (helpReader.hasNextLine()) {
                            System.out.println(helpReader.nextLine());
                        }
                        helpReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Helpfile was not found.");
                        e.printStackTrace();
                    }
                    break;

                case "addClass":
                    if (commands.size() < 2) {
                        System.out.println("Too few Arguments for addClass command");
                    }
                    else if (commands.size() > 2) {
                        System.out.println("Too many Arguments for addClass command");
                    }
                    else {
                        project.addClass(commands.get(1));
                    }
                    break;

                case "deleteClass":
                    if (commands.size() < 2) {
                        System.out.println("Too few Arguments for deleteClass command");
                    }
                    else if (commands.size() > 2) {
                        System.out.println("Too many Arguments for deleteClass command");
                    }
                    else {
                        project.deleteClass(commands.get(1));
                    }
                    break;

                case "renameClass":
                    if (commands.size() < 3) {
                        System.out.println("Too few Arguments for renameClass command");
                    }
                    else if (commands.size() > 3) {
                        System.out.println("Too many Arguments for renameClass command");
                    }
                    else {
                        project.renameClass(commands.get(1), commands.get(2));
                    }
                    break;

                case "addRel":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for  addRelationship command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for addRelationship command");
                }
                else{
                    project.addRel(commands.get(1), commands.get(2)); 
                }
                    break;

                case "deleteRel":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for  deleteRelationship command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for deleteRelationship command");
                }
                else{
                    project.delRel(commands.get(1), commands.get(2)); 
                }
                    break;

                case "addAttr":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for  addAtrribute command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for addAtribute command");
                }
                else{
                    project.addAttr(commands.get(1), commands.get(2)); 
                }
                    break;

                case "deleteAttr":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for  deleteAtrribute command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for deleteAtribute command");
                }
                else{
                    project.delAttr(commands.get(1), commands.get(2)); 
                }
                    break;

                case "renameAttr":
                if(commands.size() < 4){
                    System.out.println("Too few Arguments for  renameAtrribute command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for renameAtribute command");
                }
                // else{
                //     project.renameAttr(commands.get(1), commands.get(2), commands.get(3)); 
                // }
                //     break;

                case "save":
                if(commands.size() < 2){
                    System.out.println("Too few Arguments for save command");
                }
                else if(commands.size() > 2){
                    System.out.println("Too many Arguments for save command");
                }
                // else{
                //     project.save(commands.get(1)); 
                // }
                //     break;

                case "load":
                if(commands.size() < 2){
                    System.out.println("Too few Arguments for load command");
                }
                else if(commands.size() > 2){
                    System.out.println("Too many Arguments for load command");
                }
                // else{
                //     project.load(commands.get(1)); 
                // }
                break;
                
                case "printList":
                    project.printClassList();
                break;

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


                default:
                    System.out.println("Error: " + commands.get(0) + " is not a recognized command");

            }
        }
    }

    // Todo: Read input from user
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
        UMLEditor project = new UMLEditor();
        commandInterface(project);
        System.exit(0);
    }
}
