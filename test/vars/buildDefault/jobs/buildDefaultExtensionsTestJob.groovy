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
package vars.buildDefault.jobs

import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*

def customPreExtension1(Map config) {
  sh("echo 'customPreExtension1'")
}

def customPreExtension2(Map config) {
  sh("echo 'customPreExtension2'")
}

def customPostExtension1(Map config) {
  sh("echo 'customPostExtension1'")
}

def customPostExtension2(Map config) {
  sh("echo 'customPostExtension2'")
}

def execute() {
  buildDefault(
    [
      (SCM)     : [
        (SCM_URL): "git@git-ssh.domain.tld/group1/project1"
      ],
      (LOGLEVEL): LogLevel.ALL,
      (BUILD_DEFAULT) : [
        (BUILD_DEFAULT_PRE_EXTENSIONS): [
          this.&customPreExtension1,
          this.&customPreExtension2,
        ],
        (BUILD_DEFAULT_POST_EXTENSIONS): [
          this.&customPostExtension1,
          this.&customPostExtension2,
        ]
      ]
    ]
  )
}


return this
