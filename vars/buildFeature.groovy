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
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils
import org.jenkinsci.plugins.workflow.cps.DSL

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * Step for building feature branches. This step basically calls buildDefault step with custom
 * maven options for the compile stage.
 *
 * When running in trusted mode this step also configures the appropriate scm options
 * - checkout to local branch, 'LocalBranch' extension
 * - merge with master, 'PreBuildMerge' extension
 *
 * When running in untrusted mode (folder library) the scm configuration from the job page is used for checkout.
 * In this mode you have to configure
 * - Checkout to local branch Extension with empty value
 * - Merge before build with origin/master
 *
 * @param config Configuration options for the used steps
 *
 * @see <a href="../vars/buildDefault.groovy">buildDefault.groovy</a>
 * @see <a href="../vars/buildDefault.md">buildDefault.md</a>
 */
void call(Map config = [:]) {
  wrap.color(config) {
    Logger.init(this, config)
    Logger log = new Logger(this)

    // add maven configuration for compile step (no deploy to artifact manager for feature branches)
    Map featureBranchConfig = [
      (STAGE_COMPILE): [
        (MAVEN): [(MAVEN_GOALS): "clean install"]
      ],
      (JOB_TYPE)     : JOB_TYPE_FEATURE
    ]

    config = MapUtils.merge(config, featureBranchConfig)

    Map scmConfig = (Map) config.get(SCM) ?: [:]
    String scmUrl = scmConfig.get(SCM_URL) ?: null


    try {
      // check wether to use configured scm or existing scm var
      if (scmUrl == null) {
        config[SCM] = config[SCM] ?: [:]
        config[SCM][SCM_USE_SCM_VAR] = true
      }
      //calling buildDefault with scm configuration
      buildDefault(config)
    }

    catch (Exception e) {
      currentBuild.result = "FAILED"
      notifyMail(config)
      throw e
    }
  }
}
