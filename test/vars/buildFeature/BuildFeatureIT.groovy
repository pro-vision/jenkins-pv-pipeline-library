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
package vars.buildFeature


import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import de.provision.devops.testing.jenkins.pipeline.PVStepConstants

import static io.wcm.testing.jenkins.pipeline.StepConstants.ANSI_COLOR
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.SH
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertStepCalls
import static org.junit.Assert.*

class BuildFeatureIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldCallBuildDefaultStepWithoutScmConfig() {
    loadAndExecuteScript("vars/buildFeature/jobs/buildFeatureWithScmConfigTestJob.groovy")
    Map config = assertOnce(PVStepConstants.BUILD_DEFAULT)

    // color wrapping
    assertOnce(ANSI_COLOR)

    // check scm config
    Map scmConfig = (Map) config.scm ?: null
    assertNotNull("config for commit step should contain 'scm' key", scmConfig)
    assertNull("scm config should not contain 'useScmVar'", scmConfig[SCM_USE_SCM_VAR])

    assertEquals(JOB_TYPE_FEATURE, config.get(JOB_TYPE))

    // check build config
    Map compileStageCfg = (Map) config[STAGE_COMPILE]
    assertNotNull("config should contain a config for compile stage", compileStageCfg)
    assertEquals("[maven:[goals:[clean, install], mapMergeMode:REPLACE]]", compileStageCfg.toString())

    // check build config
    Map analyzeStageCfg = (Map) config[STAGE_ANALYZE]
    assertNull("config should not contain a config for analyze stage", analyzeStageCfg)
  }

  @Test
  void shouldCallDefaultBuildWithUseSCMVar() {
    loadAndExecuteScript("vars/buildFeature/jobs/buildFeatureWithScmVarTestJob.groovy")
    Map config = assertOnce(PVStepConstants.BUILD_DEFAULT)

    // color wrapping
    assertOnce(ANSI_COLOR)

    // check scm config
    Map scmConfig = (Map) config.scm ?: null
    assertNotNull("config for commit step should contain 'scm' key", scmConfig)
    assertTrue("scm config should contain 'useScmVar' with value true when access was rejected by sandbox", (Boolean) scmConfig[SCM_USE_SCM_VAR] ?: false)

    assertEquals(JOB_TYPE_FEATURE, config.get(JOB_TYPE))

    // check build config
    Map compileStageCfg = (Map) config[STAGE_COMPILE]
    assertNotNull("config should contain a config for compile stage", compileStageCfg)
    assertEquals("[maven:[goals:[clean, install], mapMergeMode:REPLACE]]", compileStageCfg.toString())

    // check build config
    Map analyzeStageCfg = (Map) config[STAGE_ANALYZE]
    assertNull("config should not contain a config for analyze stage", analyzeStageCfg)
  }

  @Test
  void shouldRunWithCustomConfig() {
    loadAndExecuteScript("vars/buildFeature/jobs/buildFeatureCustomTestJob.groovy")
    Map config = assertOnce(PVStepConstants.BUILD_DEFAULT)

    // color wrapping
    assertOnce(ANSI_COLOR)

    // check scm config
    Map scmConfig = (Map) config.scm ?: null
    assertNotNull("config for commit step should contain 'scm' key", scmConfig)
    assertTrue("scm config should contain 'useScmVar' with value true when access was rejected by sandbox", (Boolean) scmConfig[SCM_USE_SCM_VAR] ?: false)

    assertEquals(JOB_TYPE_FEATURE, config.get(JOB_TYPE))

    // check build config
    Map compileStageCfg = (Map) config[STAGE_COMPILE]
    assertNotNull("config should contain a config for compile stage", compileStageCfg)
    assertEquals("[maven:[goals:[goal1, goal2], mapMergeMode:REPLACE, defines:[customDefine:value]]]", compileStageCfg.toString())

    // check build config
    Map analyzeStageCfg = (Map) config[STAGE_ANALYZE]
    assertNull("config should not contain a config for analyze stage", analyzeStageCfg)
  }

  @Test
  void shouldCallDefaultBuildWithExtend() {
    loadAndExecuteScript("vars/buildFeature/jobs/buildFeatureWithExtendTestJob.groovy")
    List shCalls = assertStepCalls(SH, 2)
    assertOnce(PVStepConstants.BUILD_DEFAULT)

    // color wrapping
    assertOnce(ANSI_COLOR)
  }

  // register callbacks to track calls
  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()

    // catch buildDefault step calls
    this.helper.registerAllowedMethod(PVStepConstants.BUILD_DEFAULT, [Map.class], {
      config ->
        this.stepRecorder.record(PVStepConstants.BUILD_DEFAULT, config)
    })
    this.helper.registerAllowedMethod(PVStepConstants.BUILD_DEFAULT, [], {
      this.stepRecorder.record(PVStepConstants.BUILD_DEFAULT, null)
    })
  }

}
