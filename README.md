# mgp-timings

## Description

### Purpose of the project



### Architecture


### Requirements

* Java 11
* Maven 3.6

## How to build

### How to build locally

```mvn clean verify```

### CI Build

The CI is done thanks to GitHub actions and runs the following:
* Server tests
  * JUnits
  * Quarkus Integration tests
  * A docker image is built with the Python Integration Tests integrated and pushed to dockerhub along with the server itself
  * Code coverage for sever side code is gathered
  * A staging environment is started on a private Kubernetes cluster
  * Python integration tests are ran against this staging environment
* Client tests
  * When the staging environment is loaded, the client is started in demo mode which simulates random Bluetooth reception
  * Auto update of the client is tested
  * Test coverage is gathered and sent to codecov
* Releasing
  * Version is bumped automatically. Add `#major` or `#patch` in your commit tags if you want to bump the corresponding number. By default, the minor is bumped.
  * Repository is tagged for each version
  * All changes are gathered in a ChangeLog and GH releases are used
  * Builds of the `master` branch are directly loaded on the Production environment

## How to run

There are three profiles. See in ```src/main/resources/application.properties```.

### How to run locally with dev profile

```mvn quarkus:dev```

Note: you can connect your IDE for debugging on port 5005.

### How to run locally with localmariadb profile

First:
```kubectl port-forward -n teknichrono $(kubectl get pod -n teknichrono -l app=mysql -o jsonpath='{.items[0].metadata.name}') 3306:3306```
```kubectl port-forward -n teknichrono service/mysql-service 3306:3306```
then
```mvn quarkus:dev -Dquarkus-profile=localmariadb```
or
```java -Dquarkus-profile=localmariadb -jar target/teknichrono-runner.jar```

## How to test

### JUnit

```mvn clean test```

### QuarkusTest

```mvn clean verify```

### End to End


## Services

TODO List of the REST API services here

For the time being, you can have a look in: ```src/main/java/org/trd/app/teknichrono/rest```
