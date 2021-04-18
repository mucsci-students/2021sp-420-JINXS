package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class CopyCommand extends CLICommand{
    
    public CopyCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if (args.size() < 2) {
            System.out.println("Too few Arguments for copy command");
            return false;
        } else if (args.size() > 2) {
            System.out.println("Too many Arguments for copy command");
            return false;
        } else {
            project.saveToMeme(true);
            if (!project.copyClass(args.get(0), args.get(1))) {
                project.removeLastSave();
                return false;
            }
            return true;
        }
    }
}
