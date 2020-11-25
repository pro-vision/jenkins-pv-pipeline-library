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
* [Step sequence](#step-sequence)
    * [Get default maven defines](#get-default-maven-defines)
    * [Configuration merge](#configuration-merge)
    * [Execute Maven](#execute-maven)
    * [Stashing workspace files](#stashing-workspace-files)
* [Extension options](#extension-options)
* [Configuration options](#configuration-options)
    * [`_extend`](#_extend-optional)
    * [`enabled`](#enabled-optional)
    * [`maven`](#maven-optional)
    * [`stash`](#stash-optional)
* [Related classes](#related-classes)

## Step sequence

### Get default maven defines

This step calls the
[`getDefaultMavenDefines`](getDefaultMavenDefines.md) step first to enable
defines like
* `-Dcontinuous-integration=true` or
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

## Extension options

This step supports the extension mechanism, so you are able to extend
the step by executing code before and/or after the step is executed or
even replacing the whole functionality.

:bulb: See [`_extend`](#_extend-optional) for an example.

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
    (STAGE_ANALYZE_ENABLED) : true,
    (STAGE_ANALYZE_EXTEND) : null,
    (STASH) : false
  ]
)
```

### `enabled` (optional)
|||
|---|---|
|Constant|[`ConfigConstants.STAGE_ANALYZE_ENABLED`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Boolean`|
|Default|`true`|

Use this configuration option to enable (default) or disable the
`defaultAnalyzeStage` step.

### `_extend` (optional)
|||
|---|---|
|Constant|[`ConfigConstants.STAGE_ANALYZE_EXTEND`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Closure` with signature `(Map config, superImpl)`|
|Default|`null`|

Use this configuration option to overwrite or extend the
`defaultAnalyzeStage` step.

**Example:**
```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

def customAnalyzeStage(Map config, superImpl) {
  echo "before defaultAnalyzeStage stage"
  superImpl(config)
  echo "after defaultAnalyzeStage stage"
}

defaultAnalyzeStage(
  (STAGE_ANALYZE) : [
    (STAGE_ANALYZE_EXTEND) : this.&customAnalyzeStage
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
    (MAVEN_DEFINES) : ["continuous-integration" : true]
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
