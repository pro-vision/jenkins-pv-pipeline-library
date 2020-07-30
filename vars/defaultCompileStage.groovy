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
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * Default build step for building maven based projects.
 * Per default this step will call maven with the goals "clean deploy"
 *
 * @param config Configuration options for the used steps
 *
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md
 */
void call(Map config) {
  Logger log = new Logger("defaultCompileStage")
  TypeUtils typeUtils = new TypeUtils()
  Map compileStageCfg = (Map) config[STAGE_COMPILE] ?: [:]

  def _extend = typeUtils.isClosure(compileStageCfg[STAGE_COMPILE_EXTEND]) ? compileStageCfg[STAGE_COMPILE_EXTEND] : null

  stage('Compile') {
    // no extends are configured, call the implementation
    if (!_extend) {
      log.debug("no extend configured, using default implementation")
      _impl(config)
    } else {
      // call extend and provide a reference to the default implementation
      _extend(config, this.&_impl)
    }
  }
}

/**
 * Implementation of the compile stage
 *
 * @param config Configuration options for the used steps
 *
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.groovy
 * @see https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md
 */
void _impl(Map config) {
  Logger log = new Logger("defaultCompileStage._impl")

  Map compileStageCfg = (Map) config[STAGE_COMPILE] ?: [:]

  log.debug("compileStageCfg from config[STAGE_COMPILE]", compileStageCfg)

  // get maven defines based on project structure (e.g. nodejs.directory)
  def defaultMavenDefines = getDefaultMavenDefines()
  // merge config with maven defaults and incoming stage configuration

  Map compileDefaultCgf = [
    (MAVEN): [
      (MAVEN_GOALS)    : ["clean", "deploy"],
      (MAVEN_ARGUMENTS): ["-B", "-U"]
    ]
  ]
  compileStageCfg = MapUtils.merge(config, compileDefaultCgf, defaultMavenDefines, compileStageCfg)

  log.debug("compileStageCfg after merging, before execute", compileStageCfg)

  // execute maven build
  execMaven(compileStageCfg)

  // check if we have to stash the files for later use
  if (compileStageCfg[STASH]) {
    log.info("stashing compile files for later usage with name: '${STASH_COMPILE_FILES}'")
    // stash the files for later usage
    stash(name: STASH_COMPILE_FILES, includes: "**/*")
  }
}