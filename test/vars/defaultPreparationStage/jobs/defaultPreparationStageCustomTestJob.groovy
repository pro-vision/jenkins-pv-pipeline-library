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
package vars.defaultPreparationStage.jobs

import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.SCM
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.SCM_BRANCHES
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.SCM_EXTENSIONS
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.SCM_URL
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.TOOLS
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

def execute() {
  defaultPreparationStage(
    (SCM): [
      (SCM_URL)       : "git@git-ssh.domain.tld/group1/project1",
      (SCM_BRANCHES)  : [[name: '*/customBranch']],
      (SCM_EXTENSIONS): [
        ['$class': 'PreBuildMerge', options: [fastForwardMode: 'FF', mergeRemote: 'origin', mergeStrategy: 'default', mergeTarget: 'master']]
      ]
    ],
    (TOOLS): [
      (TOOL_MAVEN): "customMaven",
      (TOOL_JDK)  : "customJDK"
    ]
  )
}

return this
