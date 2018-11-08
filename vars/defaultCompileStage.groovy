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

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * Default build step for building maven based projects.
 * Per default this step will call maven with the goals "clean deploy"
 *
 * @param config Configuration options for the used steps
 *
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.groovy">execMaven.groovy</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md">execMaven.md</a>
 */
void call(Map config) {
  Logger log = new Logger(this)
  stage('Compile') {
    // get maven defines based on project structure (e.g. nodejs.directory)
    def defaultMavenDefines = getDefaultMavenDefines()
    // merge config with maven defaults and incoming stage configuration
    Map compileStageCfg = (Map) config[STAGE_COMPILE] ?: [:]
    Map compileDefaultCgf = [(MAVEN): [
      (MAVEN_GOALS)    : ["clean", "deploy"],
      (MAVEN_ARGUMENTS): ["-B", "-U"]
    ]
    ]
    compileStageCfg = MapUtils.merge(config, compileDefaultCgf, compileStageCfg, defaultMavenDefines)
    // execute maven build
    execMaven(compileStageCfg)

    // check if we have to stash the files for later use
    if (compileStageCfg[STASH]) {
      log.info("stashing compile files for later usage with name: '${STASH_COMPILE_FILES}'")
      // stash the files for later usage
      stash(name: STASH_COMPILE_FILES, includes: "**/*")
    }
  }
}
