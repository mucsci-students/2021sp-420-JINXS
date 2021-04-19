package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class PrintContentsCommand extends CLICommand{
    
    public PrintContentsCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if(args.size() < 1){
            System.out.println("Too few Arguments for printClassContents command");
            return false;
        }
        else if(args.size() > 1){
            System.out.println("Too many Arguments for printClassContents command");
            return false;
        }
        else{
            project.printClassContents(args.get(0)); 
            return true;
        }
    }
}