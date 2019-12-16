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
package vars.defaultResultsStage.jobs

import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

def customResultsStage(Map config, originalImpl) {
  sh("echo 'customResultsStage before'")
  originalImpl(config)
  sh("echo 'customResultsStage after'")
}

def execute(Map config = [:]) {
  Map customConfig = [
    (STAGE_RESULTS) : [
      (STAGE_RESULTS_EXTEND) : this.&customResultsStage
    ]
  ]
  config = MapUtils.merge(config, customConfig)
  defaultResultsStage(config)
}

return this
