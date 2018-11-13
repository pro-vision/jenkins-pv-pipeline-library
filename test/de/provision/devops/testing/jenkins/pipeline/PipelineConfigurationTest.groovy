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
    Assert.assertEquals("defaultJDK", underTest.getJdk())
    Assert.assertEquals("defaultMaven", underTest.getMaven())
    Assert.assertEquals("defaultNodeJS", underTest.getNodeJs())
    List expectedScmPollingConfig = [
      "defaultScmPolling"
    ]
    Assert.assertEquals(expectedScmPollingConfig, underTest.getDefaultSCMPolling())
  }

  @Test
  void shouldReturnCorrectJdk() {
    underTest = new PipelineConfiguration(this.script,"pv-pipeline-library/config/config-multiple-tools.yaml")
    Assert.assertEquals("sun-java8-jdk", underTest.getJdk("8"))
    Assert.assertEquals("sun-java7-jdk", underTest.getJdk("jdk_7"))
    Assert.assertEquals("sun-java8-192-jdk", underTest.getJdk("1.8.0_192"))
  }

  @Test
  void shouldReturnCorrectMaven() {
    underTest = new PipelineConfiguration(this.script,"pv-pipeline-library/config/config-multiple-tools.yaml")
    Assert.assertEquals("apache-maven-3.6.0", underTest.getMaven("3.6.0"))
    Assert.assertEquals("apache-maven-3.5.4", underTest.getMaven("3_5_4"))
    Assert.assertEquals("maven-test1", underTest.getMaven("11__22__3_"))
    Assert.assertEquals("maven-test2", underTest.getMaven("_11___22_3__4"))
    Assert.assertEquals("maven-test3", underTest.getMaven("11__22__a_"))
  }

  @Test
  void shouldReturnCorrectNodeJs() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/config-multiple-tools.yaml")
    Assert.assertEquals("nodejs-11.1.0", underTest.getNodeJs("11.1.0"))
    Assert.assertEquals("nodejs-10.13.0", underTest.getNodeJs("10_13_0"))
  }

  @Test
  void shouldReturnMultipleScmPollingConfigs() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/config-multiple-polling.yaml")
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
    underTest.getJdk()
  }

  @Test(expected = AbortException.class)
  void shouldAbortOnMissingMavenConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/invalid-config.yaml")
    underTest.getMaven()
  }

  @Test(expected = AbortException.class)
  void shouldAbortOnMissingNodsJsConfig() {
    underTest = new PipelineConfiguration(this.script, "pv-pipeline-library/config/invalid-config.yaml")
    underTest.getNodeJs()
  }
}
