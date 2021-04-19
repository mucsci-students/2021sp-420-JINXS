package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class RetypeCommand extends CLICommand{
    
    public RetypeCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if (UMLObject.equals("rel")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for change relType command");
                return false;
            } else if (args.size() > 4) {
                System.out.println("Too many Arguments for change relType command");
                return false;
            } else {
                project.saveToMeme(true);
                project.changeRelType(args.get(1), args.get(2), args.get(3));
                return true;
            }
        } else if (UMLObject.equals("field")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for change fieldType command");
                return false;
            } else if (args.size() > 4) {
                System.out.println("Too many Arguments for change fieldType command");
                return false;
            } else {
                project.saveToMeme(true);
                if (!project.changeFieldType(args.get(1), args.get(2), args.get(3))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("method")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for change methodType command");
                return false;
            } else if (args.size() > 4) {
                System.out.println("Too many Arguments for change methodType command");
                return false;
            } else {
                project.saveToMeme(true);
                if (!project.changeMethodType(args.get(1), args.get(2), args.get(3))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("param")) {
            if (args.size() < 5) {
                System.out.println("Too few Arguments for change paramType command");
                return false;
            } else if (args.size() > 5) {
                System.out.println("Too many Arguments for change paramType command");
                return false;
            } else {
                project.saveToMeme(true);
                if (!project.changeParamType(args.get(1), args.get(2), args.get(3), args.get(4))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else {
            System.out.println("Retype + " + args.get(0) + " is not a valid command");
            return false;
        }
    } 
}
