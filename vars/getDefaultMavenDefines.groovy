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

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAVEN
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAVEN_DEFINES

/**
 * Utility function to detect some defines automatically by looking into the project structure.
 * This automatically adds
 *
 *   -Dnodejs.directory=${WORKSPACE}/target/.nodejs when at least one package.json was found in workspace
 *
 * @return Map containing the maven defines configuration
 *
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.groovy">execMaven.groovy</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/execMaven.md">execMaven.md</a>
 * @see <a href="../vars/defaultCompileStage.groovy">defaultCompileStage.groovy</a>
 * @see <a href="../vars/defaultCompileStage.md">defaultCompileStage.md</a>
 * @see <a href="../vars/defaultAnalyzeStage.groovy">defaultAnalyzeStage.groovy</a>
 * @see <a href="../vars/defaultAnalyzeStage.md">defaultAnalyzeStage.md</a>
 */
Map call() {
  Logger log = new Logger(this)
  // init defaults
  Map defines = ["continuous-integration": true]

  // look for package.json files, when found maven will execute a nodejs build and we have to set the directory
  def foundPackageJsons = findFiles(glob: '**/package.json')
  if (foundPackageJsons.size() > 0) {
    log.info("found package.json " + foundPackageJsons.length + " time(s), auto adding nodejs.directory to maven defines")
    defines = MapUtils.merge(defines, ["nodejs.directory": '${WORKSPACE}/target/.nodejs'])
  }

  return [(MAVEN): [(MAVEN_DEFINES): defines]]
}
