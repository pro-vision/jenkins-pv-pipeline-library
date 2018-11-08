/*-
 * #%L
 * wcm.io
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
import de.provision.devops.jenkins.pipeline.PipelineConfiguration
import io.wcm.devops.jenkins.pipeline.model.Tool

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.TOOL_JDK
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.TOOL_MAVEN
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

/**
 * This step takes care about initializing the tools used at p!v.
 * Per default this is currently
 * - JDK8
 * - Apache Maven 3.3.3
 *
 * @param config Configuration options for the used steps
 *
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setupTools.groovy">setupTools.groovy</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/vars/setupTools.md">setupTools.md</a>
 */
void call(Map config = [:]) {
  PipelineConfiguration pipelineConfig = new PipelineConfiguration(this)
  Map toolsConfig = (Map) config[TOOLS] ?: [:]
  String defaultMaven = pipelineConfig.getDefaultMaven()
  String defaultJdk = pipelineConfig.getDefaultJdk()
  String toolMaven = toolsConfig[TOOL_MAVEN] ?: defaultMaven
  String toolJdk = toolsConfig[TOOL_JDK] ?: defaultJdk

  setupTools(
    (TOOLS):
      [
        [(TOOL_NAME): toolMaven, (TOOL_TYPE): Tool.MAVEN],
        [(TOOL_NAME): toolJdk, (TOOL_TYPE): Tool.JDK]
      ]
  )
}
