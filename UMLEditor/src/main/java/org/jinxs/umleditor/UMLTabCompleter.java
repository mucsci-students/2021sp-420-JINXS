package org.jinxs.umleditor;

import java.util.ArrayList;
import java.util.Collection;

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
            )
        );
    }

    public AggregateCompleter update(UMLEditor editor){
        Collection<Completer> comps = comp.getCompleters();
        comps = new ArrayList<>(comps);

        ArrayList<String> classNames = new ArrayList();
        ArrayList<UMLClass> classes = editor.getClasses();

        for(int i = 0; i < classes.size(); ++i){
            UMLClass cClass = classes.get(i);
            ArrayList<UMLMethod> methods = cClass.getMethods();
            String cName = cClass.name;
            classNames.add(cName);

            comps.add(
                new ArgumentCompleter(
                    new StringsCompleter("add"),
                    new StringsCompleter("field", "method"),
                    new StringsCompleter(cName),
                    new NullCompleter()
                )
            );

            comps.add(
                new ArgumentCompleter(
                    new StringsCompleter("delete", "rename"),
                    new StringsCompleter("class"),
                    new StringsCompleter(cName),
                    new NullCompleter()
                )
            );

            for(int f = 0; f < cClass.getFields().size(); ++f){
                comps.add(
                    new ArgumentCompleter(
                        new StringsCompleter("delete", "rename", "retype"),
                        new StringsCompleter("field"),
                        new StringsCompleter(cName),
                        new StringsCompleter(cClass.getFields().get(f).name),
                        new NullCompleter()
                    )
                );
               
            }

            for(int m = 0; m < methods.size(); ++m){
                comps.add(
                    new ArgumentCompleter(
                        new StringsCompleter("delete", "rename", "retype"),
                        new StringsCompleter("method"),
                        new StringsCompleter(cName),
                        new StringsCompleter(methods.get(m).name),
                        new NullCompleter()
                    )
                );

                for(int p = 0; p < methods.get(m).params.size(); ++p){
                    comps.add(
                        new ArgumentCompleter(
                            new StringsCompleter("delete", "rename", "retype"),
                            new StringsCompleter("param"),
                            new StringsCompleter(cName),
                            new StringsCompleter(methods.get(m).name),
                            new StringsCompleter(methods.get(m).params.get(p).name),
                            new NullCompleter()
                        )
                    );
                }
            }

            for(int r = 0; r < cClass.getRels().size(); ++r){
                comps.add(
                    new ArgumentCompleter(
                        new StringsCompleter("delete", "rename"),
                        new StringsCompleter("rel"),
                        new StringsCompleter(cName),
                        new StringsCompleter(classNames),
                        new NullCompleter()
                    )
                );

            }

            comps.add(
                new ArgumentCompleter(
                    new StringsCompleter("printContents", "printRel"),
                    new StringsCompleter(cName),
                    new NullCompleter()
                )
            );

            
            comps.add(
                new ArgumentCompleter(
                    new StringsCompleter("add", "retype"),
                    new StringsCompleter("rel"),
                    new StringsCompleter(classNames),
                    new StringsCompleter(classNames),
                    new StringsCompleter("inheritance", "realization", "aggregation", "composition"),
                    new NullCompleter()
                )
            );
        }
        
        comps.add(
            new ArgumentCompleter(
                new StringsCompleter("add"),
                new StringsCompleter("class", "field", "method", "param"),
                new NullCompleter()
            )
        );

        return new AggregateCompleter(comps);
    }
}