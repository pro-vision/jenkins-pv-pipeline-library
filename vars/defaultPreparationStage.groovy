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


import com.kenai.jffi.Closure
import io.wcm.devops.jenkins.pipeline.utils.TypeUtils
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger

import java.util.function.Function

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * Default build step for preparing the build for maven based projects.
 * This step takes care of:
 * - setting up JDK and maven
 * - check out the scm
 * - set the build name (e.g. #1_origin/develop)
 * - delete project artifacts from local repository
 *
 * @param config Configuration options for the used steps
 *
 * @see ../vars/setupPVTools.groovy
 * @see ../vars/setupPVTools.md
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.md
 */
void call(Map config) {
  Logger log = new Logger("defaultPreparationStage")
  Map preparationStageConfig = (Map) config[STAGE_PREPARATION] ?: [:]
  Boolean stageWrap = preparationStageConfig[STAGE_PREPARATION_STAGE_WRAP] != null ? preparationStageConfig[STAGE_PREPARATION_STAGE_WRAP] : true
  log.debug("stageWrap", stageWrap)
  if (stageWrap) {
    stage('Preparation') {
      _call(config)
    }
  } else {
    _call(config)
  }
}

/**
 * Internal function that takes care of the extend mechanism defaultPreparationStage
 *
 * @param config Configuration options for the used steps
 *
 */
void _call(Map config) {
  Logger log = new Logger("defaultPreparationStage._call")
  TypeUtils typeUtils = new TypeUtils()
  Map preparationStageConfig = (Map) config[STAGE_PREPARATION] ?: [:]

  def _extend = typeUtils.isClosure(preparationStageConfig[STAGE_PREPARATION_EXTEND]) ? preparationStageConfig[STAGE_PREPARATION_EXTEND] : null

  // no extends are configured, call the implementation
  if (!_extend) {
    log.debug("no extend configured, using default implementation")
    _impl(config)
  } else {
    // call extend and provide a reference to the default implementation
    _extend(config, this.&_impl)
  }
}

/**
 * Internal function that executes the preparation stage steps
 *
 * @param config Configuration options for the used steps
 *
 * @see ../vars/setupPVTools.groovy
 * @see ../vars/setupPVTools.md
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.md
 */
void _impl(Map config) {
  Logger log = new Logger("defaultPreparationStage._impl")

  Map preparationStageConfig = (Map) config[STAGE_PREPARATION] ?: [:]
  Boolean doSetupTools = preparationStageConfig[STAGE_PREPARATION_SETUP_TOOLS] != null ? preparationStageConfig[STAGE_PREPARATION_SETUP_TOOLS] : true
  Boolean doCheckoutScm = preparationStageConfig[STAGE_PREPARATION_CHECKOUT_SCM] != null ? preparationStageConfig[STAGE_PREPARATION_CHECKOUT_SCM] : true
  Boolean doSetBuildName = preparationStageConfig[STAGE_PREPARATION_SET_BUILD_NAME] != null ? preparationStageConfig[STAGE_PREPARATION_SET_BUILD_NAME] : true
  Boolean doPurgeSnapshots = preparationStageConfig[STAGE_PREPARATION_PURGE_SHAPSHOTS] != null ? preparationStageConfig[STAGE_PREPARATION_PURGE_SHAPSHOTS] : true


  log.debug("doSetupTools", doSetupTools)
  log.debug("doCheckoutScm", doCheckoutScm)
  log.debug("doSetBuildName", doSetBuildName)
  log.debug("doPurgeSnapshots", doPurgeSnapshots)

  if (doSetupTools) {
    // setup basic tools, jdk, maven, etc
    setupPVTools(config)
  } else {
    log.info("tool setup is disabled by provided configuration")
  }
  if (doCheckoutScm) {
    // do the checkout
    checkoutScm(config)
  } else {
    log.info("scm checkout is disabled by provided configuration")
  }
  if (doSetBuildName) {
    // set the build name
    setBuildName()
  } else {
    log.info("build name setting is disabled by provided configuration")
  }
  if (doPurgeSnapshots) {
    // purge SNAPSHOTs from maven repository
    maven.purgeSnapshots(config)
  } else {
    log.info("SNAPSHOT purging is disabled by provided configuration")
  }
}