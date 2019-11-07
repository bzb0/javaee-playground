# JavaEE Playground

### Description

A test field/playground project for testing out various Java EE technologies (e.g. CDI, EJB, JMS, JAX-RS, JPA etc.). The project is written in Java 8
and uses the Java EE 7 framework. The project tech stack is:

```
  Java SE           8
  Java EE           7
  Maven             3.5.0
  Lombok            1.16.18
  WildFly           17.0.1.Final
  Docker            19.03.0
  Docker-Compose    1.24.1
```

### Building the project

The following command compiles the project and generates the WAR file `javaee-playground.war` in the directory `./target`:

```
./mvnw clean install
```

The JPA integration tests can be executed with the following command:

```
./mvnw clean verify -Pfailsafe
```

Since we are generating a WAR file we need a target platform (application server), that will run/host our web application. This project uses WildFly
as application server. We use the WildFly Docker image (more infos: https://hub.docker.com/r/jboss/wildfly) as a base for our Docker image. Since we
are using various Java EE technologies (e.g. JMS, JPA etc.) we customize the WildFly Docker image with our own Docker image. For the WildFly
configuration a shell script from the Repo [wildfly-docker-configuration](https://github.com/goldmann/wildfly-docker-configuration) was borrowed.
<br/>
As the Java EE application uses JPA & Hibernate to store data in a relational database, we additionally use MariaDB Docker image (more
infos: https://hub.docker.com/_/mariadb). Since we have multi-container Docker setup we use Docker Compose to parametrize and start the application
and database Docker containers. The ``docker-compose.yml`` file contains the Docker Compose configuration.

We are not using a Maven Docker Compose plugin as the available plugins either didn't work properly, or they didn't fulfill our needs. The following
command will build our own customized WildFly Docker image:

```
docker-compose build
```

The previous command will generate the following Docker image:

```
bzb0/javaee-playground:1.0-SNAPSHOT
```

With ``docker-compose`` we start the customized WildFly Docker image and the MariaDB relational database. The file ``.env.local`` contains the
necessary environment variables for the MariaDB Docker container. Finally, we can start the application and database container in detached mode with
the following command:

```
docker-compose --env-file .env.local up -d
```

The web application will be available at port **8080** under the context root **/javaee-playground**. The following endpoints will be available:

* POST /executor/task (CDI, Container executor service)
* POST /executor/submission (CDI, Container executor service)
* GET /executor/power (CDI Producers, Nashorn JavaScript engine)
* GET /executor/range (CDI Producers, Qualifiers)
* GET /random/integers (EJB, Asynchronous execution)
* GET /store?key (EJB, Container managed concurrency)
* POST /store (EJB, Container managed concurrency)
* POST /topic/textMessages (Message driven beans, JMS topic)
* GET /queue/textMessages (Message driven beans, JMS queue)
* POST /queue/messages (JMS queue)
* GET /queue/messages (JMS queue)
* GET /queue/unconsumedMessages (JMS queue browser)
* POST /books (JPA, Hibernate)
* GET /books (JPA, Hibernate)
* GET /books/{id} (JPA, Hibernate)

In order to stop the running Docker containers (application & database) we have to execute the following command:

```
docker-compose down
```
