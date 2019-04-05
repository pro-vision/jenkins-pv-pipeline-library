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
package vars.setJobProperties.jobs

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*

def execute() {
  setJobProperties([
    (PROPERTIES): [
      (PROPERTIES_PIPELINE_TRIGGERS)        : [
        pollSCM("H/30 * * * *"),
        cron("H * * * *"),
        upstream(threshold: 'SUCCESS', upstreamProjects: 'testjob')
      ],
      (PROPERTIES_PARAMETERS)               : [
        booleanParam(defaultValue: false, description: '', name: 'boolparam'),
        choice(choices: 'choice1\nchoice2', description: '', name: 'choiceparam'),
        string(defaultValue: 'stringvalue', description: '', name: 'stringparam'),
        text(defaultValue: '''lorem
ipsum''', description: '', name: 'multiline')
      ],
      (PROPERTIES_BUILD_DISCARDER)          : [
        (PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP): '111',
        (PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP) : '222',
        (PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP)         : '',
        (PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP)          : ''
      ],
      (PROPERTIES_DISABLE_CONCURRENT_BUILDS): false,
      (PROPERTIES_CUSTOM): [
        [$class: 'BuildBlockerProperty', blockLevel: 'GLOBAL', blockingJobs: '.*blocking-job.*', scanQueueFor: 'DISABLED', useBuildBlocker: true]
      ]
    ]
  ])
}

return this
