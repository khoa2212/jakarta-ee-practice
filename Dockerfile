# # # # # # # # # #
# Build WAR file  #
# # # # # # # # # #
FROM maven:3.9.6-eclipse-temurin-17-alpine AS buildserver

# Maven env
ENV PROJECT_DIR /usr/agilecourse/api

# Create and copy project into maven mount
WORKDIR $PROJECT_DIR
COPY . $PROJECT_DIR

# Package the project
RUN mvn -f $PROJECT_DIR/pom.xml clean package -DskipTests

# # # # # # # # # #
# Config Wildfly  #
# # # # # # # # # #
FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk17 as deployer

# Appserver
ENV WILDFLY_USER admin
ENV WILDFLY_PASS Aavn@123
ENV DATASOURCE_NAME Postgres

# Database
ENV DB_NAME postgres
ENV DB_USER vamos
ENV DB_PASS Vamos123!@#
ENV DB_URI db:5432

ENV POSTGRES_DRIVER postgresql-42.7.1

# Misc
ENV JBOSS_CLI /opt/jboss/wildfly/bin/jboss-cli.sh
ENV DEPLOYMENT_DIR /opt/jboss/wildfly/standalone/deployments/

# Copying files to mount
COPY src/main/resources/$POSTGRES_DRIVER.jar $DEPLOYMENT_DIR
# COPY target/*.war $DEPLOYMENT_DIR

# Setting up WildFly Admin Console
RUN echo "=> Adding WildFly administrator"
RUN /opt/jboss/wildfly/bin/add-user.sh $WILDFLY_USER $WILDFLY_PASS --silent

COPY --from=buildserver /usr/agilecourse/api/target/*.war $DEPLOYMENT_DIR
# Exposing HTTP and Admin ports
EXPOSE 8080 9990

# Run script to add postgres driver to create datasource, then start wildfly
COPY add-db-driver.sh /opt/scripts/
USER root
RUN chmod +x /opt/scripts/add-db-driver.sh
CMD ["/bin/bash", "/opt/scripts/add-db-driver.sh"]