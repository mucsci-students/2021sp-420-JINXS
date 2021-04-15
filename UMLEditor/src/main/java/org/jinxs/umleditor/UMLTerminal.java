package org.jinxs.umleditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;

// Save and load imports
// For writing out to a file when saving
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

// For the JSON array of classes to be written to file
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


// For Jline functionality
import org.jline.builtins.*;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Options.HelpException;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.LineReader.Option;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.DefaultParser.Bracket;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.*;
import org.jline.utils.*;
import org.jline.utils.InfoCmp.Capability;

public class UMLTerminal{

    private static Terminal terminal;
    private static LineReader reader;
    private AggregateCompleter comp;

    // fields that we will use
    static BufferedReader brHelpDoc; 
    static boolean helpfile = true; // asks if the helpfile is present

    // Holds class locations if a loaded file has GUI coordinates so they can be
    // applied when the project is saved again
    private static ArrayList<ArrayList<String>> classLocations = new ArrayList<ArrayList<String>>();

    private UMLEditor project;

    public UMLTerminal(){
        project = new UMLEditor();
        comp = new UMLTabCompleter().update(project);
    }

    public void build(){
        project = new UMLEditor();
        comp = new UMLTabCompleter().update(project);
        try{
            terminal = TerminalBuilder.builder().system(true).build();
            reader = LineReaderBuilder.builder().terminal(terminal).completer(comp).build();
            boolean result = true;

            while(result){
                try {
                    String line = null;
                    line = reader.readLine("$ ", null, (MaskingCallback) null, null);
                    line = line.trim();
                    ArrayList<String> commands = new ArrayList<String>(Arrays.asList(line.split(" ")));
                    result = interpreter(commands);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            System.out.println("Terminal failed to initialize");
        }
            
    }

    public boolean interpreter(ArrayList<String> commands){
        
        if (commands == null){
            return false;
        }

        switch (commands.get(0)) { 

            //Exits the program
            case "quit":
                try{
                terminal.close();
                }
                catch(IOException e){
                    System.out.println("Terminal failed to close");
                }
            return false;

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
                            project.saveToMeme(true);
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
                            project.saveToMeme(true);
                            project.addRel(commands.get(2), commands.get(3), commands.get(4));
                        }
                    break; 

                    case "field":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for addfield command");
                        }
                        else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for addfield command");
                        }
                        else{
                            project.saveToMeme(true);
                            project.addAttr(commands.get(2), commands.get(4), commands.get(1), commands.get(3));
                        }
                    break;

                    case "method":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for addMethod command");
                        }
                        else{
                            project.saveToMeme(true);
                            project.addAttr(commands.get(2), commands.get(4),commands.get(1), commands.get(3));
                            for(int i = 5; i < commands.size(); i += 2){
                                project.addParam(commands.get(2), commands.get(4), commands.get(i + 1), commands.get(i));
                            }
                        }
                    break; 
                    
