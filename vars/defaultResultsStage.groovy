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

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import io.wcm.devops.jenkins.pipeline.utils.TypeUtils
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import io.wcm.devops.jenkins.pipeline.utils.maps.MapMergeMode
import io.wcm.devops.jenkins.pipeline.utils.maps.MapUtils

import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.MAP_MERGE_MODE

/**
 * Default build step for analyzing the build results for maven based projects.
 * This step takes care of:
 * - fingerprint artifacts
 * - publish JUnit test reports
 * - publish JaCoCo code coverage
 * - publish findbugs result
 * - publish pmd results
 * - publish open tasks
 * - publish checkstyle result
 * - aggregate static analysis results
 *
 * @param config Configuration options for the used steps
 */
void call(Map config = [:]) {
  Logger log = new Logger("defaultResultsStage")
  TypeUtils typeUtils = new TypeUtils()
  Map resultStageConfig = config[STAGE_RESULTS] ?: [:]
  Boolean resultsStageEnabled = resultStageConfig[STAGE_RESULTS_ENABLED] != null ? resultStageConfig[STAGE_RESULTS_ENABLED] : true

  if (!resultsStageEnabled) {
    log.debug("defaultResultsStage is disabled")
    return
  }

  def _extend = typeUtils.isClosure(resultStageConfig[STAGE_RESULTS_EXTEND]) ? resultStageConfig[STAGE_RESULTS_EXTEND] : null

  stage('Results') {
    // no extends are configured, call the implementation
    if (!_extend) {
      log.debug("no extend configured, using default implementation")
      _impl(config)
    } else {
      // call extend and provide a reference to the default implementation
      _extend(config, this.&_impl)
    }
  }
}

void _impl(Map config = [:]) {
  // fingerprint artifacts
  this._fingerprint(config)

  // publish junit results
  this._junit(config)

  // publish code coverage
  this._jacoco(config)

  // publish findbugs
  this._findBugs(config)

  // publish pmd
  this._pmd(config)

  // publish open tasks
  this._openTasks(config)

  // publish checkstyle
  this._checkStyle(config)

  // published combined static analysis results
  this._analysisPublisher(config)
}

void _fingerprint(Map config = [:]) {
  Logger log = new Logger("defaultResultStage._fingerprint")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_FINGERPRINT)
  Map defaultConfig = [
    (STAGE_RESULTS_FINGERPRINT_ENABLED) : true,
    (STAGE_RESULTS_FINGERPRINT_FILESET) : "**/target/**/*.zip,**/target/**/*.jar",
    (MAP_MERGE_MODE): MapMergeMode.REPLACE,
  ]
  cfg = MapUtils.merge(defaultConfig, cfg)
  Boolean enabled = cfg[STAGE_RESULTS_FINGERPRINT_ENABLED]
  String fileset = cfg[STAGE_RESULTS_FINGERPRINT_FILESET]
  if (!enabled) {
    return
  }
  fingerprint(fileset)
}

