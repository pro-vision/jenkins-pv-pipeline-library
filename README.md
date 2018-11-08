# PV Pipeline Library

The PV pipeline library is an extension for the
[pipeline library](https://github.com/wcm-io-devops/jenkins-pipeline-library)
and adds steps to standardize the maven build process in pro!vision
projects.

:bulb: Is is absolutely recommended to read the
[wcm-io-devops Jenkins Pipeline Library documentation](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/README.md)
of the pipeline library.

# Table of contents
* [Key concepts](#key-concepts)
    * [Default maven build](#default-maven-build)
    * [Feature maven build](#feature-maven-build)
    * [routeDefaultJenkinsFile](#routedefaultjenkinsfile)
    * [Provide default tools](#provide-default-tools)
* [Requirements](#requirements)
* [Steps](#steps)
* [Tutorials](#tutorials)
* [Documentation](#documentation)
* [Building/Testing](#buildingtesting)
    * [Building with maven](#building-with-maven)

## Key concepts

The main concepts of the pv pipeline library are

* **DRY**
* **Maintainability**
* **Configurability**

It reduces the code a developer has to write to get the project build by
Jenkins. It consolidates the characteristics of our projects but also
provides a high level of configurability.

### Default maven build

The requirements of our maven based project have a large congruence.
A normal maven project has the following steps:

* Set job properties (like log rotation, no concurrent builds)
* Checkout code from SCM using GIT
* Remove existing project and dependency snapshots from local maven
  repository (e.g. from io.wcm)
* Build the maven project and deploy the artifacts to nexus
* Analyse the maven project (e.g. pmd, checkstyle, opentasks, findbugs)
* Publish reports (e.g. JUnit, pmd, checkstyle, opentasks, spotbugs etc.)
* Notify the teams when a job status changes (e.g. failure, unstable,
  fixed)

To avoid that each project has redundant pipeline code doing these steps
this library introduces the [buildDefault](vars/buildDefault.md)

### Feature maven build

Building a feature branch is the same as the default maven build with
two exceptions:

* The branch is merged with master before build
* The artifacts are not deployed to nexus (so no `mvn clean deploy`)

The [buildFeature](vars/buildFeature.groovy) step automatically takes
care of this.

### routeDefaultJenkinsFile

The [routeDefaultJenkinsFile](vars/routeDefaultJenkinsFile.md) the main part for **DRY**. This step automatically detects if the
current pipeline build is a "normal" pipeline build or a multi branch
pipeline build.

With this step it is possible to place one `Jenkinsfile` in you project
root which looks like this:

```groovy
@Library('pipeline-library') pipelineLibrary
@Library('pv-pipeline-library') pvPipelineLibrary

routeDefaultJenkinsFile()
```

### Configurability

Even when the pv pipeline library streamlines the jenkins build it
provides a large amount of configuration options to ensure that project
specific needs are covered.

:bulb: Have a look at [Configuration](docs/config-structure.md) for more
information.

:bulb: It is also recommended to read the
[config structure](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/config-structure.md)
documentation from pipeline library.

### Provide default tools

The big part of our projects is using JDK and Apache Maven 3. So the
initialization of these tools was moved to the step
[setupPVTools](vars/setupPVTools.md) to make this easier.

## Requirements

In order to use the pv pipeline library you have to fulfil these
requirements:

* jenkins-pv-pipeline-library [requirements](docs/requirements.md)
* jenkins-pipeline-library [requirements](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/requirements.md)

## Steps

The PV pipeline library provides the following steps:

* [`buildDefault`](vars/buildDefault.md)
* [`buildFeature`](vars/buildFeature.md)
* [`defaultAnalyzeStage`](vars/defaultAnalyzeStage.md)
* [`defaultBuildWrapper`](vars/defaultBuildWrapper.md)
* [`defaultCompileStage`](vars/defaultCompileStage.md)
* [`defaultPreparationStage`](vars/defaultPreparationStage.md)
* [`defaultResultsStage`](vars/defaultResultsStage.md)
* [`featurePreparationStage`](vars/featurePreparationStage.md)
* [`getDefaultMavenDefines`](vars/getDefaultMavenDefines.md)
* [`routeDefaultJenkinsFile`](vars/routeDefaultJenkinsFile.md)
* [`setDefaultJobProperties`](vars/setJobProperties.md)
* [`setupPVTools`](vars/setupPVTools.md)

## Utilities

The PV pipeline library provides the following utilities:

* [`defaults`](vars/defaults.md)

## Tutorials

located here: [docs/tutorials](docs/tutorials).

The most important tutorials:
* [Tutorial: Setup a project folder](docs/tutorials/setup-project-folder/README.md)
* [Tutorial: Setup a project](docs/tutorials/setup-project/README.md) for
* [Tutorial: Setup a feature branch build job](docs/tutorials/setup-feature-branch/README.md)

### Documentation

General documentation is in the [docs](docs) folder.

Please have a look at the [Configuration](docs/config-structure.md)
documentation for config options and configuration examples.

## Building/Testing

The library uses two approaches for testing.

The class parts are tested by unit testing using JUnit/Surefire. All
unit tests have the naming format `*Test.groovy` and are located below
`test/io`.

The step parts are tested by using
[Jenkins Pipeline Unit](https://github.com/lesfurets/JenkinsPipelineUnit)
with jUnit/Failsafe. All integration tests have the naming format
`*IT.groovy` and are located below `test/vars`.

### Building with maven

    mvn clean install
