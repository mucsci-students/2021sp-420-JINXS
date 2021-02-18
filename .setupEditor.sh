#!/bin/bash

function setup() {

    cd UMLEditor
    ./mvnw -N io.takari:maven:wrapper
    ./mvnw package -f pom.xml
    cd ..

}

function editor() {

    java -jar UMLEditor/target/UMLEditor-1.0.jar

}