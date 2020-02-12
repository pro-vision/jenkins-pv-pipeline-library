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

import io.wcm.devops.jenkins.pipeline.utils.TypeUtils
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.devops.jenkins.pipeline.utils.maps.MapMergeMode
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
 * - Merge before build with parent branch
 *
 * @param config Configuration options for the used steps
 *
 * @see ../vars/buildDefault.groovy
 * @see ../vars/buildDefault.md
 */
void call(Map config = [:]) {
  Logger log = new Logger("buildFeature")
  TypeUtils typeUtils = new TypeUtils()
  Map buildFeatureConfig = (Map) config[BUILD_FEATURE] ?: [:]

  def _extend = typeUtils.isClosure(buildFeatureConfig[BUILD_FEATURE_EXTEND]) ? buildFeatureConfig[BUILD_FEATURE_EXTEND] : null

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
 * Implementation of the step
 *
 * @param config Configuration options for the used steps
 */
void _impl(Map config = [:]) {
  wrap.color(config) {
    Logger.init(this, config)
    Logger log = new Logger(this)

    Map defaultFeatureBranchConfig = [
      (JOB_TYPE)     : JOB_TYPE_FEATURE,
      (BUILD_FEATURE): [
        (STAGE_COMPILE): [
          (MAVEN): [
            (MAVEN_GOALS)   : ["clean", "install"],
            (MAP_MERGE_MODE): MapMergeMode.REPLACE
          ]
        ]
      ],
    ]

    // merge with default config
    config = MapUtils.merge(defaultFeatureBranchConfig, config)

    // get the feature branch config
    Map buildFeatureConfig = (Map) config[BUILD_FEATURE] ?: [:]
    config = MapUtils.merge(config, buildFeatureConfig)

    Map scmConfig = (Map) config.get(SCM) ?: [:]
    String scmUrl = scmConfig.get(SCM_URL) ?: null

    // check wether to use configured scm or existing scm var
    if (scmUrl == null) {
      config[SCM] = config[SCM] ?: [:]
      config[SCM][SCM_USE_SCM_VAR] = true
    }

    //calling buildDefault with scm configuration
    buildDefault(config)
  }
}