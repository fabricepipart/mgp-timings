####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile.jvm -t quarkus/getting-started-jvm .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/getting-started-jvm
#
###
#FROM fabric8/java-alpine-openjdk8-jre

FROM fabric8/java-centos-openjdk11-jdk
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV AB_ENABLED=jmx_exporter
ENV QUARKUS_PROFILE=prod
COPY target/*-runner.jar /deployments/app.jar
EXPOSE 8080
ENTRYPOINT [ "/deployments/run-java.sh" ] 
#ENTRYPOINT [ "java", "-Dquarkus-profile=prod", "-Dquarkus.http.host=0.0.0.0", "-jar", "/deployments/app.jar" ]