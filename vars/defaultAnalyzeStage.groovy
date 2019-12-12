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
import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import io.wcm.devops.jenkins.pipeline.utils.TypeUtils
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * Default build step for analyzing maven based projects.
 * This step runs maven with
 * - checkstyle
 * - pmd
 * - findbugs
 *
 * @param config Configuration options for the used steps
 *
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md
 */
void call(Map config) {
  Logger log = new Logger("defaultAnalyzeStage")
  TypeUtils typeUtils = new TypeUtils()
  Map analyzeStageConfig = (Map) config[STAGE_ANALYZE] ?: [:]
  Boolean analyzeStageEnabled = analyzeStageConfig[STAGE_ANALYZE_ENABLED] != null ? analyzeStageConfig[STAGE_ANALYZE_ENABLED] : true

  if (!analyzeStageEnabled) {
    log.debug("defaultAnalyzeStage is disabled")
    return
  }

  def _extend = typeUtils.isClosure(analyzeStageConfig[STAGE_ANALYZE_EXTEND]) ? analyzeStageConfig[STAGE_ANALYZE_EXTEND] : null

  stage('Analyze') {
    // no extends are configured, call the implementation
    if (!_extend) {
      log.debug("no extend configured, using default implementation")
      _impl(config)
    } else {
      // call extend and provide a reference to the default implementation
      _extend(config, this.&_impl)
    }
  }
}

/**
 * Implementation of the analyze stage
 */
void _impl(Map config) {
  Logger log = new Logger("defaultAnalyzeStage._impl")

  // get maven defines based on project structure (e.g. nodejs.directory)
  def defaultMavenDefines = getDefaultMavenDefines()
  // merge config with maven defaults and incoming stage configuration
  Map analyzeStageConfig = (Map) config[STAGE_ANALYZE] ?: [:]
  Map analyzeDefaultCgf = [
    (MAVEN): [
      (MAVEN_GOALS)    : ["checkstyle:checkstyle", "pmd:pmd", "spotbugs:spotbugs"],
      (MAVEN_ARGUMENTS): ["-B"]
    ]
  ]
  analyzeStageConfig = MapUtils.merge(config, analyzeDefaultCgf, analyzeStageConfig, defaultMavenDefines)
  // execute maven build with the configuration
  execMaven(analyzeStageConfig)

  // check if we have to stash the files for later use
  if (analyzeStageConfig[STASH]) {
    log.info("stashing compile files for later usage with name: '${STASH_ANALYZE_FILES}'")
    // stash the files for later usage
    stash(name: STASH_ANALYZE_FILES, includes: "**/*")
  }
}