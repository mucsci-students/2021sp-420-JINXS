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

    public static void CommandInterface(UMLEditor project) {
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

                case "addRelationship":
                    break;

                case "deleteRelationship":
                    break;

                case "addAttribute":
                    break;

                case "deleteAttribute":
                    break;

                case "renameAttribute":
                    break;

                case "save":
                    break;

                case "load":
                    break;

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
        CommandInterface(project);
        System.exit(0);
    }
}
