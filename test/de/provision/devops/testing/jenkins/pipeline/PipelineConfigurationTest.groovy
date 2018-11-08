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
package de.provision.devops.testing.jenkins.pipeline

import de.provision.devops.jenkins.pipeline.PipelineConfiguration
import hudson.AbortException
import io.wcm.testing.jenkins.pipeline.CpsScriptTestBase
import org.junit.Assert
import org.junit.Test

class PipelineConfigurationTest extends CpsScriptTestBase {

  PipelineConfiguration underTest

  @Test
  void shouldLoadConfig() {
    underTest = new PipelineConfiguration(this.script)
    Map expected = [
      tool: [
        maven : [
          default: "defaultMaven"
        ],
        jdk   : [
          default: "defaultJDK"
        ],
        nodejs: [
          default: "defaultNodeJS"
        ],
      ],
      jobs: [
        default: [
          properties: [
            pipelineTriggers: [
              pollSCM: ["defaultScmPolling"]
            ]
          ]
        ]
      ]
    ]
    Map actual = underTest.getConfig()
    Assert.assertEquals(expected, actual)
  }

  @Test
  void shouldReturnDefaults() {
    underTest = new PipelineConfiguration(this.script)
    Assert.assertEquals("defaultJDK", underTest.getDefaultJdk())
    Assert.assertEquals("defaultMaven", underTest.getDefaultMaven())
    Assert.assertEquals("defaultNodeJS", underTest.getDefaultNodeJs())
    List expectedScmPollingConfig = [
      "defaultScmPolling"
    ]
    Assert.assertEquals(expectedScmPollingConfig, underTest.getDefaultSCMPolling())
  }

  @Test(expected = AbortException.class)
  void shouldReturnMultipleScmPollingConfigs() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/multiple-polling.yaml")
    List expectedScmPollingConfig = [
      "defaultScmPolling1",
      "defaultScmPolling2"
    ]
    Assert.assertEquals(expectedScmPollingConfig, underTest.getDefaultSCMPolling())
  }

  @Test(expected = AbortException.class)
  void shouldFailOnNotExistingConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/not-existing.yaml")
    underTest.getConfig()
  }

  @Test(expected = AbortException.class)
  void shouldAbortOnMissingJdkConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/invalid-config.yaml")
    underTest.getDefaultJdk()
  }

  @Test(expected = AbortException.class)
  void shouldAbortOnMissingMavenConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/invalid-config.yaml")
    underTest.getDefaultMaven()
  }

  @Test(expected = AbortException.class)
  void shouldAbortOnMissingNodsJsConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/invalid-config.yaml")
    underTest.getDefaultNodeJs()
  }
}
