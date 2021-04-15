package org.jinxs.umleditor;

import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

public class UMLTabCompleter{

    private AggregateCompleter comp;

    public UMLTabCompleter(){
        this.comp = new AggregateCompleter(
            new ArgumentCompleter(
                new StringsCompleter("add"),
                new StringsCompleter("class", "field", "method", "param"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("add"),
                new StringsCompleter("rel"),
                new StringsCompleter("inheritance", "realization", "aggregation", "composition"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("delete"),
                new StringsCompleter("class", "field", "method", "param", "allParams"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("delete"),
                new StringsCompleter("rel"),
                new StringsCompleter("inheritance", "realization", "aggregation", "composition"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("rename"),
                new StringsCompleter("class", "field", "method", "param", "allParams"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("change"),
                new StringsCompleter("fieldType", "methodType", "paramType", "relType"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("save"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("load"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("undo"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("redo"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("help"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("quit"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("printList"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("printContents"),
                new NullCompleter()
            ),
            new ArgumentCompleter(
                new StringsCompleter("printRel"),
                new NullCompleter()
            )
        );
    }

    public AggregateCompleter update(UMLEditor editor){
        return comp;
    }
}