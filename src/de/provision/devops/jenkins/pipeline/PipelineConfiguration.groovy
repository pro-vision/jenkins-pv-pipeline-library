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
package de.provision.devops.jenkins.pipeline

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.devops.jenkins.pipeline.utils.resources.YamlLibraryResource
import org.jenkinsci.plugins.workflow.cps.DSL

class PipelineConfiguration implements Serializable {

  private static final long serialVersionUID = 1L

  static final String CONFIG_PATH = "pv-pipeline-library/config/config.yaml"

  Map cfg = null

  //Serializable CpsScript is passed during runtime
  @SuppressFBWarnings("SE_BAD_FIELD")
  Script script = null

  String configPath = null

  Logger log = new Logger(this)

  PipelineConfiguration(Script script, String configPath = PipelineConfiguration.CONFIG_PATH) {
    this.script = script
    this.configPath = configPath
  }

  Object getConfig() {
    if (this.cfg == null) {
      YamlLibraryResource configResource = new YamlLibraryResource((DSL) this.script.steps, this.configPath)
      this.cfg = configResource.load()
    }
    return this.cfg
  }

  String getDefaultJdk() {
    Object ret = null
    try {
      ret = this.getConfig().tool.jdk.default
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.jdk.default")
    }
    return ret
  }

  String getDefaultMaven() {
    Object ret
    try {
      ret = this.getConfig().tool.maven.default
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.maven.default")
    }
    return ret
  }

  String getDefaultNodeJs() {
    Object ret
    try {
      ret = this.getConfig().tool.nodejs.default
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.nodejs.default")
    }
    return ret
  }

  List getDefaultSCMPolling() {
    Object ret
    try {
      ret = this.getConfig().jobs.default.properties.pipelineTriggers.pollSCM
    } catch (NullPointerException ex) {
      ret = null
    }
    return ret
  }

  String failOnMissingConfig(String id) {
    log.error("Unable to retrieve '$id' from config. Make sure that a valid config is present!")
    script.error("Unable to retrieve '$id' from config. Make sure that a valid config is present!")
  }
}
