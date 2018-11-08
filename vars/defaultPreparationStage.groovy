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
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
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
 * @see <a href="../vars/setupPVTools.groovy">setupPVTools.groovy</a>
 * @see <a href="../vars/setupPVTools.md">setupPVTools.md</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.groovy">checkoutScm.groovy</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/checkoutScm.md">checkoutScm.md</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.groovy">setBuildName.groovy</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setBuildName.md">setBuildName.md</a>
 */
void call(Map config) {
  Logger log = new Logger(this)
  stage('Preparation') {
    Map preparationStageConfig = config[STAGE_PREPARATION] ?: [:]
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
}
