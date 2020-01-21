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

/**
 * Configuration class for the pro!vision pipeline library
 */
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

  /**
   * Loads and returns the configuration
   *
   * @return The loaded yaml configuration as mal
   */
  Map getConfig() {
    if (this.cfg == null) {
      YamlLibraryResource configResource = new YamlLibraryResource(this.script, this.configPath)
      this.cfg = configResource.load()
    }
    return this.cfg
  }

  /**
   * Retrieves the name of JDK installation from the configuration.
   * When no key is provided the default name is returned
   *
   * @param key The key of the JDK tool to retrieve the tool name for
   * @return The name of the JDK tool
   */
  String getJdk(String key = "default") {
    Object ret = null
    try {
      ret = _getEntryByKey((Map) this.getConfig().tool.jdk, key)
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.jdk.$key")
    }
    return ret
  }

  /**
   * Retrieves the name of Maven installation from the configuration.
   * When no key is provided the default name is returned.
   *
   * @param key The key of the Maven tool to retrieve the tool name for
   * @return The name of the Maven tool
   */
  String getMaven(String key = "default") {
    Object ret
    try {
      ret = _getEntryByKey((Map) this.getConfig().tool.maven, key)
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.maven.$key")
    }
    return ret
  }

  /**
   * Retrieves the name of NodeJS installation from the configuration.
   * When no key is provided the default name is returned.
   *
   * @param key The key of the NodeJS tool to retrieve the tool name for
   * @return The name of the NodeJS tool
   */
  String getNodeJs(String key = "default") {
    Object ret
    try {
      ret = _getEntryByKey((Map) this.getConfig().tool.nodejs, key)
    } catch (NullPointerException ex) {
      failOnMissingConfig("jenkins.tools.nodejs.$key")
    }
    return ret
  }

  /**
   * @return The default scm polling configuration
   */
  List getDefaultSCMPolling() {
    Object ret
    try {
      ret = this.getConfig().jobs.default.properties.pipelineTriggers.pollSCM
    } catch (NullPointerException ex) {
      ret = null
    }
    return ret
  }

  /**
   * Utility function to retrieve a tool by its key from the configuration
   *
   * @param config The configuration to retrieve the item with the matching key from
   * @param key The key to search for
   * @return The found configuration entry
   */
  private String _getEntryByKey(Map config, String key) {
    // apply fix for names like 3_5_4, the underscores are getting stripped
    if (key =~ /^\d[\d_]+$/) {
      key = key.replace("_","")
    }
    String ret = null
    for (entry in config) {
      if ("${entry.key}" == "$key") {
        ret = entry.value
        break
      }
    }
    return ret
  }

  /**
   * Utility function to mark the configuration retrieve process as failed.
   *
   * @param id The id of the configuration to fail for.
   */
  void failOnMissingConfig(String id) {
    log.error("Unable to retrieve '$id' from config. Make sure that a valid config is present!")
    script.error("Unable to retrieve '$id' from config. Make sure that a valid config is present!")
  }
}
