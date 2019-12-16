# Extension options

With the version 1.2.0 extensions were introduced.
There are basically two types of extensions.

# [Extension points](#extension-points)
# [Extend](#extend-mechanism)

## Extension points

Additive extensions allows you to add custom pipeline code at specific points, like
* [`buildDefault` `preExtensions`](../vars/buildDefault.md#preextensions-optional) and
* [`buildDefault` `postExtensions`](../vars/buildDefault.md#postextensions-optional)

where you can execute custom code before and after the default set of stages.

These extensions points are just a list of references to
functions/closures which are called with the config parameter.

### Example

This example will call the default steps
* defaultPreparationStage
* defaultCompileStage
* defaultAnalyzeStage
* defaultResultStage

but it will add the preStage1 before the preparation stage and the
postStage1 after the result stage.

So the resulting stages would be:
* preStage1
* defaultPreparationStage
* defaultCompileStage
* defaultAnalyzeStage
* defaultResultStage
* postStage1

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

def preStage1(Map config) {
  stage("preStage1") {
    echo "I am doing stuff before the defaultPreparationStage, like deleting workspace etc."
  }
}

def postStage1(Map config) {
  stage("postStage1") {
    echo "I am doing stuff after the defaultResultStage, like deployments etc."
  }  
}

buildDefault(
  (BUILD_DEFAULT) : [
    (BUILD_DEFAULT_PRE_EXTENSIONS) : [
      this.&preStage1 
    ],
    (BUILD_DEFAULT_POST_EXTENSIONS) : [
      this.&postStage1 
    ]
  ]
)

```

## Extend Mechanism

The extend mechanism allows you to extend or even replace a step, like

* [`buildFeature`](../vars/buildFeature.md#_extend-optional)
* [`defaultAnalyzeStage`](../vars/defaultAnalyzeStage.md#_extend-optional)
* [`defaultCompileStage`](../vars/defaultCompileStage.md#_extend-optional)
* [`defaultPreparationStage`](../vars/defaultPreparationStage.md#_extend-optional)
* [`defaultResultsStage`](../vars/defaultResultsStage.md#_extend-optional)

The callback provided by using the configuration option like
`ConfigConstants.STAGE_ANALYZE_EXTEND` will be called with

* `Map config` - the configuration to the time of the call
* `superImpl` - reference to the original implementation

So it is up to you to use the original implementation or replace it

### Example

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

def customPreparationStage(Map config, superImpl) {
  echo "i am executing code before the defaultPreparationStage"
  superImpl(config)
}

def customCompileStage(Map config, superImpl) {
  superImpl(config)
  echo "i am executing code before the customCompileStage"
}

def customResultsStage(Map config, superImpl) {
  echo "I am replacing the defaultResultsStage"
}

Map config = [  
  (STAGE_PREPARATION) : [
    (STAGE_PREPARATION_EXTEND) : this.&customPreparationStage
  ],
  (STAGE_COMPILE) : [
    (STAGE_COMPILE_EXTEND) : this.&customCompileStage
  ],
  (STAGE_RESULTS) : [
    (STAGE_RESULTS_EXTEND) : this.&customResultsStage
  ]
]

routeDefaultJenkinsFile(config)

```