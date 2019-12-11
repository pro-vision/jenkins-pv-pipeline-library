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
import io.wcm.devops.jenkins.pipeline.utils.ConfigConstants
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import org.jenkinsci.plugins.workflow.cps.DSL

/**
 * This is the default step used in Jenkinsfiles lying in the project root.
 * This script checks if the job is a normal pipeline build job or a multibranch pipeline job.
 * Depending on the detected job type the logic is routing to
 * - buildFeature step or
 * - buildDefault step
 *
 * @param config Configuration options for the used steps
 *
 * @see ../vars/buildDefault.groovy
 * @see ../vars/buildDefault.md
 * @see ../vars/buildFeature.groovy
 * @see ../vars/buildFeature.md
 */
void call(Map config = [:]) {
  wrap.color(config) {
    Logger.init(this, config)
    Logger log = new Logger(this)

    // try to retrieve the branch name from environment. When BRANCH_NAME is found it is a multi branch pipeline build
    String branchName = null
    try {
      branchName = env.getProperty(EnvironmentConstants.BRANCH_NAME)
      log.debug("Detected 'BRANCH_NAME' with value: ", branchName)
    } catch (Exception e) {
      log.debug("No environment variable 'BRANCH_NAME' found")
    }

    if (scm && branchName != null) {
      // when scm and branchName is detected call the buildFeature step
      log.info("MultiBranch Pipeline Build detected. Executing 'buildFeature' step")
      buildFeature(config)
    } else if (scm) {
      // when only scm variable is detected call the buildDefault step
      log.info("Existing scm checkout found, executing defaultBuildStep")
      // tell default build that the existing scm var should be used for checkout
      config[ConfigConstants.SCM] = config[ConfigConstants.SCM] ?: [:]
      config[ConfigConstants.SCM][ConfigConstants.SCM_USE_SCM_VAR] = true
      buildDefault(config)
    } else {
      log.error("Unable to route the defaultJenkinsFile, exiting with error")
      error("Unable to route the defaultJenkinsFile, exiting with error")
    }
  }
}
