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
package vars.defaults

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.POLLSCM
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertTwice
import static org.junit.Assert.assertEquals

class DefaultsIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldCreateOneScmPollingTrigger() {
    loadAndExecuteScript("vars/defaults/jobs/defaultsTestJob.groovy")
    Object actualScmPollingCall = assertOnce(POLLSCM)
    assertEquals("defaultScmPolling", actualScmPollingCall)
  }

  @Test
  void shouldCreateMultipleScmPollingTriggers() {
    this.getContext().getDslMock().mockResource("pv-pipeline-library/config/config.yaml", "pv-pipeline-library/config/config-multiple-polling.yaml")
    loadAndExecuteScript("vars/defaults/jobs/defaultsTestJob.groovy")
    List expectedScmPollingCalls = [
      "defaultScmPolling1",
      "defaultScmPolling2"
    ]
    List actualScmPollingCalls = assertTwice(POLLSCM)
    assertEquals(expectedScmPollingCalls, actualScmPollingCalls)
  }
}
