# defaultBuildWrapper

The default build wrapper was introduced to increase reusability and
reduce the amount of code to write in pipeline to get
* timestamps in console
* email- or instant messenger notifications
* build timeouts
* ssh agents
* scm polling
* log discarding

This step should be used as a wrapper for all custom pipeline scripts.
This step is also used by the [`buildDefault`](buildDefault.md) step

# Table of contents
* [Step sequence](#step-sequence)
  * [Initialize the logger](#initialize-the-logger)
    * [Wrap build into try/catch](#wrap-build-into-trycatch)
    * [Set build timeout](#set-build-timeout)
    * [Enable timestamps on console](#enable-timestamps-on-console)
    * [Set default job properties](#set-default-job-properties)
    * [Provide ssh-agent](#provide-ssh-agent)
    * [Execute the provided closure](#execute-the-provided-closure)
    * [Notify](#notify)
* [Examples](#examples)
    * [Example 1: Simple echo example](#example-1-simple-echo-example)
    * [Example 2: Node allocation, LogLevel.TRACE and success mail notification](#example-2-node-allocation-logleveltrace-and-success-mail-notification)
    * [Example 3: Node allocation, LogLevel.TRACE and success mail notification](#example-3-increase-build-timeout)
* [Configuration options](#configuration-options)
    * [`timeoutTime`](#timeouttime)
    * [`timeoutUnit`](#timeoutunit)
  *   [`sshTargets`](#sshtargets)
* [Related classes](#related-classes)

## Step sequence

### Initialize the logger

In the first step the [logger](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/logging.md) of the [pipeline library](https://github.com/wcm-io-devops/jenkins-pipeline-library/tree/master/docs) is initialized.

### Wrap build into try/catch

The complete build is then wrapped inside a try and catch to enable
email notification when an exception like `HudsonAbortException` is thrown.

### Set build timeout

The pipeline then gets a build timeout of 30 minutes.

### Enable timestamps on console

After wrapping into the timeout wrapper the timestamps on the console output
are enabled.

### Set default job properties

Then the default job properties are set by calling the
[`setJobProperties`](setJobProperties.groovy) step.

See [setJobProperties](setJobProperties.md) for configuration options.

### Provide ssh-agent

The `defaultBuildWrapper` is able to wrap the closure with an ssh-agent.

```groovy
import io.wcm.devops.jenkins.pipeline.ssh.SSHTarget 

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
  (BUILD_WRAPPER) : [
    (BUILD_WRAPPER_SSH_TARGETS) : [ new SSHTarget("ssh-host.company.tld") ]
  ]
]

routeDefaultJenkinsFile(config)
```

See jenkins-pipeline-library [sshAgentWrapper](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/sshAgentWrapper.md) for details.

### Execute the provided closure

After the job properties are set the closure is called

### Notify

Finally the wrapper calls the
* [`notify.mail`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notify.md) and
* [`notify.mattermost`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notify.md)
* [`notify.mqtt`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notify.md)
* [`notify.teams`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notify.md)

steps and depending on configuration and build result notifications are
send.

## Examples

### Example 1: Simple echo example

```groovy
defaultBuildWrapper() {
    echo "test"    
}
```

### Example 2: Node allocation, LogLevel.TRACE and success mail notification

This example
* configures the pipeline logger with `LogLevel.TRACE`
* print the current workspace in the log
* Send a notification to my.mail@example.com after every successful
  build (and on the [`notifyMail`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.md#configuration-options) default triggers)

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [
    (LOGLEVEL) : LogLevel.TRACE,
    (NOTIFY) : [
        (NOTIFY_ON_SUCCESS) : true,
        (NOTIFY_TO) : 'my.mail@example.com'
    ]
]
defaultBuildWrapper(config) {
    node() {
        sh 'pwd' // prints the current working directory
    }    
}
```

### Example 3: Increase build timeout

This example
* changes the build timeout to one hour

```groovy
import java.util.concurrent.TimeUnit

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

Map config = [
    (TIMEOUT) : [
        (TIMEOUT_TIME) : 1,
        (TIMEOUT_UNIT) : TimeUnit.HOURS
    ]
]
defaultBuildWrapper(config) {
    node() {
        sh 'pwd' // prints the current working directory
    }    
}
```

## Configuration options

All configuration options must be inside the `timeout`
([`ConfigConstants.TIMEOUT`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
or `buildWrapper`
([`ConfigConstants.BUILD_WRAPPER`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy


import io.wcm.devops.jenkins.pipeline.ssh.SSHTarget

import java.util.concurrent.TimeUnit
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

defaultBuildWrapper(
    (TIMEOUT) : [
        (TIMEOUT_TIME) : 30,
        (TIMEOUT_UNIT) : TimeUnit.MINUTES
    ],
    (BUILD_WRAPPER) : [
      (BUILD_WRAPPER_SSH_TARGETS) : [ new SSHTarget("ssh-host.company.tld") ]
    ]
) {
    // closure content
}
```

:bulb: The provided config map is also passed to
* [`setJobProperties`](setJobProperties.groovy)
* [`Logger.init`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/logging/Logger.groovy)
* [`notifyMail`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.md#configuration-options) default triggers)

### `timeoutTime`
|          |                                                                                                            |
|:---------|:-----------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.TIMEOUT_TIME`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Integer`                                                                                                  |
| Default  | `30`                                                                                                       |

Use this configuration option to specify the integer component of the build timeout.

### `timeoutUnit`
|          |                                                                                                            |
|:---------|:-----------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.TIMEOUT_UNIT`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `java.util.concurrent.TimeUnit`                                                                            |
| Default  | `TimeUnit.MINUTES`                                                                                         |

Use this configuration option to specify the TimeUnit component of the build timeout.

### `sshTargets`
|          |                                                                                                                         |    |
|:---------|:------------------------------------------------------------------------------------------------------------------------|:---|
| Constant | [`ConfigConstants.BUILD_WRAPPER_SSH_TARGETS`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |    |
| Type     | List of `io.wcm.devops.jenkins.pipeline.ssh.SSHTarget`                                                                  |    |
| Default  | `[]`                                                                                                                    |    |

Use this configuration option to specify a list of sshTarget to prepare
an ssh-agent for.  
See jenkins-pipeline-library
[sshAgentWrapper](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/sshAgentWrapper.md)
for details (especially for the autolookup)

## Related classes
* [`LogLevel`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/logging/LogLevel.groovy)
* [`Logger`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/logging/Logger.groovy)
