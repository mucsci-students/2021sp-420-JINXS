package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class PrintRelCommand extends CLICommand{
    
    public PrintRelCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if(args.size() < 1){
            System.out.println("Too few Arguments for printRel command");
            return false;
        }
        else if(args.size() > 1){
            System.out.println("Too many Arguments for printRel command");
            return false;
        }
        else{
            project.printRel(args.get(0)); 
            return true;
        }
    }
}
