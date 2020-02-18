# featurePreparationStage

The `featurePreparationStage` runs after the
[`defaultPreparationStage`](defaultPreparationStage.md) and is
responsible for setting up a feature branch build during execution of
[`buildFeature`](buildFeature.groovy).

# Table of contents
* [Tasks](#tasks)
 *  [Merge with parent branch](#merge-with-parent-branch)
* [Configuration options](#configuration-options)
  * [`merge` (optional)](#merge-optional)
    * [`enabled` (optional)](#enabled-optional)

## Tasks

### Merge with parent branch

Since feature branches should integrate without merge conflicts into
their parent branch the current feature branch is merged with this
branch.

For determing the parent branch the function
[`gitTools.getParentBranch()`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/gitTools.md#string-getparentbranch)
from the
[wcm.io DevOps Jenkins Pipeline Library](https://github.com/wcm-io-devops/jenkins-pipeline-library)
is used.

## Configuration options

All configuration options must be inside the `featurePreparationStage`
([`ConfigConstants.STAGE_FEATURE_PREPARATION`](../src/de/provision/devops/jenkins/pipeline/utils/ConfigConstants.groovy))
map element to be evaluated and used by the step.

```groovy
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAP_MERGE_MODE

Map config = [
  (STAGE_FEATURE_PREPARATION): [
    (STAGE_FEATURE_PREPARATION_MERGE): [
      (STAGE_FEATURE_PREPARATION_MERGE_ENABLED): true,
      (MAP_MERGE_MODE)                         : (MapMergeMode.REPLACE)
    ]
  ]
]

featurePreparationStage(config)
```
### `merge` (optional)

|          |                                                                                                                                                                                            |
|:---------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_FEATURE_PREPARATION_MERGE`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Map`                                                                                                                                                                                      |
| Default  | see  Map in [Configuration options](#configuration-options)                                                                                                                                |

Map for merge configuration options.

#### `enabled` (optional)

|          |                                                                                                                                                                                                    |
|:---------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Constant | [`ConfigConstants.STAGE_FEATURE_PREPARATION_MERGE_ENABLED`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/utils/ConfigConstants.groovy) |
| Type     | `Boolean`                                                                                                                                                                                          |
| Default  | `true`                                                                                                                                                                                             |

Enables / disables the merge with the parent branch.

## Related Classes
* [`GitCommandBuilderImpl`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/shell/GitCommandBuilderImpl.groovy)
