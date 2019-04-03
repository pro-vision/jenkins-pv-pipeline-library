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
package vars.defaultCompileStage

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.*
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static org.junit.Assert.assertEquals

class DefaultCompileStageIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldRunWithDefault() {
    loadAndExecuteScript("vars/defaultCompileStage/jobs/defaultCompileStageDefaultsTestJob.groovy")

    assertOnce(CONFIGFILEPROVIDER)
    String shellCall = (String) assertOnce(SH)
    assertEquals("error in executed shell command", "mvn clean deploy -B -U -Dcontinuous-integration=true", shellCall)

    assertOnce(FINGERPRINT)

    assertNone(STASH)
  }

  @Test
  void shouldRunWithCustomConfig() {
    loadAndExecuteScript("vars/defaultCompileStage/jobs/defaultCompileStageCustomTestJob.groovy")

    assertOnce(CONFIGFILEPROVIDER)
    String shellCall = (String) assertOnce(SH)
    assertEquals("error in executed shell command", "mvn -f path/to/custom/pom.xml customGoal1 customGoal2 -CustomARG1 -DcustomDefine1=true -DcustomDefine2=value -Dcontinuous-integration=true", shellCall)

    assertOnce(FINGERPRINT)

    Map actualStashCall = (Map) assertOnce(STASH)
    assertEquals([name: ConfigConstants.STASH_COMPILE_FILES, includes: "**/*"], actualStashCall)
  }

}
