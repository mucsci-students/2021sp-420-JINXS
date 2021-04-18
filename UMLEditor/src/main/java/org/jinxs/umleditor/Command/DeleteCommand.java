package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

public class DeleteCommand extends CLICommand{
    
    public DeleteCommand(UMLEditor editor, String object, ArrayList<String> args) {
        super(editor, object, args);
    }

    @Override
    public boolean execute() {
        if (UMLObject.equals("class")) {
            if (args.size() < 2) {
                System.out.println("Too few Arguments for deleteClass command");
                return false;
            }
            else if (args.size() > 2) {
                System.out.println("Too many Arguments for deleteClass command");
                return false;
            }
            else{
                project.saveToMeme(true);
                project.deleteClass(args.get(1));
                return true;
            }
        } else if (UMLObject.equals("rel")) {
            if (args.size() < 3) {
                System.out.println("Too few Arguments for deleteRel command");
                return false;
            }
            else if (args.size() > 3) {
                System.out.println("Too many Arguments for deleteRel command");
                return false;
            }
            else{
                project.saveToMeme(true);
                project.delRel(args.get(1), args.get(2));
                return true;
            }
        } else if (UMLObject.equals("field")) {
            if (args.size() < 3) {
                System.out.println("Too few Arguments for deletefield command");
                return false;
            }
            else if (args.size() > 3) {
                System.out.println("Too many Arguments for deletefield command");
                return false;
            }
            else{
                project.saveToMeme(true);
                project.delAttr(args.get(1), args.get(2), args.get(0));
                return true;
            }
        } else if (UMLObject.equals("method")) {
            if (args.size() < 3) {
                System.out.println("Too few Arguments for deleteMethod command");
                return false;
            }
            project.saveToMeme(true);
            project.delAttr(args.get(1), args.get(2),args.get(0));
            return true;
        } else if (UMLObject.equals("param")) {
            if (args.size() < 4) {
                System.out.println("Too few Arguments for addParam command");
                return false;
            }
            project.saveToMeme(true);
            project.deleteParam(args.get(1), args.get(2), args.get(3));
            return true;
        } else if (UMLObject.equals("allParams")) {
            if (args.size() < 3) {
                System.out.println("Too few Arguments for deleteAllParams command");
                return false;
            }
            project.saveToMeme(true);
            project.deleteAllParams(args.get(1), args.get(2));
            return true;
        } else {
            System.out.println("Delete + " + args.get(0) + " is not a valid command");
            return false;
        }
    }
}
