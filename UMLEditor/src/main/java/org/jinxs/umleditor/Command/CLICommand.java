package org.jinxs.umleditor.Command;

import java.util.ArrayList;

import org.jinxs.umleditor.UMLEditor;

// Abstract base command that each CLI command instance will implement
public abstract class CLICommand {
    public UMLEditor project;
    public String UMLObject;
    public ArrayList<String> args;

    CLICommand(UMLEditor editor, String object, ArrayList<String> commands) {
        this.project = editor;
        this.UMLObject = object;
        this.args = commands;
    }
    
    // Each command will override to perform its respective command from the controller
    // Returns true if the command changes the project state, false if not
    public abstract boolean execute();
}
