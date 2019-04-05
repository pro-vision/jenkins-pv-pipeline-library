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
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger

/**
 * This step sets the default job properties for all jobs using the pipeline script.
 * The defaults are
 * - no automatic rebuild
 * - rebuild is enabled
 * - poll scm every 15 minutes on each day
 * - keep builds maximum 365 days, but 50 builds max
 * - disable concurrent builds
 *
 * @see <a href="../vars/defaultBuildWrapper.groovy">defaultBuildWrapper.groovy</a>
 * @see <a href="../vars/defaultBuildWrapper.md">defaultBuildWrapper.md</a>
 */
void call(Map config = [:]) {

  Logger log = new Logger("setJobProperties")


  Map propertiesCfg = config[PROPERTIES] ?: [:]

  log.debug("properties config: $propertiesCfg")

  List<Map> triggersCfg = propertiesCfg[PROPERTIES_PIPELINE_TRIGGERS] ?: []
  List parametersCfg = propertiesCfg[PROPERTIES_PARAMETERS] ?: []
  List customPropertiesCfg = propertiesCfg[PROPERTIES_CUSTOM] ?: []

  Map buildDiscarderCfg = propertiesCfg[PROPERTIES_BUILD_DISCARDER] ?: [:]
  String artifactDaysToKeep = buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP] != null ? buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP] : ''
  String artifactNumToKeep = buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP] != null ? buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP] : ''
  String daysToKeep = buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP] != null ? buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP] : '365'
  String numToKeep = buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP] != null ? buildDiscarderCfg[PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP] : '50'

  Boolean disableConcurrentBuildsCfg = propertiesCfg[PROPERTIES_DISABLE_CONCURRENT_BUILDS] != null ? propertiesCfg[PROPERTIES_DISABLE_CONCURRENT_BUILDS] : true

  List jobProperties = [
    [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
    pipelineTriggers(triggersCfg),
    buildDiscarder(logRotator(artifactDaysToKeepStr: artifactDaysToKeep, artifactNumToKeepStr: artifactNumToKeep, daysToKeepStr: daysToKeep, numToKeepStr: numToKeep)),
  ]



  if (disableConcurrentBuildsCfg == true) {
    jobProperties.push(disableConcurrentBuilds())
  }

  // only add parameters when set, otherwise you have to click on build twice
  if (parametersCfg.size() > 0) {
    jobProperties.push(parameters(parametersCfg))
  }

  for (customProperty in customPropertiesCfg) {
    jobProperties.push(customProperty)
  }



  properties(jobProperties)
}
