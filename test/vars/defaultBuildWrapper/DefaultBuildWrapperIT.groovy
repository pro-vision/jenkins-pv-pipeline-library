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
package vars.defaultBuildWrapper


import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.testing.jenkins.pipeline.StepConstants
import org.junit.Test

import java.util.concurrent.TimeUnit

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.ANSI_COLOR_GNOME_TERMINAL
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.ANSI_COLOR_XTERM
import static io.wcm.testing.jenkins.pipeline.StepConstants.ANSI_COLOR
import static io.wcm.testing.jenkins.pipeline.StepConstants.TOOL
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.*
import static org.junit.Assert.assertArrayEquals
import static org.junit.Assert.assertEquals

class DefaultBuildWrapperIT extends PVLibraryIntegrationTestBase {


  @Test
  void shouldRunWithProvidedConfig() {
    loadAndExecuteScript("vars/defaultBuildWrapper/jobs/defaultBuildWrapperConfigTestJob.groovy")
    assertNone(StepConstants.POLLSCM)
    assertNone(StepConstants.CRON)
    assertNone(StepConstants.UPSTREAM)
    Map timeoutCall = assertOnce(StepConstants.TIMEOUT)
    String ansiColorCall = assertOnce(ANSI_COLOR)
    List toolCalls = assertTwice(TOOL)
    assertArrayEquals("error in executed tool commands", ["customMaven", "customJDK"].toArray(), toolCalls.toArray())
    assertEquals(10, timeoutCall.time)
    assertEquals(TimeUnit.HOURS, timeoutCall.unit)
    assertEquals(ANSI_COLOR_GNOME_TERMINAL, ansiColorCall)
    // assert that loglevel was not changed again by defaultBuildWrapper
    assertEquals(LogLevel.DEBUG, Logger.getLevel())
    assertOnce("mail")
    assertOnce("mattermost")
    assertOnce("mqtt")
  }

  @Test
  void shouldRunWithClosureOnly() {
    loadAndExecuteScript("vars/defaultBuildWrapper/jobs/defaultBuildWrapperClosureTestJob.groovy")
    assertNone(StepConstants.POLLSCM)
    assertNone(StepConstants.CRON)
    assertNone(StepConstants.UPSTREAM)
    Map timeoutCall = assertOnce(StepConstants.TIMEOUT)
    String ansiColorCall = assertOnce(ANSI_COLOR)
    List toolCalls = assertTwice(TOOL)
    assertArrayEquals("error in executed tool commands", ["defaultMaven", "defaultJDK"].toArray(), toolCalls.toArray())
    assertEquals(30, timeoutCall.time)
    assertEquals(TimeUnit.MINUTES, timeoutCall.unit)
    assertEquals(ANSI_COLOR_XTERM, ansiColorCall)
    assertOnce("mail")
    assertOnce("mattermost")
    assertOnce("mqtt")
  }

  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()

    // mock notify calls
    helper.registerAllowedMethod("mail", [Map.class], { Map config ->
      stepRecorder.record("mail", config)
    })
    helper.registerAllowedMethod("mattermost", [Map.class], { Map config ->
      stepRecorder.record("mattermost", config)
    })
    helper.registerAllowedMethod("mqtt", [Map.class], { Map config ->
      stepRecorder.record("mqtt", config)
    })
  }
}
