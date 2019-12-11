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
package de.provision.devops.jenkins.pipeline.utils

/**
 * Constants for configuration values. Used for passing configuration options into the library steps
 */
class ConfigConstants {

  public static final String BUILD_DEFAULT = "buildDefault"
  public static final String BUILD_DEFAULT_PRE_EXTENSIONS = "preExtensions"
  public static final String BUILD_DEFAULT_POST_EXTENSIONS = "postExtensions"

  public static final String JOB_TYPE = "jobType"
  public static final String JOB_TYPE_FEATURE = "feature"

  public static final String NODE = "node"

  public static final String PROPERTIES = "properties"

  public static final String PROPERTIES_BUILD_DISCARDER = "buildDiscarder"
  public static final String PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP = "artifactDaysToKeep"
  public static final String PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP = "artifactsNumToKeep"
  public static final String PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP = "daysToKeep"
  public static final String PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP = "numToKeep"
  public static final String PROPERTIES_CUSTOM = "customProperties"
  public static final String PROPERTIES_DISABLE_CONCURRENT_BUILDS = "disableConcurrentBuilds"
  public static final String PROPERTIES_PARAMETERS = "parameters"
  public static final String PROPERTIES_PIPELINE_TRIGGERS = "pipelineTriggers"

  public static final String STAGE_COMPILE = "compile"
  public static final String STAGE_COMPILE_EXTEND = "_extend"

  public static final String STAGE_ANALYZE = "analyze"
  public static final String STAGE_ANALYZE_EXTEND = "_extend"

  public static final String STAGE_PREPARATION = "preparationStage"

  public static final String STAGE_PREPARATION_CHECKOUT_SCM = "checkoutScm"
  public static final String STAGE_PREPARATION_EXTEND = "_extend"
  public static final String STAGE_PREPARATION_PURGE_SHAPSHOTS = "purgeSnapshots"
  public static final String STAGE_PREPARATION_SET_BUILD_NAME = "setBuildName"
  public static final String STAGE_PREPARATION_SETUP_TOOLS = "setupTools"
  public static final String STAGE_PREPARATION_STAGE_WRAP = "stageWrap"

  public static final String STAGE_RESULTS = "stageResults"

  public static final String STAGE_RESULTS_ANALYSIS_PUBLISHER = "analysisPublisher"
  public static final String STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED = "enabled"

  public static final String STAGE_RESULTS_CHECKSTYLE = "checkstyle"
  public static final String STAGE_RESULTS_CHECKSTYLE_ENABLED = "enabled"

  public static final String STAGE_RESULTS_EXTEND = "_extend"

  public static final String STAGE_RESULTS_JACOCO = "jacoco"
  public static final String STAGE_RESULTS_JACOCO_ENABLED = "enabled"
  public static final String STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD = "buildOverBuild"
  public static final String STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS = "changeBuildStatus"
  public static final String STAGE_RESULTS_JACOCO_CLASS_PATTERN = "classPattern"
  public static final String STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE = "deltaBranchCoverage"
  public static final String STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE = "deltaClassCoverage"
  public static final String STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE = "deltaComplexityCoverage"
  public static final String STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE = "deltaInstructionCoverage"
  public static final String STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE = "deltaLineCoverage"
  public static final String STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE = "deltaMethodCoverage"
  public static final String STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN = "exclusionPattern"
  public static final String STAGE_RESULTS_JACOCO_EXEC_PATTERN = "execPattern"
  public static final String STAGE_RESULTS_JACOCO_INCLUSION_PATTERN = "inclusionPattern"

  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE = "maximumBranchCoverage"
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE = "maximumClassCoverage"
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE = "maximumComplexityCoverage"
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE = "maximumInstructionCoverage"
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE = "maximumLineCoverage"
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE = "maximumMethodCoverage"

  public static final String STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE = "minimumBranchCoverage"
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE = "minimumClassCoverage"
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE = "minimumComplexityCoverage"
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE = "minimumInstructionCoverage"
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE = "minimumLineCoverage"
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE = "minimumMethodCoverage"

  public static final String STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES = "skipCopyOfSrcFiles"
  public static final String STAGE_RESULTS_JACOCO_SOURCE_PATTERN = "sourcePattern"

  public static final String STAGE_RESULTS_FINDBUGS = "findbugs"
  public static final String STAGE_RESULTS_FINDBUGS_ENABLED = "enabled"

  public static final String STAGE_RESULTS_JUNIT = "junit"
  public static final String STAGE_RESULTS_JUNIT_ENABLED = "enabled"

  public static final String STAGE_RESULTS_OPEN_TASKS = "openTasks"
  public static final String STAGE_RESULTS_OPEN_TASKS_ENABLED = "enabled"

  public static final String STAGE_RESULTS_PMD = "pmd"
  public static final String STAGE_RESULTS_PMD_ENABLED = "enabled"


  public static final String STASH = "stash"

  public static final String STASH_COMPILE_FILES = "compileFiles"
  public static final String STASH_ANALYZE_FILES = "analyzeFiles"

  public static final String TIMEOUT = "timeout"
  public static final String TIMEOUT_TIME = "timeoutTime"
  public static final String TIMEOUT_UNIT = "timeoutUnit"

  public static final String TOOL_MAVEN = "maven"
  public static final String TOOL_JDK = "jdk"

}
