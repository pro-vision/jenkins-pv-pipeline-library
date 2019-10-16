# defaultPreparationStage

The default preparation stage takes care about the basic job setup
needed to build the project:

* initialize needed build tools
* checkout from scm
* set the build name to a meaning name
* delete project artifacts from local maven repository

# Table of contents
* [Step sequence](#step-sequence)
  * [Setup build tools](#setup-build-tools)
  * [Checkout from scm](#checkout-from-scm)
  * [Set build name](#set-build-name)
  * [Delete project maven artifacts from local repository](#delete-project-maven-artifacts-from-local-repository)
* [Examples](#examples)
  * [Example 1: Use other maven and jdk version](#example-1-use-other-maven-and-jdk)
  * [Example 2: Configure scm checkout](#example-2-configure-scm-checkout)
* [Configuration options](#configuration-options)
  * [`checkoutScm` (optional)](#checkoutscm-optional)
  * [`purgeSnapshots` (optional)](#purgesnapshots-optional)
  * [`setBuildName` (optional)](#setbuildname-optional)
  * [`setupTools` (optional)](#setuptools-optional)
  * [`stageWrap`  (optional)](#stagewrap-optional)

## Step sequence

### Setup build tools

At first the tools needed for the maven build are initialized by using
the [`setupPVTools`](setupPVTools.groovy) step.

### Checkout from scm

The code will then be checked out by using the
[`checkoutScm`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md)
step.

### Set build name

After checkout the name of the build is set to a more meaningful name like
* `#1_origin/develop`
* `#2_master`

by using the
[`setBuildName`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.md)
step.

### Delete project maven artifacts from local repository

Since we always want to ensure that the build always uses the latest
artifacts the project related maven artifacts are deleted from local
repository by using the using
[`maven.purgeSnapshots`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/maven.md) step.

## Examples

### Example 1: Use other maven and jdk

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
    (TOOLS) : [
        (TOOL_JDK) : "custom-jdk-installation",
        (TOOL_MAVEN) : "custom-maven-installation"
    ]
]

defaultPreparationStage(config)
```

### Example 2: Configure scm checkout

This example will checkout `develop` and `master` branch from github
with the `LocalBranch` extension.

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
    (SCM) : [
        (SCM_URL) : "https://github.com/wcm-io/wcm-io-wcm.git",
        (SCM_BRANCHES) : [[name: '*/master'], [name: '*/develop']],
        (SCM_EXTENSIONS) : [[$class: 'LocalBranch']]
    ]
]

defaultPreparationStage(config)
```

## Configuration options

The provided configuration map object is passed to the steps that
support configuration

* [`setupPVTools`](setupPVTools.md#configuration-options)
* [`checkoutScm.md`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md#configuration-options)

The step itself supports the following configuration options.

All configuration options must be inside the `preparationStage`
([`ConfigConstants.STAGE_PREPARATION`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

defaultPreparationStage( 
    (STAGE_PREPARATION) : [
        (STAGE_PREPARATION_CHECKOUT_SCM): true,
        (STAGE_PREPARATION_PURGE_SHAPSHOTS): true,
        (STAGE_PREPARATION_SET_BUILD_NAME): true,
        (STAGE_PREPARATION_SETUP_TOOLS): true,
        (STAGE_PREPARATION_STAGE_WRAP): true
    ]
)
```

### `checkoutScm` (optional)
|          |                                                                                                                              |
|:---------|:-----------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_PREPARATION_CHECKOUT_SCM`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                    |
| Default  | `true`                                                                                                                       |

Controls if checkout from scm is executed.

### `purgeSnapshots` (optional)
|          |                                                                                                                                 |
|:---------|:--------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_PREPARATION_PURGE_SHAPSHOTS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                       |
| Default  | `true`                                                                                                                          |

Controls if maven snapshots are purged from the local repository.

### `setBuildName` (optional)
|          |                                                                                                                                |
|:---------|:-------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_PREPARATION_SET_BUILD_NAME`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                      |
| Default  | `true`                                                                                                                         |

Controls if build name is updated.

### `setupTools` (optional)
|          |                                                                                                                             |
|:---------|:----------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_PREPARATION_SETUP_TOOLS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                   |
| Default  | `true`                                                                                                                      |

Controls if tool setup is executed.

### `stageWrap` (optional)
|          |                                                                                                                             |
|:---------|:----------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_PREPARATION_STAGE_WRAP`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                   |
| Default  | `true`                                                                                                                      |

Controls if the steps are wrapped into a stage named "Preparation". Can
be useful in declarative pipelines.




