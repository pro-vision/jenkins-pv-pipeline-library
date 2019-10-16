/*-
 * #%L
 * pro!vision GmbH
 * %%
 * Copyright (C) 2018 pro!vision GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package vars.defaultPreparationStage

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Test

import static de.provision.devops.testing.jenkins.pipeline.PVStepConstants.*
import static io.wcm.testing.jenkins.pipeline.StepConstants.*
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertTwice
import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertEquals

class DefaultPreparationStageIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldRunWithDefaults() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/defaultPreparationStageDefaultsTestJob.groovy")

    Map actualCheckoutCall = (Map) assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertOnce(SET_BUILD_NAME)
    assertOnce(STAGE)

    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())

    Map expectedCheckoutCall = [
      '$class'                         : 'GitSCM',
      branches                         : [
        [name: '*/master'],
        [name: '*/develop']
      ],
      doGenerateSubmoduleConfigurations: false,
      extensions                       : [
        ['$class': 'LocalBranch']
      ],
      submoduleCfg                     : [],
      userRemoteConfigs                : [
        [url: 'git@git-ssh.domain.tld/group1/project1', credentialsId: 'ssh-git-credentials-id']
      ]
    ]

    assertEquals("Checkout call is not correct", expectedCheckoutCall, actualCheckoutCall)
  }

  @Test
  void shouldRunWithCustomConfig() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/defaultPreparationStageCustomTestJob.groovy")

    assertNone(STAGE)
    Map actualCheckoutCall = (Map) assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertOnce(SET_BUILD_NAME)

    assertArrayEquals("error in executed tool commands", ["customMaven", "customJDK"].toArray(), toolCalls.toArray())

    Map expectedCheckoutCall = [
      '$class'                         : 'GitSCM',
      branches                         : [
        [name: '*/customBranch']
      ],
      doGenerateSubmoduleConfigurations: false,
      extensions                       : [
        ['$class': 'PreBuildMerge', options: [fastForwardMode: 'FF', mergeRemote: 'origin', mergeStrategy: 'default', mergeTarget: 'master']]
      ],
      submoduleCfg                     : [],
      userRemoteConfigs                : [
        [url: 'git@git-ssh.domain.tld/group1/project1', credentialsId: 'ssh-git-credentials-id']
      ]
    ]

    assertEquals("Checkout call is not correct", expectedCheckoutCall, actualCheckoutCall)
  }

  @Test
  void shouldOnlySetBuildName() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/shouldOnlySetBuildNameTestJob.groovy")

    assertOnce(STAGE)
    assertNone(CHECKOUT)
    assertNone(TOOL)
    assertNone(MAVEN_PURGE_SNAPSHOTS)
    assertOnce(SET_BUILD_NAME)
  }

  @Test
  void shouldOnlySetupTools() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/shouldOnlySetupToolsTestJob.groovy")

    assertOnce(STAGE)
    assertNone(CHECKOUT)
    assertTwice(TOOL)
    assertNone(MAVEN_PURGE_SNAPSHOTS)
    assertNone(SET_BUILD_NAME)
  }

  @Test
  void shouldOnlyPurgeRepositoryTestJob() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/shouldOnlyPurgeRepositoryTestJob.groovy")

    assertOnce(STAGE)
    assertNone(CHECKOUT)
    assertNone(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertNone(SET_BUILD_NAME)
  }

  @Test
  void shouldOnlyCheckoutScmTestJob() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/shouldOnlyCheckoutScmTestJob.groovy")

    assertOnce(STAGE)
    assertOnce(CHECKOUT)
    assertNone(TOOL)
    assertNone(MAVEN_PURGE_SNAPSHOTS)
    assertNone(SET_BUILD_NAME)
  }

  @Test
  void shouldDoNothingTestJob() {
    loadAndExecuteScript("vars/defaultPreparationStage/jobs/shouldDoNothingTestJob.groovy")

    assertOnce(STAGE)
    assertNone(CHECKOUT)
    assertNone(TOOL)
    assertNone(MAVEN_PURGE_SNAPSHOTS)
    assertNone(SET_BUILD_NAME)
  }

  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()
    helper.registerAllowedMethod(PURGE_SNAPSHOTS_FROM_REPOSITORY, [], {
      stepRecorder.record(PURGE_SNAPSHOTS_FROM_REPOSITORY, null)
    })
    helper.registerAllowedMethod(SET_BUILD_NAME, [], {
      stepRecorder.record(SET_BUILD_NAME, null)
    })
  }
}
