# buildDefault

This step is the main step for building the `develop` and `master`
branch for maven projects with Jenkins pipeline.

# Table of contents

* [Step sequence](#step-sequence)
  * [Wrapping the build](#wrapping-the-build)
    * [Allocating node](#allocating-node)
      * [Prepare the build](#prepare-the-build)
      * [Compile maven project](#compile-maven-project)
      * [Analyze maven project](#analyze-maven-project)
      * [Publish Results](#publish-results)
* [Configuration options](#configuration-options)
* [Default Configuration](#default-configuration)
* [Related classes](#related-classes)

## Step sequence

### Wrapping the build

This step wraps the complete build into the
[`defaultBuildWrapper`](defaultBuildWrapper.md). The
`defaultBuildWrapper` takes care about
* initializing the logger
* set build timeout
* add timestamps to console output
* set default job properties
  * scm polling
  * rebuild settings
* wrapping the build into a try catch block
* email notification

:bulb: Have a look at [defaultBuildWrapper](defaultBuildWrapper.md) for
more information.

#### Allocating node

Inside the `defaultBuildWrapper` a build node is allocated. and

##### Prepare the build

Inside the allocated node the
[`defaultPreparationStage`](defaultPreparationStage.md) is used to
prepare the build of the project.

This step
* initializes the tools (JDK and maven) by using [`setupPVTools`](setupPVTools.md)
* checks out from SCM by using
  [`checkoutScm`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md)
* sets the build name by using
  [`setBuildName`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.md)
* deletes maven snapshot artifacts from local maven repository by using
  [`maven.purgeSnapshots`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/maven.md)

##### Compile maven project

After preparation the project is build by using the
[`defaultCompileStage`](defaultCompileStage.md).

This stage takes care about merging the maven defaults with project
specific configuration (if set).

This step
* calls
  [`execMaven`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md)
  with `clean deploy -B -U` (defaults)
* stashes files from the workspace after maven execution for later use
  (if enabled)

:bulb: [`defaultCompileStage`](defaultCompileStage.groovy) is also used
during feature branch build but the maven goals are configured to `clean
install` in this case since feature branches should not deploy to jenkins

##### Analyze maven project

After compiling the project it is analyzed by using the
[defaultAnalyzeStage](defaultAnalyzeStage.md)

This stage takes care about merging the maven defaults with project
specific configuration (if set).

This step
* calls
  [`execMaven`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md)
  with `checkstyle:checkstyle pmd:pmd spotbugs:spotbugs -B` (defaults)
* stashes files from the workspace after maven execution for later use
  (if enabled)

##### Publish Results

The last step called in the `defaultBuild` is the
[`defaultResultsStage](defaultResultsStage.md)

This stage processes and publishes the following build artifacts:

* JUnit test reports
* JaCoCo code coverage
* findbugs result
* pmd results
* open tasks
* checkstyle result
* aggregate static analysis results

## Configuration options

The configuration of this step is mostly build out of the config options from
the used steps.

It just passes the config map to all config aware steps.

* [`checkoutScm`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md#configuration-options)
  config options
* [`defaultAnalyzeStage`](defaultAnalyzeStage.md#configuration-options)
* [`defaultCompileStage`](defaultCompileStage.md#configuration-options)
* [`defaultPreparationStage`](defaultPreparationStage.md#configuration-options)
* [Logging](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/logging.md#configuration-options) configuration options
* [`notifyMail`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.md)
* [`setupPVTools`](setupPVTools.md#configuration-options)

:bulb: [Documentation about configuration](../docs/config-structure.md)

### `node`

|||
|---|---|
|Constant|[`ConfigConstants.NODE`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`String`|
|Default|`null`|

Use this configuration option to specify a specific node/agent to build on.

Example:
```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
    (NODE) : 'unix && 64bit'
]
buildDefault(config)
```

See
[Using Agents](https://github.com/jenkinsci/pipeline-plugin/blob/master/TUTORIAL.md#using-agents)
from pipeline plugin tutorial for more information

## Configuration structure

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
[
    (LOGLEVEL) : LogLevel.INFO,
    (NODE) : null, // node expression to specify the used agent/slave
    (NOTIFY) : [
        // notify mail configuration options
    ],
    (SCM) : [
        // checkoutScm configuration options
    ],
    (STAGE_COMPILE) : [
        // compile stage configuration options
    ],
    (STAGE_ANALYZE) : [
        // analyze stage configuration options
    ],
    (TOOLS) : [
        // setupPVTools configuration options
    ]    
]
```

## Default Configuration

The buildDefault runs with these defaults when
* the SCM from the job is used.
* no nodejs build was found

:exclamation: This is just an example for the defaults. Please do not configure
anything when you want to use the defaults!

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [
    (LOGLEVEL) : LogLevel.INFO,
    (NODE) : null,
    (NOTIFY) : [
        // step uses notification defaults     
    ],
    (SCM) : [        
        // scm is configured to use the existing scm var
        (SCM_USE_SCM_VAR) : true
    ],
    (PROPERTIES) : [
        (PROPERTIES_PIPELINE_TRIGGERS) : [
            pollSCM('H/15 * * * 0-6')
        ]
    ],
    (STAGE_COMPILE) : [
        // maven settings for compile stage
        (MAVEN) : [
            (MAVEN_GOALS) : ["clean","deploy"],
            (MAVEN_ARGUMENTS) : ["-B","-U"],
            (MAVEN_DEFINES) : ["continuousIntegration" : true]
        ],
        (STASH) : false, 
    ],
    (STAGE_ANALYZE) : [
        // maven settings for analyze stage
        (MAVEN) : [
            (MAVEN_GOALS) : ["checkstyle:checkstyle", "pmd:pmd", "spotbugs:spotbugs"],
            (MAVEN_ARGUMENTS) : ["-B" ],
            (MAVEN_DEFINES) : ["continuousIntegration" : true]
        ],
        (STASH) : false,        
    ],
    // default tools are retrieved from PipelineConfiguration
    /*
    (TOOLS) : [
        (TOOL_JDK) : "...",
        (TOOL_MAVEN) : "..."
    ]
    */    
]

routeDefaultJenkinsFile(config)
```

## Complete configuration

:exclamation: Not all configuration options can used together. This
example gives just a complete overview of all possible configuration options

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [
    (LOGLEVEL) : LogLevel.INFO,
    (NOTIFY) : [
        // step uses notification defaults     
    ],
    (SCM) : [        
        // scm is configured to use the existing scm var
        (SCM_USE_SCM_VAR) : true
    ],
    (PROPERTIES) : [
        (PROPERTIES_PIPELINE_TRIGGERS) : [
            pollSCM('H/15 * * * 0-6')
        ]
    ],
    (STAGE_COMPILE) : [
        // maven settings for compile stage
        (MAVEN) : [
            (MAVEN_GOALS) : [ "clean", "deploy" ],
            (MAVEN_ARGUMENTS) : [ "-B", "-U" ],
            (MAVEN_DEFINES) : [ "continuousIntegration" : true ]
        ],
        (STASH) : false, 
    ],
    (STAGE_ANALYZE) : [
        // maven settings for analyze stage
        (MAVEN) : [
            (MAVEN_GOALS) : [ "checkstyle:checkstyle", "pmd:pmd", "spotbugs:spotbugs" ],
            (MAVEN_ARGUMENTS) : [ "-B" ],
            (MAVEN_DEFINES) : [ "continuousIntegration" : true ]
        ],
        (STASH) : false,        
    ],
    // default tools are retrieved from PipelineConfiguration
    /*
    (TOOLS) : [
        (TOOL_JDK) : "...",
        (TOOL_MAVEN) : "..."
    ]
    */   
]

routeDefaultJenkinsFile(config)
```

## Related classes
* [`ConfigConstants`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)
