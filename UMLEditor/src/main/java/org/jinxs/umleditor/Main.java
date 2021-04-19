package org.jinxs.umleditor;

public class Main{
    public static void main(String[] args) {
        
        if (args.length == 0) {
            new UMLGUI();
        }
        else if (args.length > 0) {
            if (args.length == 1 && args[0].equals("--cli")) {
                new UMLTerminal().build();
            }
            else {
                System.out.print("Unrecognized argument(s): \"");
                for (int i = 0; i < args.length; ++i) {
                    System.out.print(args[i]);
                    if (i + 1 < args.length) {
                        System.out.print(" ");
                    }
                }
                System.out.println("\"");
            }
        }
    }
}
