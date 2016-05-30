mvn install -N
mvn clean compile 
mvn javadoc:jar source:jar package deploy -DskipTests=true
