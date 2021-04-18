package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class AddCommand extends CLICommand{
    
    public AddCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if (UMLObject.equals("class")) {
            if (args.size() < 2) {
                System.out.println("Too few Arguments for addClass command");
                return false;
            }
            else if (args.size() > 2) {
                System.out.println("Too many Arguments for addClass command");
                return false;
            }
            else{
                project.saveToMeme(true);
                if (!project.addClass(args.get(1))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("rel")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for addRel command");
                return false;
            }
            else if (args.size() > 4) {
                System.out.println("Too many Arguments for addRel command");
                return false;
            }
            else{
                project.saveToMeme(true);
                project.addRel(args.get(1), args.get(2), args.get(3));
                return true;
            }
        } else if (UMLObject.equals("field")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for addfield command");
                return false;
            }
            else if (args.size() > 4) {
                System.out.println("Too many Arguments for addfield command");
                return false;
            }
            else{
                project.saveToMeme(true);
                if (!project.addAttr(args.get(1), args.get(3), args.get(0), args.get(2))) {
                    project.removeLastSave();
                    return false;
                }
                return true;
            }
        } else if (UMLObject.equals("method")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for addMethod command");
                return false;
            }
            else{
                project.saveToMeme(true);
                if (!project.addAttr(args.get(1), args.get(3),args.get(0), args.get(2))) {
                    project.removeLastSave();
                    return false;
                }
                else {
                    for(int i = 4; i < args.size(); i += 2){
                        project.addParam(args.get(1), args.get(3), args.get(i + 1), args.get(i));
                    }
                    return true;
                }
            }
        } else if (UMLObject.equals("param")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for addParam command");
                return false;
            }
            else{
                project.saveToMeme(true);
                for(int i = 3; i < args.size(); i += 2){
                    project.addParam(args.get(1), args.get(2), args.get(i + 1), args.get(i)); 
                }
                return true;
            }
        } else {
            System.out.println("Add + " + args.get(0) + " is not a valid command");
            return false;
        }
    }
}
