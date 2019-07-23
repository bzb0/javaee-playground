#!/bin/bash

# This script was borrowed from https://github.com/goldmann/wildfly-docker-configuration/blob/master/cli/customization/execute.sh

# Usage: execute.sh [WildFly mode] [configuration file]
#
# The default mode is 'standalone' and default configuration is based on the
# mode. It can be 'standalone.xml' or 'domain.xml'.

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

function wait_for_server() {
  until `$JBOSS_CLI -c "ls /deployment" &> /dev/null`; do
    sleep 1
  done
}

echo "=> Starting WildFly server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -c $JBOSS_CONFIG > /dev/null &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> Executing the commands"

$JBOSS_CLI --connect <<EOF
batch
/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql.jdbc,driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)
data-source add --name=MariaDBDS --driver-name=mysql --jndi-name=java:jboss/datasources/MariaDBDS --connection-url=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_SCHEMA?useUnicode=true&amp;characterEncoding=UTF-8 --user-name=$DB_USER --password=$DB_PASS --max-pool-size=25 --blocking-timeout-wait-millis=5000 --enabled=true

jms-queue --profile=full add --queue-address=textQueue --entries=["queue/textQueue"]
jms-queue --profile=full add --queue-address=mdbTextQueue --entries=["queue/mdbTextQueue"]
jms-topic --profile=full add --topic-address=mdbTextTopic --entries=["topic/mdbTextTopic"]
run-batch
exit
EOF

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi
