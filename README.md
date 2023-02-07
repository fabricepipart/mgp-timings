[![codecov](https://codecov.io/gh/fabricepipart/mgp-timings/branch/main/graph/badge.svg)](https://codecov.io/gh/fabricepipart/mgp-timings)
[![GitHub actions workflow](https://github.com/fabricepipart/mgp-timings/actions/workflows/workflow.yml/badge.svg)](https://github.com/fabricepipart/mgp-timing/blob/main/.github/workflows/workflow.yml)

# mgp-timings

## Description of the project

The goal of this project is to give access to all laptimes that are registered and publicly available for Motorcycles
Grand Prix sessions.

Technically it is a REST API that can be target either programmatically or via a web browser. It is coded in Java and based on the [Quarkus framework](https://quarkus.io/).

---

## Using the services

### All services catalog

You can review all available endpoints thanks to the Open API definition that can be
found [here](https://mgp-timings.teknichrono.fr/swagger?format=json)

The Swagger UI is available even on production for now [here](https://mgp-timings.teknichrono.fr/swagger-ui)

### Main services

| Service | URL | Example | Description |
|---------|-----|---------|-------------|
| List all races of a given year | `/event/{year}/names`| [https://mgp-timings.teknichrono.fr/event/2022/names](https://mgp-timings.teknichrono.fr/event/2022/names) | Get all the 3 letters acronyms for each event of the year. Referenced as `eventShortName` below |
| List info about an event | `/session/{year}/{eventShortName}/{category}` | [https://mgp-timings.teknichrono.fr/session/2022/FRA/GP](https://mgp-timings.teknichrono.fr/session/2022/FRA/GP) | All info about an event (conditions, sessions, PDF files available...). Category is either `MOTO3`, `MOTO2`, `GP` |
| List all riders of a given race | `/session/{year}/{eventShortName}/{category}/riders`| [https://mgp-timings.teknichrono.fr/session/2022/FRA/MOTO3/riders](https://mgp-timings.teknichrono.fr/session/2022/FRA/MOTO3/riders) | All riders that participated to that event in that category |
| Session results summary | `/session/{year}/{eventShortName}/{category}/{session}/results/details` | [https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/FP3/results/details](https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/FP3/results/details) | Results in a simplified table format |
| Session results summary as CSV | `/session/{year}/{eventShortName}/{category}/{session}/results/details/csv` | [https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/FP3/results/details/csv](https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/FP3/results/details/csv) | Same as above but in a CSV format  |
| Details of a session | `/session/{year}/{eventShortName}/{category}/{session}/analysis` | [https://mgp-timings.teknichrono.fr/session/2022/FRA/GP/RAC/analysis]https://mgp-timings.teknichrono.fr/session/2022/FRA/GP/RAC/analysis() | All the laps done by each rider, tyres used, max speed ... (from PDF) |
| Details of a session as CSV | `/session/{year}/{eventShortName}/{category}/{session}/analysis/csv` | [https://mgp-timings.teknichrono.fr/session/2022/FRA/GP/RAC/analysis/csv](https://mgp-timings.teknichrono.fr/session/2022/FRA/GP/RAC/analysis/csv) | Same as above but in a CSV format |
| Top speeds of the session | `/session/{year}/{eventShortName}/{category}/{session}/topspeed` | [https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/Q2/topspeed](https://mgp-timings.teknichrono.fr/session/2022/QAT/GP/Q2/topspeed) | Summary of top speeds (from PDF) |

Please note that there are many more services, please consult the Swagger UI for more info.

### An error occured?

Please report the problem by filling an issue: https://github.com/fabricepipart/mgp-timings/issues

---

## How to build locally the project

### Requirements

* Java 11
* Maven 3.6

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


---

## How to run locally the project

### How to run locally with dev profile

```mvn quarkus:dev```

Note: you can connect your IDE for debugging on port 5005.

---

## How to test locally the project

### JUnit

```mvn clean test```

### QuarkusTest

```mvn clean verify```

