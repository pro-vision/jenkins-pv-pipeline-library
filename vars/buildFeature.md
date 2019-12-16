# buildFeature

This step is the main step for building the `feature/*` branches for
maven projects with Jenkins multibranch pipeline.

Basically the `buildFeature` step calls the
[`buildDefault`](buildDefault.md) step with a changed configuration.

:bulb: Have a look at the
[feature branch job tutorial](../docs/tutorials/setup-feature-branch/README.md).

# Table of contents

* [Modes](#modes)
  * [Mode 1 (trusted)](#mode-1-trusted)
  * [Mode 2 (untrusted)](#mode-2-untrusted)
* [Extension options](#extension-options)
* [Configuration options](#configuration-options)
  * [`_extend`](#_extend-optional)
* [Related classes](#related-classes)

## Modes

The `buildFeature` step support two modes depending on the groovy script
security and if the lib is running as a global trusted library or a
folder/project library.

Both modes change the configuration of the
[`defaultCompileStage`](defaultCompileStage.md) because featurebranch
artifacts should not be deployed to nexus!

The configuration adjustment is done by doing a merge with the incoming
configuration. So you are still able to provide a custom maven
configuration for the stage.

This is the default configuration passed to buildDefault
```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
 
Map config = [
    (STAGE_COMPILE) : [
        (MAVEN) : [
            (MAVEN_GOALS) : "clean install"
        ]
    ]
]
```

### Mode 1 (trusted)

This mode is only working if the library is trusted or the belonging
groovy calls are approved in script approval.

In this mode you just configure the SCM and the gredentials on the
multibranch pipeline page.

The step will then read the `scm` pipeline variable and transforms the
existing scm object into a Map based configuration which will then be
passed to `buildDefault` and the nested `checkoutScm` step.

The step will transform the following `scm` properties into the scm configuration:
* `userRemoteConfigs`
* `extensions`, currently supported:
    *  CleanBeforeCheckout
    *  CleanCheckout
    *  PreBuildMerge
*  `branches`
*  `submoduleCfg`
*  `doGenerateSubmoduleConfigurations`

This step automatically adds:
* the `PreBuildMerge` extension to merge with the master

### Mode 2 (untrusted)

This mode is the "fallback" mode when Mode 1 was rejected by the Groovy
Sandbox.

:bulb: In most cases this would be the mode used since running a library
as global trusted can have negative side effects!

In this mode a scm configuration is added to tell the step
[`checkoutScm`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md)
that the existing `scm` variable has to be used for checkout.

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

Map config = [
    (SCM) : [
            (SCM_USE_SCM_VAR) : true 
    ],
    (STAGE_COMPILE) : [
        (MAVEN) : [
            (MAVEN_GOALS) : "clean install"
        ]
    ]    
]
```

## Extension options

This step supports the extension mechanism, so you are able to extend
the step by executing code before and/or after the step is executed or
even replacing the whole functionality.

:bulb: See [`_extend`](#_extend-optional) for an example.

## Configuration options

The `buildFeature` step reuses the `buildDefault` step with adjusted
configuration so please have a look at
[`buildDefault` configuration options](buildDefault.md#configuration-options)

:bulb: [Documentation about configuration](../docs/config-structure.md)

All configuration options for the `buildFeature` stage must be inside
the
[`ConfigConstants.BUILD_FEATURE`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)
map element to be evaluated and used by the step.

### `_extend` (optional)
|||
|---|---|
|Constant|[`ConfigConstants.BUILD_FEATURE_EXTEND`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy)|
|Type|`Closure` with signature `(Map config, superImpl)`|
|Default|`null`|

Use this configuration option to overwrite or extend the `buildFeature`
step.

**Example:**
```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

void customFeatureStage(Map config, superImpl) {
  echo "before defaultFeature stage"
  superImpl()
  echo "after defaultFeature stage"
}

buildFeature(
  (BUILD_FEATURE) : [
    (BUILD_FEATURE_EXTEND) : this.&customFeatureStage
  ]
)

```

## Related classes
* [`MapUtils`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/maps/MapUtils.groovy)
