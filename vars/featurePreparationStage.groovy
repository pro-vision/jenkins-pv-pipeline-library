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
import io.wcm.devops.jenkins.pipeline.environment.EnvironmentConstants
import io.wcm.devops.jenkins.pipeline.shell.GitCommandBuilderImpl
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import org.jenkinsci.plugins.workflow.cps.DSL

/**
 * Preparation stage for feature branches
 * In this step the current featurebranch is merged with the origin feature branch
 *
 * @param config Configuration options for the used steps
 *
 */
void call(Map config) {
  Logger log = new Logger(this)
  String jobType = config.get(ConfigConstants.JOB_TYPE) ?: null

  // only execute stage when job type is a feature branch
  if (jobType == ConfigConstants.JOB_TYPE_FEATURE) {
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
        error("The branch '${env.getProperty(EnvironmentConstants.GIT_BRANCH)}' is not suitable for integration into master")
      }
    }
  }
}
