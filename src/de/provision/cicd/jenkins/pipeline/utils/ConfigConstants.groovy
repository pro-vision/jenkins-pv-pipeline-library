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
package de.provision.cicd.jenkins.pipeline.utils

import de.provision.devops.jenkins.pipeline.utils.ConfigConstants as NewConfigConstants

/**
 * Legacy constants for configuration.
 * This class was kept as adapter class for legacy projects accessing the old package
 */
@Deprecated
class ConfigConstants {

  public static final String JOB_TYPE = NewConfigConstants.JOB_TYPE
  public static final String JOB_TYPE_FEATURE = NewConfigConstants.JOB_TYPE_FEATURE

  public static final String NODE = NewConfigConstants.NODE

  public static final String PROPERTIES = NewConfigConstants.PROPERTIES

  public static final String PROPERTIES_PIPELINE_TRIGGERS = NewConfigConstants.PROPERTIES_PIPELINE_TRIGGERS
  public static final String PROPERTIES_PARAMETERS = NewConfigConstants.PROPERTIES_PARAMETERS
  public static final String PROPERTIES_BUILD_DISCARDER = NewConfigConstants.PROPERTIES_BUILD_DISCARDER
  public static final String PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP = NewConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP
  public static final String PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP = NewConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP
  public static final String PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP = NewConfigConstants.PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP
  public static final String PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP = NewConfigConstants.PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP
  public static final String PROPERTIES_DISABLE_CONCURRENT_BUILDS = NewConfigConstants.PROPERTIES_DISABLE_CONCURRENT_BUILDS


  public static final String STAGE_COMPILE = NewConfigConstants.STAGE_COMPILE
  public static final String STAGE_ANALYZE = NewConfigConstants.STAGE_ANALYZE

  public static final String STAGE_PREPARATION = NewConfigConstants.STAGE_PREPARATION

  public static final String STAGE_PREPARATION_CHECKOUT_SCM = NewConfigConstants.STAGE_PREPARATION_CHECKOUT_SCM
  public static final String STAGE_PREPARATION_PURGE_SHAPSHOTS = NewConfigConstants.STAGE_PREPARATION_PURGE_SHAPSHOTS
  public static final String STAGE_PREPARATION_SET_BUILD_NAME = NewConfigConstants.STAGE_PREPARATION_SET_BUILD_NAME
  public static final String STAGE_PREPARATION_SETUP_TOOLS = NewConfigConstants.STAGE_PREPARATION_SETUP_TOOLS

  public static final String STAGE_RESULTS = NewConfigConstants.STAGE_RESULTS

  public static final String STAGE_RESULTS_ANALYSIS_PUBLISHER = NewConfigConstants.STAGE_RESULTS_ANALYSIS_PUBLISHER
  public static final String STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED = NewConfigConstants.STAGE_RESULTS_ANALYSIS_PUBLISHER_ENABLED

  public static final String STAGE_RESULTS_CHECKSTYLE = NewConfigConstants.STAGE_RESULTS_CHECKSTYLE
  public static final String STAGE_RESULTS_CHECKSTYLE_ENABLED = NewConfigConstants.STAGE_RESULTS_CHECKSTYLE_ENABLED

  public static final String STAGE_RESULTS_JACOCO = NewConfigConstants.STAGE_RESULTS_JACOCO
  public static final String STAGE_RESULTS_JACOCO_ENABLED = NewConfigConstants.STAGE_RESULTS_JACOCO_ENABLED
  public static final String STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD = NewConfigConstants.STAGE_RESULTS_JACOCO_BUILD_OVER_BUILD
  public static final String STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS = NewConfigConstants.STAGE_RESULTS_JACOCO_CHANGE_BUILD_STATUS
  public static final String STAGE_RESULTS_JACOCO_CLASS_PATTERN = NewConfigConstants.STAGE_RESULTS_JACOCO_CLASS_PATTERN
  public static final String STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_BRANCH_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_CLASS_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_COMPLEXITY_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_INSTRUCTION_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_LINE_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_DELTA_METHOD_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN = NewConfigConstants.STAGE_RESULTS_JACOCO_EXCLUSION_PATTERN
  public static final String STAGE_RESULTS_JACOCO_EXEC_PATTERN = NewConfigConstants.STAGE_RESULTS_JACOCO_EXEC_PATTERN
  public static final String STAGE_RESULTS_JACOCO_INCLUSION_PATTERN = NewConfigConstants.STAGE_RESULTS_JACOCO_INCLUSION_PATTERN

  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_BRANCH_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_CLASS_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_COMPLEXITY_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_INSTRUCTION_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_LINE_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MAXIMUM_METHOD_COVERAGE

  public static final String STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_BRANCH_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_CLASS_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_COMPLEXITY_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_INSTRUCTION_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_LINE_COVERAGE
  public static final String STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE = NewConfigConstants.STAGE_RESULTS_JACOCO_MINIMUM_METHOD_COVERAGE

  public static final String STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES = NewConfigConstants.STAGE_RESULTS_JACOCO_SKIP_COPY_OF_SRC_FILES
  public static final String STAGE_RESULTS_JACOCO_SOURCE_PATTERN = NewConfigConstants.STAGE_RESULTS_JACOCO_SOURCE_PATTERN

  public static final String STAGE_RESULTS_FINDBUGS = NewConfigConstants.STAGE_RESULTS_FINDBUGS
  public static final String STAGE_RESULTS_FINDBUGS_ENABLED = NewConfigConstants.STAGE_RESULTS_FINDBUGS_ENABLED

  public static final String STAGE_RESULTS_JUNIT = NewConfigConstants.STAGE_RESULTS_JUNIT
  public static final String STAGE_RESULTS_JUNIT_ENABLED = NewConfigConstants.STAGE_RESULTS_JUNIT_ENABLED

  public static final String STAGE_RESULTS_OPEN_TASKS = NewConfigConstants.STAGE_RESULTS_OPEN_TASKS
  public static final String STAGE_RESULTS_OPEN_TASKS_ENABLED = NewConfigConstants.STAGE_RESULTS_OPEN_TASKS_ENABLED

  public static final String STAGE_RESULTS_PMD = NewConfigConstants.STAGE_RESULTS_PMD
  public static final String STAGE_RESULTS_PMD_ENABLED = NewConfigConstants.STAGE_RESULTS_PMD_ENABLED


  public static final String STASH = NewConfigConstants.STASH

  public static final String STASH_COMPILE_FILES = NewConfigConstants.STASH_COMPILE_FILES
  public static final String STASH_ANALYZE_FILES = NewConfigConstants.STASH_ANALYZE_FILES

  public static final String TIMEOUT = NewConfigConstants.TIMEOUT
  public static final String TIMEOUT_TIME = NewConfigConstants.TIMEOUT_TIME
  public static final String TIMEOUT_UNIT = NewConfigConstants.TIMEOUT_UNIT

  public static final String TOOL_MAVEN = NewConfigConstants.TOOL_MAVEN
  public static final String TOOL_JDK = NewConfigConstants.TOOL_JDK
}
