# defaultCompileStage

The default compile stage executes maven with `clean deploy -B -U` and
(of course) the default defines and settings provided by the pipeline
([ManagedFiles](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/managed-files.md))
auto lookup mechanism


and the arg

# Table of contents
* [Step sequence](#step-sequence)
    * [Get default maven defines](#get-default-maven-defines)
    * [Configuration merge](#configuration-merge)
    * [Execute Maven](#execute-maven)
    * [Recording fingerprints](#recording-fingerprints)
    * [Stashing workspace files](#stashing-workspace-files)
* [Extension options](#extension-options)
* [Configuration options](#configuration-options)
    * [`_extend`](#_extend-optional)
    * [`maven`](#maven-optional)
    * [`stash`](#stash-optional)
* [RdefaultAnalyzeStage.md#maven-optional()

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

### Recording fingerprints

After the maven execution the fingerprints of the artifacts are recorded.
Used pattern: `**/target/**/*.jar,**/target/**/*.zip`

### Stashing workspace files

If enabled the step will stash the current workspace files under the
name `compileFiles` so you will be able to use these files later in your
build on another node.

:bulb: The files will be then available by using `unstash` the name `compileFiles` (`ConfigConstants.STASH_COMPILE_FILES`).

## Extension options

This step supports the extension mechanism, so you are able to extend
the step by executing code before and/or after the step is executed or
even replacing the whole functionality.

:bulb: See [`_extend`](#_extend-optional) for an example.

## Configuration options

All configuration options must be inside the `compile`
([`ConfigConstants.STAGE_COMPILE`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
defaultCompileStage(
    (STAGE_COMPILE) : [
        (MAVEN) : [
            // config from execMaven step
        ],
        (STAGE_COMPILE_EXTEND) : null,
        (STASH) : false
    ]
)
```

### `_extend` (optional)
|||
|---|---|
|Constant|[`ConfigConstants.STAGE_COMPILE_EXTEND`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Closure` with signature `(Map config, superImpl)`|
|Default|`null`|

Use this configuration option to overwrite or extend the
`defaultCompileStage` step.

**Example:**
```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

void customCompileStage(Map config, superImpl) {
  echo "before defaultCompileStage stage"
  superImpl()
  echo "after defaultCompileStage stage"
}

defaultCompileStage(
  (STAGE_COMPILE) : [
    (STAGE_COMPILE_EXTEND) : this.&customCompileStage
  ]
)

```

### `maven` (optional)

|||
|---|---|
|Constant|[`ConfigConstants.MAVEN`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Map`|
|Default|`null`|

Since the configuration is calculated inside the step the default would be
```groovy
[
    (MAVEN_GOALS) : ["clean", "deploy"],
    (MAVEN_ARGUMENTS) : ["-B", "-U"],
    (MAVEN_DEFINES) : ["continuous-integration" : true]
]
```

:bulb: Please have a look at
[`execMaven`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md#configuration-options)
for a complete list of configuration options.

:bulb: When running in [`buildFeature`](buildFeature.md) then the goals
are `clean install` per default.

### `stash` (optional)
|          |                                                                                                     |
|:---------|:----------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STASH`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                           |
| Default  | `false`                                                                                             |

Controls if the workspace files will be stashed after the maven build.

:bulb: Use the available constant from
[`ConfigConstants`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)

## Related classes
* [`MapUtils`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/maps/MapUtils.groovy)

[_extend (optional)]: #_extend-optional

