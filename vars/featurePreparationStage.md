# featurePreparationStage

The `featurePreparationStage` runs after the
[`defaultPreparationStage`](defaultPreparationStage.md) and is
responsible for setting up a feature branch build during execution of
[`buildFeature`](buildFeature.groovy).

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

## Related Classes
* [`GitCommandBuilderImpl`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/shell/GitCommandBuilderImpl.groovy)
