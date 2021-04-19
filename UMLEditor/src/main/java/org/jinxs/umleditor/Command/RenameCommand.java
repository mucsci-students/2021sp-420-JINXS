package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class RenameCommand extends CLICommand{
    
    public RenameCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if (UMLObject.equals("class")) {
            if (args.size() < 3) {
                System.out.println("Too few Arguments for renameClass command");
                return false;
            }
            else if (args.size() > 3) {
                System.out.println("Too many Arguments for renameClass command");
                return false;
            }
            else{
                project.saveToMeme(true);
                if (!project.renameClass(args.get(1),args.get(2))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("field")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for renamefield command");
                return false;
            }
            else if (args.size() > 4) {
                System.out.println("Too many Arguments for renamefield command");
                return false;
            }
            else{
                project.saveToMeme(true);
                if (!project.renameAttr(args.get(1), args.get(2), args.get(3), args.get(0))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("method")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for renameMethod command");
                return false;
            }
            else if (args.size() > 4) {
                System.out.println("Too many Arguments for renameMethod command");
                return false;
            }
            project.saveToMeme(true);
            if (!project.renameAttr(args.get(1), args.get(2),args.get(3),args.get(0))) {
                project.removeLastSave();
                return false;
            }
            return true;
        } else if (UMLObject.equals("param")) {
            if (args.size() < 5) {
                System.out.println("Too few Arguments for renameParam command");
                return false;
            }
            project.saveToMeme(true);
            if (!project.changeParamName(args.get(1), args.get(2), args.get(3), args.get(4))) {
                project.removeLastSave();
                return false;
            }
            return true;
        } else if (UMLObject.equals("allParams")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for renameAllParams command");
                return false;
            }
            ArrayList<String> params = new ArrayList<String>();
            ArrayList<String> pTypes = new ArrayList<String>();
            for(int i = 3; i < args.size(); i += 2){
                params.add(args.get(i + 1));
                pTypes.add(args.get(i));
            }
            project.saveToMeme(true);
            if (!project.changeAllParams(args.get(1), args.get(2), params, pTypes)) {
                project.removeLastSave();
                return false;
            }
            return true;
        } else {
            System.out.println("Rename + " + args.get(0) + " is not a valid command");
            return false;
        }
    }
}
