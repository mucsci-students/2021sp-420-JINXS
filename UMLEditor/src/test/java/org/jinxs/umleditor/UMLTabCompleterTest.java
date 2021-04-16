package org.jinxs.umleditor;

import org.junit.Test;

public class UMLTabCompleterTest{

    @Test
    public void updateTest(){
        UMLTabCompleter comp = new UMLTabCompleter();
        UMLEditor project = new UMLEditor();

        project.addClass("cladd");
        project.addClass("pladd");
        project.addClass("bradd");



        comp.update(project);

    }
}