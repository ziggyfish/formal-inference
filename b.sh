bash mvnw clean package
cd JMLProcessor/target
mkdir JMLProcessor2
mv JMLProcessor-1.0-SNAPSHOT-jar-with-dependencies.jar JMLProcessor2
cd JMLProcessor2
jar xf JMLProcessor-1.0-SNAPSHOT-jar-with-dependencies.jar
ls .
