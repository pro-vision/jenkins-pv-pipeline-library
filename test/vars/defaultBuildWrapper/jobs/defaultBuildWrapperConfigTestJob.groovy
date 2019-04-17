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
package vars.defaultBuildWrapper.jobs

import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger

import java.util.concurrent.TimeUnit

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

def execute() {
  Map config = [
    (MAVEN)     : [
      (MAVEN_POM): "path/to/test/pom.xml"
    ],
    (TIMEOUT)   : [
      (TIMEOUT_TIME): 10,
      (TIMEOUT_UNIT): TimeUnit.HOURS
    ],
    (ANSI_COLOR): ANSI_COLOR_GNOME_TERMINAL,
    (LOGLEVEL)  : LogLevel.INFO
  ]

  Logger.init(this, LogLevel.DEBUG)
  defaultBuildWrapper(config) {

    setupPVTools((TOOLS): [
      (TOOL_MAVEN): "customMaven",
      (TOOL_JDK)  : "customJDK"
    ])
  }
}

return this
