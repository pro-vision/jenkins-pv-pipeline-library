# setupPVTools

This step initializes the default tools always needed by our builds:

* JDK
* Maven

It uses the
[`setupTools`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setupTools.groovy)
step from the pipeline library.

The versions of the initialized tools can be [configured](#configuration-options).

# Table of contents
* [Defaults](#defaults)
* [Examples](#examples)
  * [Example 1: Initialize defaults](#example-1-initialize-defaults)
  * [Example 2: Initialize custom maven version](#example-2-initialize-custom-maven-version)
  * [Example 3: Initialize custom JDK](#example-3-initialize-custom-jdk)
* [Configuration options](#configuration-options)
* [Related classes](#related-classes)

## Defaults

When calling the step with defaults the following tools (configured in
Jenkins) as initialized

* sun-java8-jdk (Java SE Development Kit 8u66)
* apache-maven3 (Apache Maven 3.3.9)

## Examples

### Example 1: Initialize defaults

```groovy
setupPVTools()
```

### Example 2: Initialize custom maven version

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

setupPVTools(
    (TOOLS) : [
        (TOOL_MAVEN) : "custom-maven"
    ]
)
```

### Example 3: Initialize custom JDK

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

setupPVTools(
    (TOOLS) : [
        (TOOL_JDK) : "my-custom-jdk"
    ]
)
```

## Configuration options

All configuration options must be inside the `tools`
([`ConfigConstants.TOOLS`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

setupPVTools(
    (TOOLS) : [
        (TOOL_MAVEN) : "...", // default is retrieved from PipelineConfiguration
        (TOOL_JDK) : "..." // default is retrieved from PipelineConfiguration
    ]
)
```

### `jdk`
|                                                                                                                          ||
|:---------|:---------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.TOOL_JDK`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)           |
| Type     | `String`                                                                                                       |
| Default  | retrieved from [PipelineConfiguration](../src/de/provision/devops/jenkins/pipeline/PipelineConfiguration.groovy) |

Use this configuration option to specify the maven version you want to use.

:bulb: You can only specify maven installations here which are configured in the Jenkins instance!

### `maven`
|          |                                                                                                                  |
|:---------|:-----------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.TOOL_MAVEN`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)         |
| Type     | `String`                                                                                                         |
| Default  | retrieved from [PipelineConfiguration](../src/de/provision/devops/jenkins/pipeline/PipelineConfiguration.groovy) |

:bulb: You can only specify JDK installations here which are configured in the Jenkins instance!

## Related classes
* [`ConfigConstants`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)