void _junit(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.junit")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_JUNIT)
  Boolean enabled = cfg[STAGE_RESULTS_JUNIT_ENABLED] != null ? cfg[STAGE_RESULTS_JUNIT_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result
  junit(allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml,**/target/failsafe-reports/*.xml')
  def currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("JUNIT step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

void _jacoco(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.jacoco")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_JACOCO)
  Boolean enabled = cfg[STAGE_RESULTS_JACOCO_ENABLED] != null ? cfg[STAGE_RESULTS_JACOCO_ENABLED] : true

  if (!enabled) {
    return
  }

  String classPattern = cfg[STAGE_RESULTS_JACOCO_CLASS_PATTERN] ?: '**/target/classes'
  String execPattern = cfg[STAGE_RESULTS_JACOCO_EXEC_PATTERN] ?: '**/target/**.exec'
  String exclusionPattern = cfg[STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN] ?: ""
  String inclusionPattern = cfg[STAGE_RESULTS_JACOCO_INCLUSION_PATTERN] ?: ""
  String sourcePattern = cfg[STAGE_RESULTS_JACOCO_SOURCE_PATTERN] ?: "**/src/main/java"
  Boolean buildOverBuild = cfg[STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD] != null ? cfg[STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD] : false
  Boolean changeBuildStatus = cfg[STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS] != null ? cfg[STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS] : false
  Boolean skipCopyOfSrcFiles = cfg[STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES] != null ? cfg[STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES] : false
  String deltaBranchCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE] ?: "0"
  String deltaClassCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE] ?: "0"
  String deltaComplexityCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE] ?: "0"
  String deltaInstructionCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE] ?: "0"
  String deltaLineCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE] ?: "0"
  String deltaMethodCoverage = cfg[STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE] ?: "0"
  String minimumBranchCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE] ?: "0"
  String minimumClassCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE] ?: "0"
  String minimumComplexityCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE] ?: "0"
  String minimumInstructionCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE] ?: "0"
  String minimumLineCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE] ?: "0"
  String minimumMethodCoverage = cfg[STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE] ?: "0"
  String maximumBranchCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE] ?: "0"
  String maximumClassCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE] ?: "0"
  String maximumComplexityCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE] ?: "0"
  String maximumInstructionCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE] ?: "0"
  String maximumLineCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE] ?: "0"
  String maximumMethodCoverage = cfg[STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE] ?: "0"

  def previousBuildResult = currentBuild.result
  step([
    $class                    : 'JacocoPublisher',
    classPattern              : classPattern,
    execPattern               : execPattern,
    exclusionPattern          : exclusionPattern,
    inclusionPattern          : inclusionPattern,
    sourcePattern             : sourcePattern,
    buildOverBuild            : buildOverBuild,
    changeBuildStatus         : changeBuildStatus,
    skipCopyOfSrcFiles        : skipCopyOfSrcFiles,
    deltaBranchCoverage       : deltaBranchCoverage,
    deltaClassCoverage        : deltaClassCoverage,
    deltaComplexityCoverage   : deltaComplexityCoverage,
    deltaInstructionCoverage  : deltaInstructionCoverage,
    deltaLineCoverage         : deltaLineCoverage,
    deltaMethodCoverage       : deltaMethodCoverage,
    maximumBranchCoverage     : maximumBranchCoverage,
    maximumClassCoverage      : maximumClassCoverage,
    maximumComplexityCoverage : maximumComplexityCoverage,
    maximumInstructionCoverage: maximumInstructionCoverage,
    maximumLineCoverage       : maximumLineCoverage,
    maximumMethodCoverage     : maximumMethodCoverage,
    minimumBranchCoverage     : minimumBranchCoverage,
    minimumClassCoverage      : minimumClassCoverage,
    minimumComplexityCoverage : minimumComplexityCoverage,
    minimumInstructionCoverage: minimumInstructionCoverage,
    minimumLineCoverage       : minimumLineCoverage,
    minimumMethodCoverage     : minimumMethodCoverage,
  ])
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("JacocoPublisher step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

void _findBugs(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.findBugs")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_FINDBUGS)
  Boolean enabled = cfg[STAGE_RESULTS_FINDBUGS_ENABLED] != null ? cfg[STAGE_RESULTS_FINDBUGS_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result
  findbugs(
    canComputeNew: false,
    defaultEncoding: '',
    excludePattern: '',
    healthy: '',
    includePattern: '',
    pattern: '**/target/findbugs.xml,**/target/findbugsXml.xml,**/target/spotbugsXml.xml',
    unHealthy: ''
  )
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("findbugs step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }

}

void _pmd(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.pmd")
  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_PMD)
  Boolean enabled = cfg[STAGE_RESULTS_PMD_ENABLED] != null ? cfg[STAGE_RESULTS_PMD_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result
  pmd(
    canComputeNew: false,
    defaultEncoding: '',
    healthy: '',
    pattern: '**/target/pmd.xml',
    unHealthy: ''
  )
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("PMD step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

void _openTasks(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.openTasks")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_OPEN_TASKS)
  Boolean enabled = cfg[STAGE_RESULTS_OPEN_TASKS_ENABLED] != null ? cfg[STAGE_RESULTS_OPEN_TASKS_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result
  openTasks(
    canComputeNew: false,
    defaultEncoding: '',
    excludePattern: '**/node_modules/**,**/target/**,**/.nodejs/**,**/.rubygems/**',
    healthy: '',
    high: '',
    low: '',
    normal: 'TODO, FIXME, XXX',
    pattern: '**/*.java,**/*.js,**/*.scss,**/*.hbs,**/*.html,**/*.groovy,**/*.gspec,**/*.sh', unHealthy: ''
  )
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("OpenTasks step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

void _checkStyle(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.checkStyle")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_CHECKSTYLE)
  Boolean enabled = cfg[STAGE_RESULTS_CHECKSTYLE_ENABLED] != null ? cfg[STAGE_RESULTS_CHECKSTYLE_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result

  checkstyle(
    canComputeNew: false,
    defaultEncoding: '',
    healthy: '',
    pattern: '**/target/checkstyle-result*.xml',
    unHealthy: ''
  )
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("Checkstyle step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

void _analysisPublisher(Map config = [:]) {
  Logger log = new Logger("defaultResultStage.analysisPublisher")

  Map cfg = _getResultPluginConfig(config, STAGE_RESULTS_ANALYSIS_PUBLISHER)
  Boolean enabled = cfg[STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED] != null ? cfg[STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED] : true

  if (!enabled) {
    return
  }

  def previousBuildResult = currentBuild.result

  step([
    $class         : 'AnalysisPublisher',
    canComputeNew  : false,
    defaultEncoding: '',
    healthy        : '',
    unHealthy      : ''
  ])
  currentBuildResult = currentBuild.result
  if (currentBuildResult != previousBuildResult) {
    log.warn("AnalysisPublisher step changed build result from '$previousBuildResult' to '$currentBuildResult'")
  }
}

Map _getResultPluginConfig(Map config = [:], String pluginName) {
  Map resultStageConfig = config[STAGE_RESULTS] ?: [:]
  return resultStageConfig[pluginName] ?: [:]
}
