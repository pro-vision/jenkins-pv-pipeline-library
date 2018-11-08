# routeDefaultJenkinsFile

This is the main step that should be used inside `Jenkinsfile` that are
stored in the project.

This step checks the properties of the current run and decides if the
current run is simple pipeline run or a multibranch pipeline run.

When a project has no specialities, e.g. is running with
* Apache Maven 3
* JDK 8

this would be the content of the Jenkinsfile stored at project root:

```groovy
@Library('pipeline-library') pipelineLibrary
@Library('pv-pipeline-library') pvPipelineLibrary

routeDefaultJenkinsFile()
```

With that construct
* `develop` and `master` pipeline jobs will build with deploying the
  artifacts to nexus ([`buildDefault`](buildDefault.md)), and
* `feature/*` multibranch pipeline jobs will build without deploying the
  artifacts to nexus ([`buildFeature`](buildFeature.md))

# Table of contents
* [Process](#process)
    * [Normal pipeline build](#normal-pipeline-build)
    * [Multi branch pipeline build](#multi-branch-pipeline-build)
* [Configuration options](#configuration-options)

## Process

![routeDefaultJenkinsFile](../docs/assets/route-default-jenkins-file.png)

The `routeDefaultJenkinsFile` step checks if environment variables
exists that identify a multi branch pipeline build.

### Normal pipeline build

When a normal pipeline build is detected the step routes the execution
to [`buildDefault`](buildDefault.md) step.

### Multi branch pipeline build

When a multi branch pipeline build is detected the step routes the
execution to the [`buildFeature`](buildFeature.md) step.

The step will then adjust the configuration for a feature branch build, e.g.
* no deploy to nexus
* automatically merge with master before build (only when running in
  trusted mode)

Finally the [`buildFeature`](buildFeature.md) calls also the
[`buildDefault`](buildDefault.md) step with the adjusted configuration.

# Configuration options

The configuration of this step is build out of the config options from
the used steps.

The step itself has no specific configuration option. It just passes the
config map to all config aware steps.

See
* [`buildDefault` configuration options](buildDefault.md#configuration-options)
* [`buildFeature` configuration options](buildFeature.md#configuration-options)

:bulb: [Documentation about configuration](../docs/config-structure.md)