                    case "param":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for addParam command");
                        }
                        else{
                            project.saveToMeme(true);
                            for(int i = 4; i < commands.size(); i += 2){
                                project.addParam(commands.get(2), commands.get(3), commands.get(i + 1), commands.get(i)); 
                            }
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
                            project.saveToMeme(true);
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
                            project.saveToMeme(true);
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
                            project.saveToMeme(true);
                            project.delAttr(commands.get(2), commands.get(3),commands.get(1));
                        }
                    break;

                    case "method":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deleteMethod command");
                        }
                        project.saveToMeme(true);
                        project.delAttr(commands.get(2), commands.get(3),commands.get(1));
                    break; 
                    
                    case "param":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for addParam command");
                        }
                        project.saveToMeme(true);
                        project.deleteParam(commands.get(2), commands.get(3), commands.get(4));
                    break; 

                    case "allParams":
                        if (commands.size() < 4) {
                            System.out.println("Too few Arguments for deleteAllParams command");
                        }
                        project.saveToMeme(true);
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
                            project.saveToMeme(true);
                            project.renameClass(commands.get(2),commands.get(3));
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
                            project.saveToMeme(true);
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
                        project.saveToMeme(true);
                        project.renameAttr(commands.get(2), commands.get(3),commands.get(4),commands.get(1));
                    break; 
                    
                    case "param":
                        if (commands.size() < 6) {
                            System.out.println("Too few Arguments for renameParam command");
                        }
                        project.saveToMeme(true);
                        project.changeParamName(commands.get(2), commands.get(3), commands.get(4), commands.get(5));
                    break; 

                    case "allParams":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for renameAllParams command");
                        }
                        ArrayList<String> params = new ArrayList<String>();
                        ArrayList<String> pTypes = new ArrayList<String>();
                        for(int i = 4; i < commands.size(); i += 2){
                            params.add(commands.get(i + 1));
                            pTypes.add(commands.get(i));
                        }
                        project.saveToMeme(true);
                        project.changeAllParams(commands.get(2), commands.get(3), params, pTypes);
                    break; 

                    default:
                        System.out.println("Rename + " + commands.get(1) + " is not a valid command"); 
                    break;
                }
            break;
            case "change":
                switch(commands.get(1)) {
                    case "relType":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for change relType command");
                        } else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for change relType command");
                        } else {
                            project.saveToMeme(true);
                            project.changeRelType(commands.get(2), commands.get(3), commands.get(4));
                        }
                    break;

                    case "fieldType":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for change fieldType command");
                        } else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for change fieldType command");
                        } else {
                            project.saveToMeme(true);
                            project.changeFieldType(commands.get(2), commands.get(3), commands.get(4));
                        }
                    break;

                    case "methodType":
                        if (commands.size() < 5) {
                            System.out.println("Too few Arguments for change fieldType command");
                        } else if (commands.size() > 5) {
                            System.out.println("Too many Arguments for change fieldType command");
                        } else {
                            project.saveToMeme(true);
                            project.changeMethodType(commands.get(2), commands.get(3), commands.get(4));
                        }
                    break;

                    case "paramType":
                        if (commands.size() < 6) {
                            System.out.println("Too few Arguments for change fieldType command");
                        } else if (commands.size() > 6) {
                            System.out.println("Too many Arguments for change fieldType command");
                        } else {
                            project.saveToMeme(true);
                            project.changeParamType(commands.get(2), commands.get(3), commands.get(4), commands.get(5));
                        }
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
                    int lastSlash = -1;
                    if (commands.get(1).contains("\\")) {
                        lastSlash = commands.get(1).lastIndexOf("\\");
                    } else if (commands.get(1).contains("/")) {
                        lastSlash = commands.get(1).lastIndexOf("/");
                    } else {
                        project.save(commands.get(1), null);
                        restoreLoadCoords(commands.get(1), null);
                        break;
                    }
                    project.save(commands.get(1).substring(lastSlash + 1), commands.get(1).substring(0, lastSlash + 1));
                    restoreLoadCoords(commands.get(1).substring(lastSlash + 1), commands.get(1).substring(0, lastSlash + 1));
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
                    int lastSlash = -1;
                    if (commands.get(1).contains("\\")) {
                        lastSlash = commands.get(1).lastIndexOf("\\");
                    } else if (commands.get(1).contains("/")) {
                        lastSlash = commands.get(1).lastIndexOf("/");
                    } else {
                        project.load(commands.get(1), null);
                        storeLoadCoords(commands.get(1), null);
                        break;
                    }
                    project.load(commands.get(1).substring(lastSlash + 1), commands.get(1).substring(0, lastSlash + 1));
                    storeLoadCoords(commands.get(1).substring(lastSlash + 1), commands.get(1).substring(0, lastSlash + 1));
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

            case "undo":
                if (commands.size() < 1) {
                    System.out.println("Too few Arguments for undo command");
                } else if (commands.size() > 1) {
                    System.out.println("Too many Arguments for undo command");
                } else {
                    project.undo();
                }
            break;

            case "redo":
                if (commands.size() < 1) {
                    System.out.println("Too few Arguments for redo command");
                } else if (commands.size() > 1) {
                    System.out.println("Too many Arguments for redo command");
                } else {
                    project.redo();
                }
            break;

            //If command is not one of the specified commands above then we print an error message
            default:
                System.out.println("Error: " + commands.get(0) + " is not a recognized command");
            break;
        }
        return true;
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

    public static void storeLoadCoords (String fileName, String filePath) {
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();

        if (filePath != null) {
            filePath += fileName + ".json";
        } else {
            filePath = "saves/" + fileName + ".json";
        }

        // Open the file that was just loaded
        try (FileReader reader = new FileReader(filePath)) {
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

    public static void restoreLoadCoords (String fileName, String filePath) {
        // Initiate the JSON parser
        JSONParser jPar = new JSONParser();

        if (filePath != null) {
            filePath += fileName + ".json";
        } else {
            filePath = "saves/" + fileName + ".json";
        }
        
        // Attempt to read the filename in the "saves" directory specified by 
        // the user or catch resulting exceptions if/when that fails
        try (FileReader reader = new FileReader(filePath)) {
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
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(classList.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}