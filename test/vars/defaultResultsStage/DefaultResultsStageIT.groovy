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
package vars.defaultResultsStage

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import de.provision.devops.testing.jenkins.pipeline.PVLibraryIntegrationTestBase
import org.junit.Assert
import org.junit.Test

import static io.wcm.testing.jenkins.pipeline.StepConstants.*
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertNone
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertOnce
import static io.wcm.testing.jenkins.pipeline.recorder.StepRecorderAssert.assertStepCalls
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertEquals

class DefaultResultsStageIT extends PVLibraryIntegrationTestBase {

  @Test
  void shouldRunWithDefaults() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy")

    this.assertDefaults()
  }

  @Test
  void shouldRunWithExtend() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageExtendTestJob.groovy")

    this.assertDefaults()

    List shellCalls = assertStepCalls(SH, 2)
    assertEquals("echo 'customResultsStage before'", shellCalls[0])
    assertEquals("echo 'customResultsStage after'", shellCalls[shellCalls.size()-1])
  }

  @Test
  void shouldNotRunWhenDisabled() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageDisabledTestJob.groovy")

    assertNone(STAGE)
  }

  void assertDefaults() {
    Map expectedJUnitCall = [
      allowEmptyResults: true,
      testResults      : '**/target/surefire-reports/*.xml,**/target/failsafe-reports/*.xml'
    ]
    Map expectedJacocoCall = [
      $class                    : 'JacocoPublisher',
      classPattern              : '**/target/classes',
      execPattern               : '**/target/**.exec',
      exclusionPattern          : "",
      inclusionPattern          : "",
      sourcePattern             : '**/src/main/java',
      buildOverBuild            : false,
      changeBuildStatus         : false,
      skipCopyOfSrcFiles        : false,
      deltaBranchCoverage       : "0",
      deltaClassCoverage        : "0",
      deltaComplexityCoverage   : "0",
      deltaInstructionCoverage  : "0",
      deltaLineCoverage         : "0",
      deltaMethodCoverage       : "0",
      maximumBranchCoverage     : "0",
      maximumClassCoverage      : "0",
      maximumComplexityCoverage : "0",
      maximumInstructionCoverage: "0",
      maximumLineCoverage       : "0",
      maximumMethodCoverage     : "0",
      minimumBranchCoverage     : "0",
      minimumClassCoverage      : "0",
      minimumComplexityCoverage : "0",
      minimumInstructionCoverage: "0",
      minimumLineCoverage       : "0",
      minimumMethodCoverage     : "0",
    ]
    Map expectedFindBugsCall = [
      canComputeNew  : false,
      defaultEncoding: '',
      excludePattern : '',
      healthy        : '',
      includePattern : '',
      pattern        : '**/target/findbugs.xml,**/target/findbugsXml.xml,**/target/spotbugsXml.xml',
      unHealthy      : ''
    ]
    Map expectedPmdCall = [
      canComputeNew  : false,
      defaultEncoding: '',
      healthy        : '',
      pattern        : '**/target/pmd.xml',
      unHealthy      : ''
    ]
    Map expectedOpenTasksCall = [
      canComputeNew  : false,
      defaultEncoding: '',
      excludePattern : '**/node_modules/**,**/target/**,**/.nodejs/**,**/.rubygems/**',
      healthy        : '',
      high           : '',
      low            : '',
      normal         : 'TODO, FIXME, XXX',
      pattern        : '**/*.java,**/*.js,**/*.scss,**/*.hbs,**/*.html,**/*.groovy,**/*.gspec,**/*.sh', unHealthy: ''
    ]
    Map expectedCheckstyleCall = [
      canComputeNew  : false,
      defaultEncoding: '',
      healthy        : '',
      pattern        : '**/target/checkstyle-result*.xml',
      unHealthy      : ''
    ]
    Map expectedAnalysisPublisherCall = [
      $class         : 'AnalysisPublisher',
      canComputeNew  : false,
      defaultEncoding: '',
      healthy        : '',
      unHealthy      : ''
    ]

    Map actualJUnitCall = assertOnce(JUNIT)
    Map actualJacocoCall = assertOnce(JACOCOPUBLISHER)
    Map actualFindBugsCall = assertOnce(FINDBUGS)
    Map actualPmdCall = assertOnce(PMD)
    Map actualOpenTasksCall = assertOnce(OPENTASKS)
    Map actualChecktypeCall = assertOnce(CHECKSTYLE)
    Map actualAnalysisPublisherCall = assertOnce(ANALYSISPUBLISHER)

    Assert.assertEquals("assertion failure in junit call", expectedJUnitCall, actualJUnitCall)
    Assert.assertEquals("assertion failure in jacoco call", expectedJacocoCall, actualJacocoCall)
    Assert.assertEquals("assertion failure in findbugs call", expectedFindBugsCall, actualFindBugsCall)
    Assert.assertEquals("assertion failure in pmd call", expectedPmdCall, actualPmdCall)
    Assert.assertEquals("assertion failure in open tasks call", expectedOpenTasksCall, actualOpenTasksCall)
    Assert.assertEquals("assertion failure in checkstyle call", expectedCheckstyleCall, actualChecktypeCall)
    Assert.assertEquals("assertion failure in analysis publisher call", expectedAnalysisPublisherCall, actualAnalysisPublisherCall)
  }

  @Test
  void shouldNotExecuteJunit() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_JUNIT): [
          (ConfigConstants.STAGE_RESULTS_JUNIT_ENABLED): false
        ]
      ]
    ])
    assertNone(JUNIT)

    assertOnce(JACOCOPUBLISHER)
    assertOnce(CHECKSTYLE)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecuteJacoco() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_JACOCO): [
          (ConfigConstants.STAGE_RESULTS_JACOCO_ENABLED): false
        ]
      ]
    ])
    assertNone(JACOCOPUBLISHER)

    assertOnce(JUNIT)
    assertOnce(CHECKSTYLE)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecuteCheckstyle() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_CHECKSTYLE): [
          (ConfigConstants.STAGE_RESULTS_CHECKSTYLE_ENABLED): false
        ]
      ]
    ])
    assertNone(CHECKSTYLE)

    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecuteFindbugs() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_FINDBUGS): [
          (ConfigConstants.STAGE_RESULTS_FINDBUGS_ENABLED): false
        ]
      ]
    ])
    assertNone(FINDBUGS)

    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(CHECKSTYLE)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecutePmd() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_PMD): [
          (ConfigConstants.STAGE_RESULTS_PMD_ENABLED): false
        ]
      ]
    ])
    assertNone(PMD)

    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(CHECKSTYLE)
    assertOnce(FINDBUGS)
    assertOnce(OPENTASKS)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecuteOpenTasks() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_OPEN_TASKS): [
          (ConfigConstants.STAGE_RESULTS_OPEN_TASKS_ENABLED): false
        ]
      ]
    ])
    assertNone(OPENTASKS)

    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(CHECKSTYLE)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(ANALYSISPUBLISHER)
  }

  @Test
  void shouldNotExecuteAnalysisPublisher() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_ANALYSIS_PUBLISHER): [
          (ConfigConstants.STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED): false
        ]
      ]
    ])
    assertNone(ANALYSISPUBLISHER)

    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(CHECKSTYLE)
    assertOnce(FINDBUGS)
    assertOnce(PMD)
    assertOnce(OPENTASKS)
  }

  @Test
  void shouldExecuteJacocoWithCustomSettings() {
    Map expectedJacocoCall = [
      $class                    : 'JacocoPublisher',
      classPattern              : 'classPattern',
      execPattern               : 'execPattern',
      exclusionPattern          : 'exclusionPattern',
      inclusionPattern          : 'inclusionPattern',
      sourcePattern             : 'sourcePattern',
      buildOverBuild            : true,
      changeBuildStatus         : true,
      skipCopyOfSrcFiles        : true,
      deltaBranchCoverage       : 'deltaBranchCoverage',
      deltaClassCoverage        : 'deltaClassCoverage',
      deltaComplexityCoverage   : 'deltaComplexityCoverage',
      deltaInstructionCoverage  : 'deltaInstructionCoverage',
      deltaLineCoverage         : 'deltaLineCoverage',
      deltaMethodCoverage       : 'deltaMethodCoverage',
      maximumBranchCoverage     : 'maximumBranchCoverage',
      maximumClassCoverage      : 'maximumClassCoverage',
      maximumComplexityCoverage : 'maximumComplexityCoverage',
      maximumInstructionCoverage: 'maximumInstructionCoverage',
      maximumLineCoverage       : 'maximumLineCoverage',
      maximumMethodCoverage     : 'maximumMethodCoverage',
      minimumBranchCoverage     : 'minimumBranchCoverage',
      minimumClassCoverage      : 'minimumClassCoverage',
      minimumComplexityCoverage : 'minimumComplexityCoverage',
      minimumInstructionCoverage: 'minimumInstructionCoverage',
      minimumLineCoverage       : 'minimumLineCoverage',
      minimumMethodCoverage     : 'minimumMethodCoverage',
    ]
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_JACOCO): [
          (ConfigConstants.STAGE_RESULTS_JACOCO_ENABLED)                     : true,
          (ConfigConstants.STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD)            : "buildOverBuild",
          (ConfigConstants.STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS)         : "changeBuildStatus",
          (ConfigConstants.STAGE_RESULTS_JACOCO_CLASS_PATTERN)               : "classPattern",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE)       : "deltaBranchCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE)        : "deltaClassCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE)   : "deltaComplexityCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE)  : "deltaInstructionCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE)         : "deltaLineCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE)       : "deltaMethodCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN)           : "exclusionPattern",
          (ConfigConstants.STAGE_RESULTS_JACOCO_EXEC_PATTERN)                : "execPattern",
          (ConfigConstants.STAGE_RESULTS_JACOCO_INCLUSION_PATTERN)           : "inclusionPattern",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE)     : "maximumBranchCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE)      : "maximumClassCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE) : "maximumComplexityCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE): "maximumInstructionCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE)       : "maximumLineCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE)     : "maximumMethodCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE)     : "minimumBranchCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE)      : "minimumClassCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE) : "minimumComplexityCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE): "minimumInstructionCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE)       : "minimumLineCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE)     : "minimumMethodCoverage",
          (ConfigConstants.STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES)      : true,
          (ConfigConstants.STAGE_RESULTS_JACOCO_SOURCE_PATTERN)              : "sourcePattern",
        ]
      ]
    ])

    Map actualJacocoCall = assertOnce(JACOCOPUBLISHER)
    Assert.assertEquals("assertion failure in jacoco call", expectedJacocoCall, actualJacocoCall)
  }
}
