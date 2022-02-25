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


import io.wcm.devops.jenkins.pipeline.utils.maps.MapMergeMode
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import org.jenkinsci.plugins.workflow.cps.DSL

import java.util.concurrent.TimeUnit

/**
 * Adapter function when running without configuration
 *
 * @param body The steps/code that has to be executed by this step
 * @return The configuration used and processed by this step
 */
Map call(Closure body) {
  return this.call([:], body)
}

/**
 * Utility step to ensure all pipeline scripts are equal.
 * This step takes care about the job defaults like
 * - mail notification
 * - build timeout
 * - adding timestamps to console
 * - settings the build result
 *
 * @param config Configuration options for the used steps
 * @param body The steps/code that has to be executed by this step
 * @return The configuration used and processed by this step
 *
 * @see <ahref="../vars/setJobProperties.groovy" > setJobProperties.groovy</a>
 * @see <ahref="../vars/setJobProperties.md" > setJobProperties.md</a>
 * @see <ahref="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.groovy" > notifyMail.groovy</a>
 * @see <ahref="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/notifyMail.md" > notifyMail.md</a>
 */
Map call(Map config, Closure body) {
  Map defaultWrapperConfig = [
    (BUILD_WRAPPER): [
      (BUILD_WRAPPER_SSH_TARGETS): [],
      (MAP_MERGE_MODE)           : MapMergeMode.REPLACE
    ],
    (TIMEOUT)      : [
      (TIMEOUT_TIME)  : 30,
      (TIMEOUT_UNIT)  : TimeUnit.MINUTES,
      (MAP_MERGE_MODE): MapMergeMode.REPLACE
    ]
  ]
  config = MapUtils.merge(defaultWrapperConfig, config)

  Map buildWrapperConfig = config[BUILD_WRAPPER]
  List sshTargets = buildWrapperConfig[BUILD_WRAPPER_SSH_TARGETS]

  // set the build timeout, default is 30 minutes
  Map timeoutConfig = (Map) config[TIMEOUT]
  int timeoutTime = (int) timeoutConfig[TIMEOUT_TIME]
  TimeUnit timeoutUnit = (TimeUnit) timeoutConfig[TIMEOUT_UNIT]

  wrappers.color(config) {
    // Only initialize logger when not already initialized, to avoid LogLevel overwriting
    if (!Logger.initialized) {
      Logger.init(this, config)
    }

    Logger log = new Logger(this)

    log.debug("config", config)

    // surround build by try catch for e-mail notification
    try {
      log.trace("set timeout to ${timeoutTime} unit: ${timeoutUnit}")
      // set timeout
      timeout(time: timeoutTime, unit: timeoutUnit) {
        // set the timestamps for the build
        timestamps {
          // set the default job properties
          setJobProperties(config)
          // wrap with ssh agent
          sshAgentWrapper(sshTargets) {
            // call the provided closure
            body()
          }
        }
      }
      // job is successful when this line is reached
      currentBuild.result = "SUCCESS"
    } catch (e) {
      // error occurred, set build result to failed
      log.error("Exception Message: ", e.toString())
      currentBuild.result = "FAILED"
      throw e
    } finally {
      notify.mail(config)
      // notify.mattermost(config)
      notify.mqtt(config)
      notify.teams(config)
    }
  }
  return config
}
