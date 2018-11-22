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
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils


/**
 * Default build step for maven/java based projects.
 * This step takes care of
 * - wrap the build with defaults (timestamps, timeout, triggers etc., mail notification)
 * - call default preparation stage (e.g. checkout from scm etc.)
 * - call default compile stage (e.g. building project with maven, deploy artifacts to nexus)
 * - call default analyze stage (e.g. scanning project with maven for pmd, checkstyle and findbugs)
 * - call default results stage (e.g. processing junit results, pmd etc.)
 *
 * @param config Pipeline options for the used steps
 * @see <a href="../vars/defaultBuildWrapper.groovy">defaultBuildWrapper.groovy</a>
 * @see <a href="../vars/defaultBuildWrapper.md">defaultBuildWrapper.md</a>
 * @see <a href="../vars/defaultPreparationStage.groovy">defaultPreparationStage.groovy</a>
 * @see <a href="../vars/defaultPreparationStage.md">defaultPreparationStage.md</a>
 * @see <a href="../vars/defaultCompileStage.groovy">defaultCompileStage.groovy</a>
 * @see <a href="../vars/defaultCompileStage.md">defaultCompileStage.md</a>
 * @see <a href="../vars/defaultCompileStage.groovy">defaultAnalyzeStage.groovy</a>
 * @see <a href="../vars/defaultCompileStage.md">defaultAnalyzeStage.md</a>
 * @see <a href="../vars/defaultCompileStage.groovy">defaultResultsStage.groovy</a>
 * @see <a href="../vars/defaultCompileStage.md">defaultResultsStage.md</a>
 */
void call(Map config = [:]) {
  wrap.color(config) {
    Logger log = new Logger(this)

    Map propertyCfg = config[PROPERTIES] ?: [:]
    List triggerCfg = propertyCfg[PROPERTIES_PIPELINE_TRIGGERS] != null ? propertyCfg[PROPERTIES_PIPELINE_TRIGGERS] : null
    if (triggerCfg == null) {
      log.info("no trigger configuration detected, setting the default triggers")
      triggerCfg = defaults.getTriggers()
    }

    Map defaultConfig = [
      (PROPERTIES): [
        (PROPERTIES_PIPELINE_TRIGGERS): triggerCfg
      ]
    ]
    config = MapUtils.merge(defaultConfig, config)

    log.debug("Calling defaultBuildWrapper with the following configuration: $config")

    // call the default build wrapper
    defaultBuildWrapper(config) {
      // allocate node
      node(config[NODE]) {
        // do the checkout, set build name etc
        log.trace("calling defaultPreparationStage")
        defaultPreparationStage(config)
        // prepare feature branches
        log.trace("calling featurePreparationStage")
        featurePreparationStage(config)
        // run maven build
        log.trace("calling defaultCompileStage")
        defaultCompileStage(config)
        // run maven analyze
        log.trace("calling defaultAnalyzeStage")
        defaultAnalyzeStage(config)
        // analyze results
        log.trace("calling defaultResultsStage")
        defaultResultsStage(config)
      }
    }
  }
}
