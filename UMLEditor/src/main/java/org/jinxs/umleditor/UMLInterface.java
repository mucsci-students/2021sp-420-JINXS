package org.jinxs.umleditor;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;

public class UMLInterface {
    
    // fields that we will use
    static BufferedReader brHelp; 
    static boolean helpFile = true; // asks if the helpfile is present

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
                        readHelpDoc(); 
                    }
                      
                    catch (Exception e) {
                     
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
                if(commands.size() < 4){
                    System.out.println("Too few Arguments for addRel command");
                }
                else if(commands.size() > 4){
                    System.out.println("Too many Arguments for addRel command");
                }
                else{
                    project.addRel(commands.get(1), commands.get(2), commands.get(3)); 
                }
                    break;

                case "deleteRel":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for deleteRel command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for deleteRel command");
                }
                else{
                    project.delRel(commands.get(1), commands.get(2)); 
                }
                    break;

                case "renameRel":
                if (commands.size() < 4) {
                    System.out.println("Too few Arguments for renameRel command");
                } else if (commands.size() > 4) {
                    System.out.println("Too many Arguments for renameRel command");
                } else {
                    project.changeRelType(commands.get(1), commands.get(2), commands.get(3));
                }
                    break;

                case "addAttr":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for addAttr command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for addAttr command");
                }
                else{
                    project.addAttr(commands.get(1), commands.get(2)); 
                }
                    break;

                case "deleteAttr":
                if(commands.size() < 3){
                    System.out.println("Too few Arguments for deleteAttr command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for deleteAttr command");
                }
                else{
                    project.delAttr(commands.get(1), commands.get(2)); 
                }
                    break;

                case "renameAttr":
                if(commands.size() < 4){
                    System.out.println("Too few Arguments for renameAttr command");
                }
                else if(commands.size() > 3){
                    System.out.println("Too many Arguments for renameAttr command");
                }
                else{
                    project.renameAttr(commands.get(1), commands.get(2), commands.get(3)); 
                }
                    break;

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

    public static void readHelpDoc() throws Exception{
        if(!helpFile){
            System.out.println("helpDocument.txt was not found"); 
            return; 
        }
        int i; 
        while((i = brHelp.read()) != -1){
            System.out.print((char) i);
        }
        brHelp.reset(); 
        System.out.print("\n");
    }

    public static void main(String[] args) {
        try{
            String filePath = new File("").getAbsolutePath();
            brHelp = new BufferedReader (new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/helpDocument.txt"));
            brHelp.mark(5000); 
        }catch(Exception FileNotFoundExcpetion){
            helpFile = false; 
        }
        UMLEditor project = new UMLEditor();
        commandInterface(project);
        System.exit(0);
    }
}
