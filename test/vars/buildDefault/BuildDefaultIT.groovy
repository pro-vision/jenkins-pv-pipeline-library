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
package vars.buildDefault

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.jenkinsci.plugins.pipeline.utility.steps.fs.FileWrapper
import org.junit.Test

import static de.provision.devops.testing.jenkins.pipeline.PVStepConstants.PURGE_SNAPSHOTS_FROM_REPOSITORY
import static io.wcm.testing.jenkins.pipeline.StepConstants.*
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.*
import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertEquals

class BuildDefaultIT extends PVLibraryIntegrationTestBase {

  @Override
  void setUp() throws Exception {
    super.setUp()
    this.setEnv("BUILD_NUMBER", "1")
  }

  @Test
  void shouldRunWithNodeJSDefine() {
    helper.registerAllowedMethod(FIND_FILES, [Map.class], {
      FileWrapper[] res = new FileWrapper[1]
      res[0] = new FileWrapper('', 'package.json', false, 0, 0)
      return res
    })

    loadAndExecuteScript("vars/buildDefault/jobs/buildDefaultTestJob.groovy")

    String nodeStep = (String) assertOnce(NODE)
    assertEquals("expected no custom node expression for agent allocation", null, nodeStep)

    // trigger assertions
    String pollScmCall = assertOnce(POLLSCM)
    assertEquals("defaultScmPolling", pollScmCall)
    assertNone(CRON)
    assertNone(UPSTREAM)
    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)
    assertEquals("[pollSCM(defaultScmPolling)]", pipelineTriggers.toString())

    // color wrapping
    assertOnce(ANSI_COLOR)

    // parameter assertions
    assertNone(PARAMETERS)

    // assert four step calls for non feature branch builds
    assertStepCalls(STAGE, 4)

    assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertTwice(CONFIGFILEPROVIDER)
    List shellCalls = assertStepCalls(SH, 4)

    assertAnalyzeSteps()

    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())
    assertArrayEquals("error in executed shell commands", [
      [returnStdout: true, script: "git branch"],
      [returnStdout: true, script: "git rev-parse HEAD"],
      'mvn clean deploy -B -U -Dcontinuous-integration=true -Dnodejs.directory=${WORKSPACE}/target/.nodejs',
      'mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs -B -Dcontinuous-integration=true -Dnodejs.directory=${WORKSPACE}/target/.nodejs'
    ].toArray(), shellCalls.toArray())

    assertJobStatusSuccess()
  }

  @Test
  void shouldRunWithDefaults() {
    loadAndExecuteScript("vars/buildDefault/jobs/buildDefaultTestJob.groovy")

    // trigger assertions
    String pollScmCall = assertOnce(POLLSCM)
    assertEquals("defaultScmPolling", pollScmCall)
    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)
    assertEquals("[pollSCM(defaultScmPolling)]", pipelineTriggers.toString())
    assertNone(CRON)
    assertNone(UPSTREAM)

    // color wrapping
    assertOnce(ANSI_COLOR)
    // parameter assertions
    assertNone(PARAMETERS)

    // assert four step calls for non feature branch builds
    assertStepCalls(STAGE, 4)

    assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)

    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertTwice(CONFIGFILEPROVIDER)
    List shellCalls = assertStepCalls(SH, 4)

    assertAnalyzeSteps()

    assertNone(EMAILEXT)

    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())
    assertArrayEquals("error in executed shell commands", [
      [returnStdout: true, script: "git branch"],
      [returnStdout: true, script: "git rev-parse HEAD"],
      "mvn clean deploy -B -U -Dcontinuous-integration=true",
      "mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs -B -Dcontinuous-integration=true"].toArray(),
      shellCalls.toArray()
    )

    assertJobStatusSuccess()
  }

  @Test
  void shouldRunWithCustomValues() {
    loadAndExecuteScript("vars/buildDefault/jobs/buildDefaultCustomTestJob.groovy")

    String nodeStep = (String) assertOnce(NODE)
    assertEquals("expected a custom node expression for agent allocation", "custom && node && expression", nodeStep)

    // trigger assertions
    assertNone(POLLSCM)
    assertNone(UPSTREAM)
    String cronCall = assertOnce(CRON)
    assertEquals("H * * * *", cronCall)
    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)
    assertEquals("[cron(H * * * *)]", pipelineTriggers.toString())

    // color wrapping
    assertOnce(ANSI_COLOR)

    // parameter assertions
    assertNone(PARAMETERS)

    // assert four step calls for non feature branch builds
    assertStepCalls(STAGE, 4)

    assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertTwice(CONFIGFILEPROVIDER)
    List shellCalls = assertStepCalls(SH, 4)

    assertAnalyzeSteps()

    assertNone(EMAILEXT)

    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())
    assertArrayEquals("error in executed shell commands", [
      [returnStdout: true, script: "git branch"],
      [returnStdout: true, script: "git rev-parse HEAD"],
      "mvn compileGoal1 compileGoal2 -B -U -Dcontinuous-integration=true",
      "mvn analyzeGoal1 analyzeGoal2 -B -Dcontinuous-integration=true"].toArray(),
      shellCalls.toArray()
    )

    assertJobStatusSuccess()
  }

  @Test
  void shouldRunWithFeaturePreparation() {
    loadAndExecuteScript("vars/buildDefault/jobs/buildFeatureTestJob.groovy")

    // trigger assertions
    String pollScmCall = assertOnce(POLLSCM)
    assertEquals("defaultScmPolling", pollScmCall)
    List pipelineTriggers = (List) assertOnce(PIPELINE_TRIGGERS)
    assertEquals("[pollSCM(defaultScmPolling)]", pipelineTriggers.toString())
    assertNone(CRON)
    assertNone(UPSTREAM)

    Map steps = this.stepRecorder.getRecordedSteps()

    // color wrapping
    assertOnce(ANSI_COLOR)

    // parameter assertions
    assertNone(PARAMETERS)

    // assert four step calls for non feature branch builds
    assertStepCalls(STAGE, 5)

    assertOnce(CHECKOUT)
    List toolCalls = assertTwice(TOOL)
    assertOnce(MAVEN_PURGE_SNAPSHOTS)
    assertTwice(CONFIGFILEPROVIDER)
    List shellCalls = assertStepCalls(SH, 5)

    assertAnalyzeSteps()

    assertNone(EMAILEXT)

    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())
    assertArrayEquals("error in executed shell commands", [
      [returnStdout: true, script: "git branch"],
      [returnStdout: true, script: "git rev-parse HEAD"],
      "git merge origin/develop",
      "mvn clean deploy -B -U -Dcontinuous-integration=true",
      "mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs -B -Dcontinuous-integration=true"].toArray(),
      shellCalls.toArray()
    )

    assertJobStatusSuccess()
  }

  protected assertAnalyzeSteps() {
    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
    assertOnce(CHECKSTYLE)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()
    helper.registerAllowedMethod(PURGE_SNAPSHOTS_FROM_REPOSITORY, [], {
      stepRecorder.record(PURGE_SNAPSHOTS_FROM_REPOSITORY, null)
    })
    // mock gitTools.getParentBranch call
    helper.registerAllowedMethod("getParentBranch", [], { Closure closure ->
      stepRecorder.record("getParentBranch", null)
      return "origin/develop"
    })

    helper.registerAllowedMethod(NODE, [String.class, Closure.class], { String agent, Closure closure ->
      stepRecorder.record(NODE, agent)
      // execute the closure
      closure.call()
    })
  }
}
