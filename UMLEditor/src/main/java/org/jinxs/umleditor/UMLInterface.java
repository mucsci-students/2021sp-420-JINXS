package org.jinxs.umleditor;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;

// Save and load imports
// For writing out to a file when saving
import java.io.FileWriter;
import java.io.IOException;

// For the JSON array of classes to be written to file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UMLInterface {
    
    // fields that we will use
    static BufferedReader brHelpDoc; 
    static boolean helpfile = true; // asks if the helpfile is present

    // Holds class locations if a loaded file has GUI coordinates so they can be
    // applied when the project is saved again
    private static ArrayList<ArrayList<String>> classLocations = new ArrayList<ArrayList<String>>();


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
                                System.out.println("Add + " + commands.get(1) + " is not a valid command"); 
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
                            System.out.println("Too few Arguments for addParam command");
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
                        if (commands.size() < 6) {
                            System.out.println("Too few Arguments for renameParam command");
                        }
                            project.changeParam(commands.get(2), commands.get(3), commands.get(4), commands.get(5)); 
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
                            restoreLoadCoords(commands.get(1));
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
                            storeLoadCoords(commands.get(1)); 
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
        try {
            String filePath = new File("").getAbsolutePath();
            brHelpDoc = new BufferedReader (new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/helpDocument.txt"));
        } catch (Exception FileNotFoundException) {
            try {
                String filePath = new File("").getAbsolutePath();
                brHelpDoc = new BufferedReader (new FileReader(filePath + "/src/main/java/org/jinxs/umleditor/helpDocument.txt"));
            } catch (Exception e) {
                helpfile = false;
            }
        }
        if(!helpfile){
            System.out.println("helpDocument.txt was not found");
            return;
        }
        int i;
        while((i = brHelpDoc.read()) != -1){
            System.out.print ((char) i);
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

    // Looks through the save file that was loaded and stores any GUI coordinates
    // associated with each class that was added so they can be readded if/when
    // the editor is saved again
    public static void storeLoadCoords (String fileName) {
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();

        // Open the file that was just loaded
        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + fileName + ".json")) {
            // Save the JSON array from the parser
            Object obj = jPar.parse(reader);
            JSONArray classList = (JSONArray) obj;

            // Clear previously stored locations if another file was loaded earlier
            classLocations.clear();

            // Get each class' JSONObject and check if it has saved coordinates
            for (int i = 0; i < classList.size(); ++i) {
                JSONObject singleClass = (JSONObject)classList.get(i);

                // Holds the coordinates and the class name
                ArrayList<String> locations = new ArrayList<String>(3);
                locations.add((String)singleClass.get("name"));

                JSONArray coords = (JSONArray) singleClass.get("coordinates");
                // If the class has coordinates, add them to the locations ArrayList
                // and add the list to the classLocations ArrayList
                if (coords != null) {
                    locations.add((String)coords.get(0));
                    locations.add((String)coords.get(1));
                    classLocations.add(locations);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void restoreLoadCoords (String fileName) {
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        String filePath = new File("").getAbsolutePath();
        try (FileReader reader = new FileReader(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + fileName + ".json")) {
            // Save the JSON array from the parser
            Object obj = jPar.parse(reader);
            JSONArray classList = (JSONArray) obj;
            
            // Loop through each class object in the JSON array from the saved file 
            for (int i = 0; i < classList.size(); ++i)  {
                JSONObject singleClass = (JSONObject)classList.get(i);

                // Get the current class' name and initialize a JSONArray to
                // hold coordinates if they exist
                String className = (String)singleClass.get("name");
                JSONArray coordsArray = new JSONArray();
                for (int j = 0; j < classLocations.size(); ++j) {
                    // If the class from the JSON file exists in the classLocations
                    // ArrayList, then it has coordinates that need to be added to the
                    // JSON file
                    if (className.equals(classLocations.get(j).get(0))) {
                        coordsArray.add(classLocations.get(j).get(1));
                        coordsArray.add(classLocations.get(j).get(2));
                        singleClass.put("coordinates", coordsArray);
                        classList.set(i, singleClass);
                    }
                }
            }
            try (FileWriter file = new FileWriter(filePath + "/UMLEditor/src/main/java/org/jinxs/umleditor/saves/" + fileName + ".json")) {
                file.write(classList.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            UMLGUI gui = new UMLGUI();
        }
        else if (args.length > 0) {
            if (args.length == 1 && args[0].equals("--cli")) {
                UMLEditor project = new UMLEditor();
                commandInterface(project);
            }
            else {
                System.out.print("Unrecognized argument(s): \"");
                for (int i = 0; i < args.length; ++i) {
                    System.out.print(args[i]);
                    if (i + 1 < args.length) {
                        System.out.print(" ");
                    }
                }
                System.out.println("\"");
            }
        }
    }
}
