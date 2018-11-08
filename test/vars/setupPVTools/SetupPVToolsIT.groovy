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
package vars.setupPVTools

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.TOOL
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertTwice
import static org.junit.Assert.assertEquals

class SetupPVToolsIT extends PVLibraryIntegrationTestBase {


  @Test
  void shouldUseDefaultTools() {
    loadAndExecuteScript("vars/setupPVTools/jobs/setupPVToolsDefaultTestJob.groovy")
    List actualToolCalls = assertTwice(TOOL)
    List expectedToolCalls = ["defaultMaven", "defaultJDK"]

    assertEquals("Tool call mismatch", expectedToolCalls, actualToolCalls)

  }

  @Test
  void shouldUseCustomTools() {
    loadAndExecuteScript("vars/setupPVTools/jobs/setupPVToolsCustomTestJob.groovy")
    List actualToolCalls = assertTwice(TOOL)
    List expectedToolCalls = ["customMaven", "customJDK"]

    assertEquals("Tool call mismatch", expectedToolCalls, actualToolCalls)
  }
}
