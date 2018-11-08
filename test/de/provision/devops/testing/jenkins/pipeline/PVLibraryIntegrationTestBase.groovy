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
package de.provision.devops.testing.jenkins.pipeline

import io.wcm.testing.jenkins.pipeline.LibraryIntegrationTestBase
import io.wcm.testing.jenkins.pipeline.LibraryIntegrationTestContext
import io.wcm.testing.jenkins.pipeline.global.lib.SubmoduleSourceRetriever
import org.jenkinsci.plugins.pipeline.utility.steps.fs.FileWrapper

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static io.wcm.testing.jenkins.pipeline.StepConstants.EXEC_MANAGED_SHELL_SCRIPT
import static io.wcm.testing.jenkins.pipeline.StepConstants.FIND_FILES
import static io.wcm.testing.jenkins.pipeline.StepConstants.MAVEN_PURGE_SNAPSHOTS
import static io.wcm.testing.jenkins.pipeline.StepConstants.TOOL

/**
 * Base class for integration tests that use the JenkinsPipelineUnit testing framework
 *
 * @see <a href="https://github.com/lesfurets/JenkinsPipelineUnit">JenkinsPipelineUnit</a>
 * @see <a href="https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/test/io/wcm/testing/jenkins/pipeline/LibraryIntegrationTestBase.groovy">LibraryIntegrationTestBase.groovy</a>
 */
class PVLibraryIntegrationTestBase extends LibraryIntegrationTestBase {

  @Override
  void setUp() throws Exception {
    super.setUp()

    // mocking findFiles method
    helper.registerAllowedMethod(FIND_FILES, [Map.class], {
      FileWrapper[] res = new FileWrapper[0]
      return res
    })

    // redirect tool step to own callback
    helper.registerAllowedMethod(TOOL, [String.class], toolCallback)

    // load the pipeline-library from git submodule
    def library = library()
      .name('pipeline-library')
      .retriever(SubmoduleSourceRetriever.submoduleSourceRetriever(("submodules")))
      .targetPath('jenkins-pipeline-library')
      .defaultVersion("master")
      .allowOverride(true)
      .implicit(true)
      .build()
    helper.registerSharedLibrary(library)
  }

  /**
   * Override for recoding the execManagedShellScript step
   */
  @Override
  protected void afterLoadingScript() {
    super.afterLoadingScript()
    helper.registerAllowedMethod(EXEC_MANAGED_SHELL_SCRIPT, [String.class, String.class], { String fileId, String argLine ->
      stepRecorder.record(EXEC_MANAGED_SHELL_SCRIPT, [fileId: fileId, args: argLine])
    })
    helper.registerAllowedMethod(EXEC_MANAGED_SHELL_SCRIPT, [String.class, List.class], { String fileId, List args ->
      stepRecorder.record(EXEC_MANAGED_SHELL_SCRIPT, [fileId: fileId, args: args])
    })
    helper.registerAllowedMethod(MAVEN_PURGE_SNAPSHOTS, [Map.class], { Map config ->
      stepRecorder.record(MAVEN_PURGE_SNAPSHOTS, config)
    }
    )
  }

  /**
   * Callback for tool step to provide tool paths for integration tests
   *
   * @return mocked tool path
   */
  def toolCallback = { String tool ->
    stepRecorder.record(TOOL, tool)
    if (tool.matches(/(?i).*maven.*/)) {
      return LibraryIntegrationTestContext.TOOL_MAVEN_PREFIX.concat(tool)
    }
    if (tool.matches(/(?i).*jdk.*/)) {
      return LibraryIntegrationTestContext.TOOL_MAVEN_PREFIX.concat(tool)
    }
    return ""
  }
}
