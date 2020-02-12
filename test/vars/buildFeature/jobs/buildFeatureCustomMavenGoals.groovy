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
package vars.buildFeature.jobs

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger

def execute() {
  Map config = [
    (LOGLEVEL)     : LogLevel.ALL,
    (BUILD_FEATURE): [
      (STAGE_COMPILE): [
        (MAVEN): [
          (MAVEN_GOALS): ["goal1", "goal2"],
        ]
      ]
    ]
  ]
  buildFeature(config)
}

return this
