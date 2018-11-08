# featurePreparationStage

The `featurePreparationStage` runs after the
[`defaultPreparationStage`](defaultPreparationStage.md) and is
responsible for setting up a feature branch build during execution of
[`buildFeature`](buildFeature.groovy).

## Tasks

### Merge with master

Since feature branches should integrate without merge conflicts into
master the current feature branch is merged with the `master` branch of
`origin`

:bulb: This was done by using the PreBuild Merge extension until version
0.7 but due to refactoring of some of the plugins this is no longer
possible for multibranch pipeline builds

## Related Classes
* [`GitCommandBuilderImpl`](https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/src/io/wcm/devops/jenkins/pipeline/shell/GitCommandBuilderImpl.groovy)
