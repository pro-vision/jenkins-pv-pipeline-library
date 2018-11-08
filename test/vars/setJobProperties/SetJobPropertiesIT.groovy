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
package vars.setJobProperties

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import io.wcm.testing.jenkins.pipeline.StepConstants
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.BUILD_DISCARDER
import static io.wcm.testing.jenkins.pipeline.StepConstants.LOG_ROTATOR
import static io.wcm.testing.jenkins.pipeline.StepConstants.PIPELINE_TRIGGERS
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static org.junit.Assert.assertEquals

/**
 * Test for job properties
 */
class SetJobPropertiesIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldSetDefaultJobProperties() {
    loadAndExecuteScript("vars/setJobProperties/jobs/setDefaultJobPropertiesTestJob.groovy")
    assertNone(StepConstants.CRON)
    assertNone(StepConstants.UPSTREAM)
    assertNone(StepConstants.POLLSCM)
    assertNone(StepConstants.BOOLEAN_PARAM)
    assertNone(StepConstants.CHOICE)
    assertNone(StepConstants.STRING)
    assertNone(StepConstants.TEXT)
    assertNone(StepConstants.PARAMETERS)

    assertOnce(StepConstants.DISABLE_CONCURRENT_BUILDS)

    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)
    assertEquals("[]", pipelineTriggers.toString())

    Map expectedLogRotatorCall = [artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '30', numToKeepStr: '50']
    Map expectedBuildDiscarderCall = [(LOG_ROTATOR): expectedLogRotatorCall]

    Map actualLogRotatorCall = (Map) assertOnce(LOG_ROTATOR)
    Map actualBuildDiscarderCall = (Map) assertOnce(BUILD_DISCARDER)

    assertEquals(expectedBuildDiscarderCall, actualBuildDiscarderCall)
    assertEquals(expectedLogRotatorCall, actualLogRotatorCall)
  }

  @Test
  void shouldSetCustomJobProperties() {
    loadAndExecuteScript("vars/setJobProperties/jobs/setCustomJobPropertiesTestJob.groovy")

    Map upstreamConfig = assertOnce(StepConstants.UPSTREAM)
    String cronConfig = assertOnce(StepConstants.CRON)
    String pollScmConfig = assertOnce(StepConstants.POLLSCM)

    assertEquals("H/30 * * * *", pollScmConfig)
    assertEquals("H * * * *", cronConfig)
    assertEquals([threshold: 'SUCCESS', upstreamProjects: 'testjob'], upstreamConfig)

    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)

    assertEquals("[pollSCM(H/30 * * * *), cron(H * * * *), upstream([threshold:SUCCESS, upstreamProjects:testjob])]", pipelineTriggers.toString())

    Map actualBooleanParamConfig = (Map) assertOnce(StepConstants.BOOLEAN_PARAM)
    Map actualChoiceParamConfig = (Map) assertOnce(StepConstants.CHOICE)
    Map actualStringParamConfig = (Map) assertOnce(StepConstants.STRING)
    Map actualTextParamConfig = (Map) assertOnce(StepConstants.TEXT)

    Map expectedBooleanParamConfig = [defaultValue: false, description: "", name: "boolparam"]
    Map expectedChoiceParamConfig = [choices: "choice1\nchoice2", description: "", name: "choiceparam"]
    Map expectedStringParamConfig = [defaultValue: "stringvalue", description: "", name: "stringparam"]
    Map expectedTextParamConfig = [defaultValue: '''lorem
ipsum''', description: "", name: "multiline"]

    assertEquals(expectedBooleanParamConfig, actualBooleanParamConfig)
    assertEquals(expectedChoiceParamConfig, actualChoiceParamConfig)
    assertEquals(expectedStringParamConfig, actualStringParamConfig)
    assertEquals(expectedTextParamConfig, actualTextParamConfig)

    List<String> expectedParametersCall = new ArrayList<String>()
    expectedParametersCall.push("booleanParam($expectedBooleanParamConfig)")
    expectedParametersCall.push("choice($expectedChoiceParamConfig)")
    expectedParametersCall.push("string($expectedStringParamConfig)")
    expectedParametersCall.push("text($expectedTextParamConfig)")

    List actualParametersCall = assertOnce(StepConstants.PARAMETERS)
    assertEquals(expectedParametersCall, actualParametersCall)

    Map expectedLogRotatorCall = [artifactDaysToKeepStr: '111', artifactNumToKeepStr: '222', daysToKeepStr: '', numToKeepStr: '']
    Map expectedBuildDiscarderCall = [(LOG_ROTATOR): expectedLogRotatorCall]

    Map actualLogRotatorCall = (Map) assertOnce(LOG_ROTATOR)
    Map actualBuildDiscarderCall = (Map) assertOnce(BUILD_DISCARDER)

    assertEquals(expectedBuildDiscarderCall, actualBuildDiscarderCall)
    assertEquals(expectedLogRotatorCall, actualLogRotatorCall)

    assertNone(StepConstants.DISABLE_CONCURRENT_BUILDS)
  }

}
