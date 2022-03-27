[![codecov](https://codecov.io/gh/fabricepipart/mgp-timings/branch/main/graph/badge.svg)](https://codecov.io/gh/fabricepipart/mgp-timings)
[![GitHub actions workflow](https://github.com/fabricepipart/mgp-timings/actions/workflows/workflow.yml/badge.svg)](https://github.com/fabricepipart/mgp-timing/blob/main/.github/workflows/workflow.yml)

# mgp-timings

## Description

### Purpose of the project

The goal of this project is to give access to all laptimes that are registered and publicly available for Motorcycles
Grand Prix sessions.

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
    * A docker image is built and pushed to dockerhub
    * Code coverage for sever side code is gathered and sent to codecov
    * A prod environment is updated on a private Kubernetes cluster
* Releasing
    * Version is bumped automatically. Add `#major` or `#patch` in your commit tags if you want to bump the
      corresponding number. By default, the minor is bumped.
    * Repository is tagged for each version
    * All changes are gathered in a ChangeLog and GH releases are used
    * Builds of the `main` branch are directly loaded on the Production environment

## How to run

### How to run locally with dev profile

```mvn quarkus:dev```

Note: you can connect your IDE for debugging on port 5005.

## How to test

### JUnit

```mvn clean test```

### QuarkusTest

```mvn clean verify```

## Services

You can review all available endpoints thanks to the Open API definition that can be
found [here](https://mgp-timings.teknichrono.fr/swagger?format=json)

The Swagger UI is available even on production for now [here](https://mgp-timings.teknichrono.fr/swagger-ui)

