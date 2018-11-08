/*-
 * #%L
 * wcm.io
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
package vars.routeDefaultJenkinsFile

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import de.provision.devops.testing.jenkins.pipeline.PVStepConstants
import hudson.AbortException
import io.wcm.testing.jenkins.pipeline.RunWrapperMock
import io.wcm.testing.jenkins.pipeline.StepConstants
import io.wcm.devops.jenkins.pipeline.environment.EnvironmentConstants
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce

class RouteDefaultJenkinsFileIT extends PVLibraryIntegrationTestBase {

  @Override
  void setUp() throws Exception {
    super.setUp()

  }

  @Test
  void shouldCallDefaultBuildStep() {
    super.binding.setVariable('scm', true)
    loadAndExecuteScript("vars/routeDefaultJenkinsFile/jobs/routeDefaultJenkinsFileJob.groovy")
    assertNone(PVStepConstants.BUILD_FEATURE)
    assertOnce(PVStepConstants.BUILD_DEFAULT)
    assertOnce(StepConstants.ANSI_COLOR)
  }

  @Test
  void shouldCallDefaultFeatureStep() {
    super.binding.setVariable('scm', true)
    this.setEnv(EnvironmentConstants.BRANCH_NAME, "feature/i-am-correct")
    this.runWrapper = new RunWrapperMock(null)
    this.binding.setVariable("currentBuild", runWrapper)
    loadAndExecuteScript("vars/routeDefaultJenkinsFile/jobs/routeDefaultJenkinsFileJob.groovy")
    assertOnce(PVStepConstants.BUILD_FEATURE)
    assertNone(PVStepConstants.BUILD_DEFAULT)
    assertOnce(StepConstants.ANSI_COLOR)
  }

  @Test(expected = AbortException)
  void shouldFailOnRoutingError() {
    super.binding.setVariable('scm', null)
    loadAndExecuteScript("vars/routeDefaultJenkinsFile/jobs/routeDefaultJenkinsFileJob.groovy")
  }

  // register callbacks to track calls
  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()

    // catch buildFeature calls
    this.helper.registerAllowedMethod(PVStepConstants.BUILD_FEATURE, [Map.class], {
      config ->
        this.stepRecorder.record(PVStepConstants.BUILD_FEATURE, config)
    })
    this.helper.registerAllowedMethod(PVStepConstants.BUILD_FEATURE, [], {
      this.stepRecorder.record(PVStepConstants.BUILD_FEATURE, null)
    })

    this.helper.registerAllowedMethod(PVStepConstants.BUILD_DEFAULT, [Map.class], {
      config ->
        this.stepRecorder.record(PVStepConstants.BUILD_DEFAULT, config)
    })
    this.helper.registerAllowedMethod(PVStepConstants.BUILD_DEFAULT, [], {
      this.stepRecorder.record(PVStepConstants.BUILD_DEFAULT, null)
    })
  }
}
