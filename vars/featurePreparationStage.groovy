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


import io.wcm.devops.jenkins.pipeline.environment.EnvironmentConstants
import io.wcm.devops.jenkins.pipeline.shell.GitCommandBuilderImpl
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.devops.jenkins.pipeline.utils.maps.MapMergeMode
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils
import org.jenkinsci.plugins.workflow.cps.DSL

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAP_MERGE_MODE

/**
 * Preparation stage for feature branches
 * In this step the current featurebranch is merged with the origin feature branch
 *
 * @param config Configuration options for the used steps
 *
 */
void call(Map config) {
  Logger log = new Logger(this)

  Map defaultConfig = [
    (STAGE_FEATURE_PREPARATION): [
      (STAGE_FEATURE_PREPARATION_MERGE): [
        (STAGE_FEATURE_PREPARATION_MERGE_ENABLED): true,
        (MAP_MERGE_MODE)                         : (MapMergeMode.REPLACE)
      ]
    ]
  ]

  config = MapUtils.merge(defaultConfig, config)
  String jobType = config.get(JOB_TYPE) ?: null

  // only execute stage when job type is a feature branch
  if (jobType == JOB_TYPE_FEATURE) {
    Map featurePreparationStageConfig = (Map) config[STAGE_FEATURE_PREPARATION]
    Map mergeConfig = (Map) featurePreparationStageConfig[STAGE_FEATURE_PREPARATION_MERGE]
    Boolean mergeEnabled = mergeConfig[STAGE_FEATURE_PREPARATION_MERGE_ENABLED]

    log.debug("merging with parent branch enabled", mergeEnabled)
    if (mergeEnabled) {
      String parentBranch = gitTools.getParentBranch()
      stage("Merge with '${parentBranch}'") {
        // build the command
        GitCommandBuilderImpl gitCommandBuilder = new GitCommandBuilderImpl((DSL) this.steps)
        gitCommandBuilder.addArguments(["merge", "${parentBranch}"])
        // execute with try catch to detect merge an execution errors
        try {
          log.info("merging with '${parentBranch}'")
          sh(gitCommandBuilder.build())
        } catch (Exception ex) {
          error("The branch '${env.getProperty(EnvironmentConstants.GIT_BRANCH)}' is not suitable for integration into '${parentBranch}'")
        }
      }
    } else {
      log.debug("merge with parent branch is disabled by configuration")
    }

  }
}
