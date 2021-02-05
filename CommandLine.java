import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.util.Scanner;

import UMLEditor.java; 

public class CommandLine{

    //fields that we will use
    static boolean helpfile = true; // asks if the helpfile is present
    static UMLEditor project; // makes an object for the UMLEditor which contains our functions

    public static void CommandLine(){
        Scanner UserInput = new Scanner(System.in);
        While(true){
            System.out.Print("$") //Repersents the start of our terminal input, maybe can change to ">" instead
            String command = UserInput.nextLine();
            ArrayList<String> commands = parseLine(command);
            if (commands == null){
                continue; 
            }
            Switch(commands.get(0)){

                //simplist case, if user types in quit, the close Scanner because we won't read anymore input
                case "quit":
                    UserInput.close;
                    return;
                
                case "help":
                    try{
                    File helpFile = new File("helpDocument.txt");
                    Scanner helpReader = new Scanner(helpFile);
                    while(helpReader.hasNextLine()){
                        String output = helpReader.nextLine();
                        System.out.println(data);
                    }  
                    helpReader.close;

                    } catch(FileNotFoundException e){
                        System.out.println("Helpfile was not found.");
                        e.printStackTrace();
                    }
                case "addClass":
                    if(commands.size() < 2){
                        System.out.println("Too few Arguments for addClass command");
                    }
                    else if(commands.size() > 2){
                        System.out.println("Too many Arguments for addClass command");
                    }
                    else{
                        project.addClass(commands.get(1)); 
                    }
                case "deleteClass":
                    if(commands.size() < 2){
                    System.out.println("Too few Arguments for deleteClass command");
                    }
                    else if(commands.size() > 2){
                    System.out.println("Too many Arguments for deleteClass command");
                    }
                     else{
                    project.deleteClass(commands.get(1)); 
                    }

                case "renameClass":

                case "AddRelationship":

                case "deleteRelationship":

                case "addAttribute":

                case "deleteAttribute":

                case "renameAttribute":

                case "save":

                case "load":

    //Todo: Read input from user
    Public static ArrayList<String> parseLine(String command){

    }          
            }
        }
    }
    

}
