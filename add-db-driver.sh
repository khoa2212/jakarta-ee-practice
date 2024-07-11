#!/bin/bash

echo "=> Starting WildFly server"
$JBOSS_HOME/bin/standalone.sh &

echo "=> Waiting for the server to boot"
until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
  echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`
  sleep 1
done

echo "=> Adding PostgreSQL module"
$JBOSS_CLI --connect --command="module add --name=org.postgres --resources=/opt/jboss/wildfly/standalone/deployments/${POSTGRES_DRIVER}.jar --dependencies=javax.api,javax.transaction.api"

echo "=> Adding PostgreSQL driver"
$JBOSS_CLI --connect --command="/subsystem=datasources/jdbc-driver=postgres:add(driver-name=postgres,driver-module-name=org.postgres,driver-class-name=org.postgresql.Driver)"

echo "=> Creating a new datasource"
$JBOSS_CLI --connect --command="data-source add \
  --name=${DATASOURCE_NAME}DS \
  --jndi-name=java:/${DATASOURCE_NAME}DS \
  --user-name=${DB_USER} \
  --password=${DB_PASS} \
  --driver-name=postgres \
  --connection-url=jdbc:postgresql://${DB_URI}/${DB_NAME} \
  --enabled=true"

echo "=> Shutting down WildFly and Cleaning up"
$JBOSS_CLI --connect --command=":shutdown"
rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/*
rm -f /tmp/*.jar

echo "Start wildfly"

$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

echo "HELLO"
