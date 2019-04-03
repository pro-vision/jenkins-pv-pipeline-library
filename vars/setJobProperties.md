# setDefaultJobProperties

This step sets the default job properties.

This is done by default in the
`defaultBuildWrapper`](defaultBuildWrapper.md) so the steps using the
default wrapper:

* [`buildDefault`](buildDefault.md)
* [`buildFeature`](buildFeature.md)
* [`routeDefaultJenkinsFile`](routeDefaultJenkinsFile.md)

are implicit using this step.

# Table of contents
* [Defaults](#defaults)
    * [Rebuild settings](#rebuild-settings)
    * [Triggers](#triggers)
    * [Build discarding](#build-discarding)
    * [Concurrent builds](#concurrent-builds)
* [Configuration options](#configuration-options)
    * [`buildDiscarder`](#builddiscarder)
        * [`artifactsNumToKeep`](#artifactsnumtokeep)
        * [`artifactDaysToKeep`](#artifactdaystokeep)
        * [`daysToKeep`](#daystokeep)
        * [`numToKeep`](#numtokeep)
    * [`disableConcurrentBuilds`](#disableconcurrentbuilds)
    * [`parameters`](#parameters)
    * [`pipelineTriggers`](#pipelinetriggers)

## Defaults

### Rebuild settings

* no auto rebuild
* rebuild is enabled

```groovy
[$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false]
```

### Triggers

:exclamation: In version 0.8 the default triggers were removed!

But you can define triggers by configuration instead!

### Build discarding

* keep builds 365 days
* keep up to 50 builds
* keep no artifacts

```groovy
buildDiscarder(
    logRotator(
        artifactDaysToKeepStr: '', 
        artifactNumToKeepStr: '', 
        daysToKeepStr: '365',
        numToKeepStr: '50'
    )
)
```

### Concurrent builds

* disable concurrent builds

```groovy
disableConcurrentBuilds()
```

## Configuration options

All configuration options must be inside the `properties`
([`ConfigConstants.PROPERTIES`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

setJobProperties(
    (PROPERTIES) : [
        (PROPERTIES_BUILD_DISCARDER) : [
            (PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP) : '',
            (PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP) : '',
            (PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP) : '365',
            (PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP) : '50'
        ],
        (PROPERTIES_DISABLE_CONCURRENT_BUILDS) : true,
        (PROPERTIES_PARAMETERS) : [
            // default: empty
        ],
        (PROPERTIES_PIPELINE_TRIGGERS) : [
            // default: empty
        ],
    ],
)
```

### `buildDiscarder`
|||
|---|---|
|Constant|[`ConfigConstants.PROPERTIES_BUILD_DISCARDER`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Map` containing configuration options for logrotator|
|Default|see below|

Use this configuration option to define the build discarding / log rotation

Default:

```
(PROPERTIES_BUILD_DISCARDER) : [
    (PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP) : '',
    (PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP) : '',
    (PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP) : '365',
    (PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP) : '50'
]
```

#### `artifactDaysToKeep`
|          |                                                                                                                                                |
|:---------|:-----------------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `String` Days to keep build artifacts                                                                                                          |
| Default  | `''`                                                                                                                                           |

Use this configuration option to define how many days artifacts will be kept.

#### `artifactsNumToKeep`
|          |                                                                                                                                               |
|:---------|:----------------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `String` Number of artifacts to keep                                                                                                          |
| Default  | `''`                                                                                                                                          |

Use this configuration option to define how many artifacts will be kept.

#### `daysToKeep`
|          |                                                                                                                                       |
|:---------|:--------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `String` Days to keep build                                                                                                           |
| Default  | `'365'` (Build will be kept max 365 days)                                                                                             |

Use this configuration option to define how many days the builds will be kept.

#### `numToKeep`
|          |                                                                                                                                      |
|:---------|:-------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `String` Number of builds to keep                                                                                                    |
| Default  | `'50'` (Max 50 builds will be kept)                                                                                                  |

Use this configuration option to define how many builds will be kept.

### `disableConcurrentBuilds`
|          |                                                                                                                                    |
|:---------|:-----------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_DISABLE_CONCURRENT_BUILDS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                          |
| Default  | `true`                                                                                                                             |

Use this configuration option to disable / enable concurrent builds.
Per default concurrent builds are disabled.

### `parameters`
|          |                                                                                                                     |
|:---------|:--------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_PARAMETERS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `List` of parameter definitions                                                                                     |
| Default  | `[]`                                                                                                                |

Use this configuration option to the pipeline triggers.

:bulb: See https://jenkins.io/doc/book/pipeline/syntax/#parameters for
more information about syntax.

### `pipelineTriggers`
|          |                                                                                                                            |
|:---------|:---------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.PROPERTIES_PIPELINE_TRIGGERS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `List` of pipeline triggers                                                                                                |
| Default  | `[]`                                                                                                                       |

Use this configuration option to the pipeline triggers.

:bulb: See https://jenkins.io/doc/book/pipeline/syntax/#triggers for
more information about syntax.
