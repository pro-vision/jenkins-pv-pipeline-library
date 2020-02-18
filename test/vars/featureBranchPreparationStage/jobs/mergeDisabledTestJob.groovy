/*-
 * #%L
 * pro!vision GmbH
 * %%
 * Copyright (C) 2018 - 2020 pro!vision GmbH
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
package vars.featureBranchPreparationStage.jobs

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants

def execute() {
  featurePreparationStage(
    [
      (ConfigConstants.STAGE_FEATURE_PREPARATION): [
        (ConfigConstants.STAGE_FEATURE_PREPARATION_MERGE): [
          (ConfigConstants.STAGE_FEATURE_PREPARATION_MERGE_ENABLED): false
        ]
      ]
    ]
  )
}

return this
