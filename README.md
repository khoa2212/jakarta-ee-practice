# Set up to run project
- Add path bin folder of wildfly to environment variable
- Add path to folder of java, maven, wildfly in system variable as JAVA_HOME, MAVEN_HOME, JBOSS_HOME

# Set up database
- Create a database postgres
- Create a file flyway.properties in folder /src/resources/config/local as this
  - flyway.url= 
  - flyway.user= 
  - flyway.password=
```
mvn flyway:migrate -Plocal -X
```
# Build project

```
mvn clean package -DskipTests
```

# Deploy project

```
mvn deploy -DskipTests
```

# Run wildfly
```
standalone.bat
```
