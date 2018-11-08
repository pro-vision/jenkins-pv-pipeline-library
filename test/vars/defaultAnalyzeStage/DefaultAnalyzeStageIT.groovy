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
package vars.defaultAnalyzeStage

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.CONFIGFILEPROVIDER
import static io.wcm.testing.jenkins.pipeline.StepConstants.SH
import static io.wcm.testing.jenkins.pipeline.StepConstants.STASH
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static org.junit.Assert.assertEquals

class DefaultAnalyzeStageIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldRunWithDefault() {
    loadAndExecuteScript("vars/defaultAnalyzeStage/jobs/defaultAnalyzeStageDefaultsTestJob.groovy")

    assertOnce(CONFIGFILEPROVIDER)
    String shellCall = (String) assertOnce(SH)
    assertEquals("error in executed shell command", "mvn checkstyle:checkstyle pmd:pmd spotbugs:spotbugs -B -Dcontinuous-integration=true", shellCall)

    assertNone(STASH)
  }

  @Test
  void shouldRunWithCustomConfig() {
    loadAndExecuteScript("vars/defaultAnalyzeStage/jobs/defaultAnalyzeStageCustomTestJob.groovy")

    assertOnce(CONFIGFILEPROVIDER)
    String shellCall = (String) assertOnce(SH)
    assertEquals("error in executed shell command", "mvn -f path/to/custom/analyze/pom.xml customGoal3 customGoal4 -CustomARG2 -DcustomDefine3=false -DcustomDefine4=customValue -Dcontinuous-integration=true", shellCall)

    Map actualStashCall = (Map) assertOnce(STASH)
    assertEquals([name: ConfigConstants.STASH_ANALYZE_FILES, includes: "**/*"], actualStashCall)
  }

}
