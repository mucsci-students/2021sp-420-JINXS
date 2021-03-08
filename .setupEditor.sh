#!/bin/bash

function setup() {

    cd UMLEditor
    ./mvnw -N io.takari:maven:wrapper
    ./mvnw -U clean install -Dmaven.test.skip=true
    cd ..

}

function editor() {

    java -jar UMLEditor/target/UMLEditor-1.0.jar $@

}