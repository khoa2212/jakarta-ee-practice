FROM quay.io/wildfly/wildfly:26.1.3.Final-jdk17
COPY target/jakartaee-hello-world.war /opt/jboss/wildfly/standalone/deployments/
