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
package vars.getDefaultMavenDefines

import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.jenkinsci.plugins.pipeline.utility.steps.fs.FileWrapper
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.FIND_FILES
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAVEN
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAVEN_DEFINES
import static org.junit.Assert.assertEquals

class GetDefaultMavenDefinesIT extends PVLibraryIntegrationTestBase {

  @Override
  void setUp() throws Exception {
    super.setUp()
  }

  @Test
  void shouldAddNodeJsDirectoryDefine() {
    helper.registerAllowedMethod(FIND_FILES, [Map.class], {
      FileWrapper[] res = new FileWrapper[1]
      res[0] = new FileWrapper('', 'package.json', false, 0, 0)
      return res
    })
    def result = loadAndExecuteScript("vars/getDefaultMavenDefines/jobs/getDefaultMavenDefinesTestJob.groovy")
    def expected = [:]
    expected[MAVEN] = [(MAVEN_DEFINES): ["continuous-integration": true, "nodejs.directory": '${WORKSPACE}/target/.nodejs']]
    assertEquals(expected, result)
  }

  @Test
  void shouldNotAddNodeJsDirectoryDefine() {
    helper.registerAllowedMethod(FIND_FILES, [Map.class], {
      FileWrapper[] res = new FileWrapper[0]
      return res
    })
    def result = loadAndExecuteScript("vars/getDefaultMavenDefines/jobs/getDefaultMavenDefinesTestJob.groovy")
    def expected = [:]
    expected[MAVEN] = [(MAVEN_DEFINES): ["continuous-integration": true]]
    assertEquals(expected, result)
  }
}
