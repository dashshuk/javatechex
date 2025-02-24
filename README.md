# Java Technical Exercise

The solutions is implemented using the following tech stack

JAVA

Gradle

SpringBoot

PostgresDB

SQL


## Instructions

From command line :

simply download the JAR to your favourite folder

Navigate to that folder at the command prompt

Type java -jar <jarname>.jar and press enter (jar name is the name of the jar file sent out)

This will run the app on default port 8080 (make sure nothing else is using that port)

Then from browser or a REST client like postman enter following URL and enter

http://localhost:8080/1

Where 1 represents customer id (its parameterised so should work any customer id)

This should give you the desired results.

Make sure the DB is running in the background


From IDE

Simply export as a gradle project and one should be able to execute tests and run fom the IDE itself

## Note

SQL Query included under src/main/resources directory