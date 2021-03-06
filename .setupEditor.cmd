:setup
cd UMLEditor
mvn -U clean install -Dmaven.test.skip=true
cd ..
EXIT /B 0

:editor
java -jar UMLEditor/target/UMLEditor-1.0.jar %*
EXIT /B 0