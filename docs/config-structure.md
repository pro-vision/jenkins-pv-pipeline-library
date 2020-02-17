# Configuration

Since everything passed to pipeline steps must be serializable the
library uses maps for configuration. In Groovy LinkedHashMaps are the
default implementation when creating a Map with `[:]` syntax.

:bulb: It is also recommended to read the documentation about
[config structure](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/config-structure.md)
from the pipeline library.

# Table of contents
* [Configuration merging / overriding](#configuration-merging--overriding)
* [Complete configuration](#complete-configuration)
* [Configuration examples](#configuration-examples)
    * [Example 1: Use different JDK and Maven](#example-1-use-different-jdk-and-maven)
    * [Example 2: Configure execMaven step in compileStage](#example-2-configure-execmaven-step-in-compilestage)
    * [Example 3: Run on specific node](#example-3-run-on-specific-node)
    * [Example 4: Run on a node matching label expression](#example-4-run-on-a-node-matching-label-expression)
    * [Example 5: Execute custom code after main build](#example-5-execute-custom-code-after-main-build)
    * [Example 6: Configure custom pipeline triggers](#example-6-configure-custom-pipeline-triggers)
    * [Example 7: Example 7: Configure custom notifications](#example-7-configure-custom-notifications)

## Configuration merging / overriding



## Complete configuration

This shows the complete configuration tree with default values.

:exclamation: **Do not** use this snippet as a base
for a project configuration.

:bulb: Always define only the options which differ from the defaults!

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import java.util.concurrent.TimeUnit

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
  (ANSI_COLOR) : ANSI_COLOR_XTERM,
  (BUILD_DEFAULT) : [
    (BUILD_DEFAULT_PRE_EXTENSIONS): [],
    (BUILD_DEFAULT_POST_EXTENSIONS): [],
  ],
  (BUILD_FEATURE) : [
    (BUILD_FEATURE_EXTEND) : null,
    (STAGE_COMPILE): [
      (MAVEN): [
        (MAVEN_GOALS)   : ["clean", "install"],
        (MAP_MERGE_MODE): MapMergeMode.REPLACE
      ]
    ]
  ],
  (BUILD_WRAPPER) : [
    (BUILD_WRAPPER_SSH_TARGETS): []    
  ],
  (LOGLEVEL) : LogLevel.INFO,
  (NODE) : null,
  (NOTIFY) : [ 
    // please refer to https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.md
    // for detailed configuraton options 
    (NOTIFY_ATTACH_LOG): false,
    (NOTIFY_ATTACHMENTS_PATTERN): '',
    (NOTIFY_BODY): null, // default content from email ext is used here
    (NOTIFY_COMPRESS_LOG): false,
    (NOTIFY_ENABLED): true,
    (NOTIFY_MIME_TYPE): null,
    (NOTIFY_ON_ABORT): true,
    (NOTIFY_ON_FAILURE): true,
    (NOTIFY_ON_STILL_FAILING): true,
    (NOTIFY_ON_FIXED): true,        
    (NOTIFY_ON_SUCCESS): false,    
    (NOTIFY_ON_UNSTABLE): true,
    (NOTIFY_ON_STILL_UNSTABLE): true,
    (NOTIFY_RECIPIENT_PROVIDERS) : null,
    (NOTIFY_SUBJECT): '${PROJECT_NAME} - Build # ${BUILD_NUMBER} - ${NOTIFICATION_TRIGGER}',
    (NOTIFY_TO): null
  ],
  (PROPERTIES) : [
    (PROPERTIES_BUILD_DISCARDER) : [
      (PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP) : '',
      (PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP) : '',
      (PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP) : '365',
      (PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP) : '50'
    ],
    (PROPERTIES_CUSTOM) : [],
    (PROPERTIES_DISABLE_CONCURRENT_BUILDS) : true,
    (PROPERTIES_PARAMETERS) : [],
    (PROPERTIES_PIPELINE_TRIGGERS) : [
        pollSCM('H/15 * * * 0-6'),
        cron('H * * * *'),
        upstream(threshold: 'SUCCESS', upstreamProjects: '/path/to/job')
    ]
  ],
  (SCM) : [
    (SCM_BRANCHES): [[name: 'branch identifier']],
    (SCM_CREDENTIALS_ID): "jenkins credential id",
    (SCM_DO_GENERATE_SUBMODULE_CONFIGURATION): false,
    (SCM_EXTENSIONS): [[$class: 'Class of git extension']],
    (SCM_SUBMODULE_CONFIG): [],
    (SCM_URL): "repository-url",
    (SCM_USER_REMOTE_CONFIG): [credentialsId: 'jenkins-credential-id', url: 'git@domain.tld/group/project.git'],
    (SCM_USER_REMOTE_CONFIGS): [[credentialsId: 'jenkins-credential-id', url: 'git@domain.tld/group/project.git']],
    (SCM_USE_SCM_VAR): true
  ],
  (STAGE_ANALYZE) : [
    (STAGE_ANALYZE_ENABLED): true,
    (STAGE_ANALYZE_EXTEND): null,
    (MAVEN) : [
      (MAVEN_ARGUMENTS): [ "-B" ],
      (MAVEN_DEFINES): ["continuousIntegration": true],
      (MAVEN_EXECUTABLE): "mvn",
      (MAVEN_GLOBAL_SETTINGS): null, // retrieved via auto lookup
      (MAVEN_GOALS): ["checkstyle:checkstyle", "pmd:pmd", "spotbugs:spotbugs"],
      (MAVEN_POM): "pom.xml",
      (MAVEN_SETTINGS): null // retrieved via auto lookup
    ],
    (STASH) : false        
  ],
  (STAGE_COMPILE) : [
    (STAGE_COMPILE_EXTEND): null,
    (MAVEN) : [
      (MAVEN_ARGUMENTS): [ "-B", "-U" ],
      (MAVEN_DEFINES): ["continuousIntegration": true],
      (MAVEN_EXECUTABLE): "mvn",
      (MAVEN_GLOBAL_SETTINGS): null, // retrieved via auto lookup
      (MAVEN_GOALS): ["clean", "deploy"],
      (MAVEN_INJECT_PARAMS): false,
      (MAVEN_POM): "pom.xml",
      (MAVEN_PROFILES): [],
      (MAVEN_SETTINGS): null // retrieved via auto lookup
    ],
    (STASH) : false
  ],
  (STAGE_PREPARATION) : [
    (STAGE_PREPARATION_CHECKOUT_SCM): true,
    (STAGE_PREPARATION_EXTEND): null,
    (STAGE_PREPARATION_SETUP_TOOLS): true,
    (STAGE_PREPARATION_PURGE_SHAPSHOTS): true,
    (STAGE_PREPARATION_SET_BUILD_NAME): true
  ],
  (STAGE_RESULTS) : [
    (STAGE_RESULTS_ENABLED): true,
    (STAGE_RESULTS_EXTEND): null,
    (STAGE_RESULTS_ANALYSIS_PUBLISHER) : [
      (STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED) : true
    ],
    (STAGE_RESULTS_CHECKSTYLE) : [
      (STAGE_RESULTS_CHECKSTYLE_ENABLED) : true
    ],
    (STAGE_RESULTS_JACOCO) : [
      (STAGE_RESULTS_JACOCO_ENABLED) : true,
      (STAGE_RESULTS_JACOCO_CLASS_PATTERN): '**/target/classes',
      (STAGE_RESULTS_JACOCO_EXEC_PATTERN): '**/target/**.exec',
      (STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN): "",
      (STAGE_RESULTS_JACOCO_INCLUSION_PATTERN): "",
      (STAGE_RESULTS_JACOCO_SOURCE_PATTERN): "**/src/main/java",
      (STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD): false,
      (STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS): false,
      (STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES):false,
      (STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE): "0",
      (STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE): "0",
    ],
    (STAGE_RESULTS_JUNIT) : [
      (STAGE_RESULTS_JUNIT_ENABLED) : true
    ],
    (STAGE_RESULTS_FINDBUGS) : [
      (STAGE_RESULTS_FINDBUGS_ENABLED) : true
    ],
    (STAGE_RESULTS_OPEN_TASKS) : [
      (STAGE_RESULTS_OPEN_TASKS_ENABLED) : true
    ],
    (STAGE_RESULTS_PMD) : [
      (STAGE_RESULTS_PMD_ENABLED) : true
    ],
  ],
  (TIMEOUT) : [
    (TIMEOUT_TIME) : 30, 
    (TIMEOUT_UNIT) : TimeUnit.MINUTES
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

## Configuration examples

The examples all assume a Jenkinsfile using [`routeDefaultJenkinsFile`](../vars/routeDefaultJenkinsFile.md)

### Example 1: Use different JDK and Maven

This example will setup the following tools
* JDK7 (must be configured in your jenkins with that name)
* Maven 3.0.5 (must be configured in your jenkins with that name)

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (TOOLS) : [
    (TOOL_JDK) : "sun-java-7",
    (TOOL_MAVEN) : "apache-maven_3_0_5"
  ]
]

routeDefaultJenkinsFile(config)
```

### Example 2: Configure execMaven step in compileStage

This example:
* uses a profile `-PcustomProfile`
* adds debug switch `-X`
* defines a system property `-Dmaven.test.failure.ignore=true`
* runs with custom goals `clean deploy site`

```groovy
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (STAGE_COMPILE) : [
    (MAVEN) : [
      (MAVEN_ARGUMENTS): ["-PcustomProfile", "-X"],
      (MAVEN_DEFINES): [
          "maven.test.failure.ignore" : true
      ],
      (MAVEN_EXECUTABLE): "mvn",                
      (MAVEN_GOALS): ["clean", "deploy", "site"]
    ],
    (STASH) : false
  ],    
]

routeDefaultJenkinsFile(config)
```

### Example 3: Run on specific node

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (NODE) : "<your-jenkins-node-name>"
]

routeDefaultJenkinsFile(config)
```

### Example 4: Run on a node matching label expression

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (NODE) : 'unix && 64bit && !debian'
]

routeDefaultJenkinsFile(config)
```

### Example 5: Execute custom code after main build

```groovy
import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (STAGE_ANALYZE) : [            
    // stash workspace after running maven analyze step
    (STASH) : true
  ]    
]

routeDefaultJenkinsFile(config)

defaultBuildWrapper(config) {
  node() {
    unstash(name: ConfigConstants.STASH_ANALYZE_FILES)
    // print out workspace
    sh "ls -la"
  }
}

```

### Example 6: Configure custom pipeline triggers

This example
* polls the SCM every 30 minutes on each day
* executes a build every hour
* executes a build when the upstream job `path/to/upstream/job` was
  build with result `UNSTABLE` or `SUCCESS`

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [    
  (ConfigConstants.PROPERTIES) : [
    (ConfigConstants.PROPERTIES_PIPELINE_TRIGGERS) : [
        pollSCM("H/30 * * * *"),
        cron("H * * * *"),
        upstream(threshold: 'UNSTABLE', upstreamProjects: 'path/to/upstream/job')
    ]
  ]
]

routeDefaultJenkinsFile(config)
```

### Example 7: Configure custom notifications

This example
* configures custom notifications for each possible build result

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
Map config = [
  (NOTIFY) : [
    (NOTIFY_ON_ABORT) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider']
      ]
    ],
    (NOTIFY_ON_UNSTABLE) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider'],
        [$class: 'DevelopersRecipientProvider']
       ]     
    ],
    (NOTIFY_ON_STILL_UNSTABLE) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider'],
        [$class: 'DevelopersRecipientProvider']
       ],
     (NOTIFY_TO): "dev-teamlead@company.com"
    ],
    (NOTIFY_ON_FAILURE) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider'],
        [$class: 'DevelopersRecipientProvider']
       ]     
    ],
    (NOTIFY_ON_STILL_FAILING) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider'],
        [$class: 'DevelopersRecipientProvider']
       ],
     (NOTIFY_TO): "dev-teamlead@company.local"
    ],
    (NOTIFY_ON_FIXED) : [
      (NOTIFY_RECIPIENT_PROVIDERS) : [
        [$class: 'RequesterRecipientProvider'],
        [$class: 'DevelopersRecipientProvider']
       ]
    ],
    (NOTIFY_ON_SUCCESS) : [
      (NOTIFY_TO): "successful-builds@company.local"
    ]
  ]  
]

routeDefaultJenkinsFile(config)
```
