# defaultAnalyzeStage

The default analyze stage executes maven with static analysis goals for
* checkstyle
* pmd
* findbugs

and (of course) witg the default defines and settings provided by the
pipeline
([ManagedFiles](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/managed-files.md))
auto lookup mechanism.

# Table of contents
* [Workflow]()
    * [Get default maven defines]()
    * [Configuration merge]()
    * [Execute Maven]()
    * [Stashing workspace files]()
* [Configuration options]()
    * [`maven`](#maven-optional)
    * [`stash`](#stash-optional)
* [Related classes]()

## Step sequence

### Get default maven defines

This step calls the
[`getDefaultMavenDefines`](getDefaultMavenDefines.md) step first to enable
defines like
* `-DcontinuousIntegration=true` or
* `-Dnodejs.directory=${WORKSPACE}/target/.nodejs` (when a nodejs
  `package.json` was found in the project)

### Configuration merge

The step then merges the default defines and the maven defaults with the
incoming configuration.

### Execute Maven

After the configuration merge the
[`execMaven`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md)
step is called with the configuration

### Stashing workspace files

If enabled the step will stash the current workspace files under the
name `analyzeFiles` so you will be able to use these files later in your
build on another node.

:bulb: The files will be then available by using `unstash` the name `analyzeFiles` (`ConfigConstants.STASH_ANALYZE_FILES`).

## Configuration options

All configuration options must be inside the `analyze` ([`ConfigConstants.STAGE_ANALYZE`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)) map element to be
evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
defaultAnalyzeStage(
    (STAGE_ANALYZE) : [
        (MAVEN) : [
            // config from execMaven step
        ],
        (STASH) : false
    ]
)
```

### `maven` (optional)

|          |                                                                                                                                                                  |
|:---------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.MAVEN`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Map`                                                                                                                                                            |
| Default  | `null`                                                                                                                                                           |

Since the configuration is calculated inside the step the default would be
```groovy
[
    (MAVEN_GOALS) : "checkstyle:checkstyle pmd:pmd spotbugs:spotbugs",
    (MAVEN_ARGUMENTS) : "-B",
    (MAVEN_DEFINES) : [continuousIntegration : true]
]
```

:bulb: Please have a look at [`execMaven`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md#configuration-options) for a complete list of configuration options.

### `stash` (optional)
|          |                                                                                                     |
|:---------|:----------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STASH`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                           |
| Default  | `false`                                                                                             |

Controls if the workspace files will be stashed after the maven build.

:bulb: Use the available constant from
([`ConfigConstants`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))

## Related classes
* [`MapUtils`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/maps/MapUtils.groovy)