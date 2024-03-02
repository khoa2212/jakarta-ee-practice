<h1 align="center">🏛️ Department management</h1>
<h3 align="center">Java EE 8.0.0, wildfly 26.1.3, postgres SQL 14</h3>

# ⚙️ Set up to run project
- Add path bin folder of wildfly to environment variable
- Add path to folder of java, maven, wildfly in system variable as JAVA_HOME, MAVEN_HOME, JBOSS_HOME

# 🔨 Set up database
- Create a database postgres
- Create a file flyway.properties in folder /src/resources/config/local as this
  - flyway.url= 
  - flyway.user= 
  - flyway.password=
``` shell
mvn flyway:migrate -Plocal -X
```
# ⚒️ Build project

``` shell
mvn clean package -DskipTests
```

# ✈️ Deploy project to wildfly

``` shell
mvn deploy -DskipTests
```

# <img width="25" src="https://avatars.githubusercontent.com/u/3066274?s=48&v=4"/> Run wildfly
``` shell
standalone.bat
```

# 🐳 Run docker locally
``` shell
docker compose up
```
