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
      pattern          : '**/target/findbugs.xml,**/target/findbugsXml.xml,**/target/spotbugsXml.xml',
      reportEncoding   : 'UTF-8',
      useRankAsPriority: true
    ]
    Map expectedPmdCall = [
      pattern: '**/target/pmd.xml',
      reportEncoding: 'UTF-8'
    ]
    Map expectedTaskScannerCall = [
      excludePattern: '**/node_modules/**,**/target/**,**/.nodejs/**,**/.rubygems/**',
      includePattern: '**/*.java,**/*.js,**/*.scss,**/*.hbs,**/*.html,**/*.groovy,**/*.gspec,**/*.sh',
      normalTags    : 'TODO. FIXME, XXX'
    ]
    Map expectedCheckStyleCall = [
      pattern       : "**/target/checkstyle-result*.xml",
      reportEncoding: "UTF-8"
    ]
    Map expectedRecordIssuesCall = [
      "tools": [ JUNIT_PARSER, FIND_BUGS, PMD_PARSER, CHECK_STYLE, TASK_SCANNER  ]
    ]

    String expectedFingerprintCall = "**/target/**/*.zip,**/target/**/*.jar"

    String actualFingerprintCall = assertOnce(FINGERPRINT)
    Map actualJUnitCall = assertOnce(JUNIT)
    Map actualJacocoCall = assertOnce(JACOCOPUBLISHER)
    Map actualFindBugsCall = assertOnce(FIND_BUGS)
    Map actualPmdParserCall = assertOnce(PMD_PARSER)
    Map actualTaskScannerCall = assertOnce(TASK_SCANNER)
    Map actualCheckStyleCall = assertOnce(CHECK_STYLE)
    Map actualRecordIssuesCall = assertOnce(RECORD_ISSUES)

    Assert.assertEquals("assertion failure in junit call", expectedFingerprintCall, actualFingerprintCall)

    Assert.assertEquals("assertion failure in junit call", expectedJUnitCall, actualJUnitCall)
    Assert.assertEquals("assertion failure in jacoco call", expectedJacocoCall, actualJacocoCall)
    Assert.assertEquals("assertion failure in findbugs call", expectedFindBugsCall, actualFindBugsCall)
    Assert.assertEquals("assertion failure in pmdParser call", expectedPmdCall, actualPmdParserCall)
    Assert.assertEquals("assertion failure in open tasks call", expectedTaskScannerCall, actualTaskScannerCall)
    Assert.assertEquals("assertion failure in checkstyle call", expectedCheckStyleCall, actualCheckStyleCall)
    Assert.assertEquals("assertion failure in resutls call", expectedRecordIssuesCall, actualRecordIssuesCall)
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
    assertNone(JUNIT_PARSER)
    assertOnce(FINGERPRINT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FIND_BUGS)
    assertOnce(PMD_PARSER)
    assertOnce(TASK_SCANNER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
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

    assertOnce(FINGERPRINT)
    assertOnce(JUNIT)
    assertOnce(JUNIT_PARSER)
    assertOnce(FIND_BUGS)
    assertOnce(PMD_PARSER)
    assertOnce(TASK_SCANNER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
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
    assertNone(CHECK_STYLE)

    assertOnce(FINGERPRINT)
    assertOnce(JUNIT)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FIND_BUGS)
    assertOnce(PMD_PARSER)
    assertOnce(TASK_SCANNER)
    assertOnce(RECORD_ISSUES)
  }

  @Test
  void shouldNotFingerprint() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_FINGERPRINT): [
          (ConfigConstants.STAGE_RESULTS_FINGERPRINT_ENABLED): false
        ]
      ]
    ])
    assertNone(FINGERPRINT)

    assertOnce(JUNIT)
    assertOnce(JUNIT_PARSER)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FIND_BUGS)
    assertOnce(PMD_PARSER)
    assertOnce(TASK_SCANNER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
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
    assertNone(FIND_BUGS)

    assertOnce(FINGERPRINT)
    assertOnce(JUNIT)
    assertOnce(JUNIT_PARSER)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(PMD_PARSER)
    assertOnce(TASK_SCANNER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
  }

  @Test
  void shouldNotExecutePmdParser() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_PMD): [
          (ConfigConstants.STAGE_RESULTS_PMD_ENABLED): false
        ]
      ]
    ])
    assertNone(PMD_PARSER)

    assertOnce(FINGERPRINT)
    assertOnce(JUNIT)
    assertOnce(JUNIT_PARSER)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FIND_BUGS)
    assertOnce(TASK_SCANNER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
  }

  @Test
  void shouldNotExecuteTaskScanner() {
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
      (ConfigConstants.STAGE_RESULTS): [
        (ConfigConstants.STAGE_RESULTS_OPEN_TASKS): [
          (ConfigConstants.STAGE_RESULTS_OPEN_TASKS_ENABLED): false
        ]
      ]
    ])
    assertNone(TASK_SCANNER)

    assertOnce(FINGERPRINT)
    assertOnce(JUNIT)
    assertOnce(JUNIT_PARSER)
    assertOnce(JACOCOPUBLISHER)
    assertOnce(FIND_BUGS)
    assertOnce(PMD_PARSER)
    assertOnce(CHECK_STYLE)
    assertOnce(RECORD_ISSUES)
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

  @Test
  void shouldExecuteCheckstyleWithCustomSettings() {
    Map expectedCheckstyleCall = [
        reportEncoding: 'customEncoding',
        pattern : 'customPattern',
    ]
    loadAndExecuteScript("vars/defaultResultsStage/jobs/defaultResultsStageTestJob.groovy", [
        (ConfigConstants.STAGE_RESULTS): [
            (ConfigConstants.STAGE_RESULTS_CHECKSTYLE): [
                (ConfigConstants.STAGE_RESULTS_CHECKSTYLE_REPORT_ENCODING): "customEncoding",
                (ConfigConstants.STAGE_RESULTS_CHECKSTYLE_PATTERN)        : "customPattern",
            ]
        ]
    ])

    Map actualCheckstyleCall = assertOnce(CHECK_STYLE)
    Assert.assertEquals("assertion failure in checkstyle call", expectedCheckstyleCall, actualCheckstyleCall)
  }
}
