FROM jboss/wildfly:17.0.1.Final

LABEL maintainer="bzb"

ARG DB_HOST
ARG DB_PORT
ARG DB_SCHEMA
ARG DB_USER
ARG DB_PASS
ARG WAR_FILE

# Adding a wait-for script for the MariaDB container
USER root
ENV WAIT_VERSION 2.5.1
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /opt/wait
RUN chmod +x /opt/wait
RUN chown jboss:root /opt/wait

USER jboss

ADD wildfly-configuration/script /opt/jboss/wildfly/customization/
ADD wildfly-configuration/module  /opt/jboss/wildfly/modules/com/mysql/jdbc/main/
RUN [ "/opt/jboss/wildfly/customization/configure.sh", "standalone", "standalone-full.xml" ]
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current

ADD ${WAR_FILE} /opt/jboss/wildfly/standalone/deployments/
CMD /bin/bash -c "/opt/wait && /opt/jboss/wildfly/bin/standalone.sh -c standalone-full.xml -b 0.0.0.0 -bmanagement 0.0.0.0"
