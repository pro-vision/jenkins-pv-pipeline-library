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
package vars.featureBranchPreparationStage

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import hudson.AbortException
import io.wcm.testing.jenkins.pipeline.StepConstants
import io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert
import io.wcm.devops.jenkins.pipeline.environment.EnvironmentConstants
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class FeatureBranchPreparationStageIT extends PVLibraryIntegrationTestBase {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none()

  @Override
  void setUp() throws Exception {
    super.setUp()
    this.setEnv(EnvironmentConstants.GIT_BRANCH, 'feature/test')
  }

  @Test
  void shouldNotRunForNonFeatureBranchJobs() {
    loadAndExecuteScript("vars/featureBranchPreparationStage/jobs/nonFeatureBranchTestJob.groovy")
    StepRecorderAssert.assertNone(StepConstants.STAGE)
    StepRecorderAssert.assertNone(StepConstants.SH)
  }

  @Test
  void shouldIntegrateFeatureBranchWithMaster() {
    loadAndExecuteScript("vars/featureBranchPreparationStage/jobs/featureBranchTestJob.groovy")

    StepRecorderAssert.assertOnce(StepConstants.STAGE)
    String mergeCall = StepRecorderAssert.assertOnce(StepConstants.SH)
    Assert.assertEquals("git merge origin/master", mergeCall)
  }

  @Test
  void shouldFailOnFeatureBranchIntegration() {
    expectedEx.expect(AbortException.class)
    expectedEx.expectMessage("The branch 'feature/test' is not suitable for integration into master")

    helper.registerAllowedMethod(StepConstants.SH, [String.class], { String command ->
      throw new Exception("git merge exception")
    })

    loadAndExecuteScript("vars/featureBranchPreparationStage/jobs/featureBranchTestJob.groovy")

    StepRecorderAssert.assertOnce(StepConstants.STAGE)
    String mergeCall = StepRecorderAssert.assertOnce(StepConstants.SH)
    Assert.assertEquals("git merge origin/master", mergeCall)
  }
}
