/**
 * Integration tests for pipeline to determine sandbox/cps problems in pipeline classes after jenkins update
 * To detect problems caused by plugin updates in most cases the instance creation of a class is sufficient to cause a
 * cps/sandbox exception.
 */
@Library('pipeline-library') pipelineLibrary
@Library('pv-pipeline-library') pvPipelineLibrary


import de.provision.devops.jenkins.pipeline.PipelineConfiguration
import de.provision.devops.jenkins.pipeline.utils.ConfigConstants
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.IntegrationTestHelper
import io.wcm.devops.jenkins.pipeline.utils.logging.LogLevel
import io.wcm.devops.jenkins.pipeline.utils.logging.Logger
import org.jenkinsci.plugins.workflow.libs.Library

// job properties

properties([
  disableConcurrentBuilds(),
  pipelineTriggers([pollSCM('H * * * * ')])
])

Logger.init(this, LogLevel.INFO)
Logger log = new Logger(this)

integrationTestUtils.runTestsOnPackage("jenkins") {
  integrationTestUtils.runTest("JENKINS-56682 load lib test") {
    node() {
      checkout scm
      def rootDir = pwd()
      pipelineScript = load "${rootDir}/jenkinsfiles/integration-tests.lib.groovy"
    }
    pipelineScript.externalConfig = [
      (PROPERTIES): [
        (PROPERTIES_PIPELINE_TRIGGERS): [],
      ],
    ]
    // run the pipeline
    pipelineScript.executePipeline()
  }
}

node() {

  integrationTestUtils.integrationTestUtils.runTestsOnPackage("de.provision.devops.jenkins.pipeline") {
    integrationTestUtils.runTest("PipelineConfiguration") {
      PipelineConfiguration configuration = new PipelineConfiguration(this, "test/pv-pipeline-library/config/config.yaml")
      integrationTestUtils.assertEquals("defaultJDK",configuration.getJdk())
      integrationTestUtils.assertEquals("defaultMaven",configuration.getMaven())
      integrationTestUtils.assertEquals("defaultNodeJS",configuration.getNodeJs())
      integrationTestUtils.assertEquals("maven-test1",configuration.getMaven("11__22__3_"))
      integrationTestUtils.assertEquals("maven-test2",configuration.getMaven("_11___22_3__4"))
      integrationTestUtils.assertEquals("maven-test3",configuration.getMaven("11__22__a_"))
      integrationTestUtils.assertEquals(['H/15 * * * 0-6'],configuration.getDefaultSCMPolling())
    }
  }

  integrationTestUtils.integrationTestUtils.runTestsOnPackage("de.provision.devops.jenkins.pipeline.utils") {
    integrationTestUtils.runTest("ConfigConstants") {
      ConfigConstants configConstants = new ConfigConstants()
      log.info(ConfigConstants.JOB_TYPE, ConfigConstants.JOB_TYPE)
      log.info(ConfigConstants.JOB_TYPE_FEATURE, ConfigConstants.JOB_TYPE_FEATURE)
      log.info(ConfigConstants.NODE, ConfigConstants.NODE)
      log.info(ConfigConstants.STAGE_PREPARATION, ConfigConstants.STAGE_PREPARATION)
      log.info(ConfigConstants.STAGE_PREPARATION_CHECKOUT_SCM, ConfigConstants.STAGE_PREPARATION_CHECKOUT_SCM)
      log.info(ConfigConstants.STAGE_PREPARATION_PURGE_SHAPSHOTS, ConfigConstants.STAGE_PREPARATION_PURGE_SHAPSHOTS)
      log.info(ConfigConstants.STAGE_PREPARATION_SET_BUILD_NAME, ConfigConstants.STAGE_PREPARATION_SET_BUILD_NAME)
      log.info(ConfigConstants.STAGE_PREPARATION_SETUP_TOOLS, ConfigConstants.STAGE_PREPARATION_SETUP_TOOLS)
      log.info(ConfigConstants.PROPERTIES, ConfigConstants.PROPERTIES)
      log.info(ConfigConstants.PROPERTIES_PIPELINE_TRIGGERS, ConfigConstants.PROPERTIES_PIPELINE_TRIGGERS)
      log.info(ConfigConstants.PROPERTIES_PARAMETERS, ConfigConstants.PROPERTIES_PARAMETERS)
      log.info(ConfigConstants.PROPERTIES_BUILD_DISCARDER, ConfigConstants.PROPERTIES_BUILD_DISCARDER)
      log.info(ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP, ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_DAYS_TO_KEEP)
      log.info(ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP, ConfigConstants.PROPERTIES_BUILD_DISCARDER_ARTIFACT_NUM_TO_KEEP)
      log.info(ConfigConstants.PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP, ConfigConstants.PROPERTIES_BUILD_DISCARDER_DAYS_TO_KEEP)
      log.info(ConfigConstants.PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP, ConfigConstants.PROPERTIES_BUILD_DISCARDER_NUM_TO_KEEP)
      log.info(ConfigConstants.PROPERTIES_DISABLE_CONCURRENT_BUILDS, ConfigConstants.PROPERTIES_DISABLE_CONCURRENT_BUILDS)
      log.info(ConfigConstants.STAGE_COMPILE, ConfigConstants.STAGE_COMPILE)
      log.info(ConfigConstants.STAGE_ANALYZE, ConfigConstants.STAGE_ANALYZE)
      log.info(ConfigConstants.STASH, ConfigConstants.STASH)
      log.info(ConfigConstants.STASH_COMPILE_FILES, ConfigConstants.STASH_COMPILE_FILES)
      log.info(ConfigConstants.STASH_ANALYZE_FILES, ConfigConstants.STASH_ANALYZE_FILES)
      log.info(ConfigConstants.TIMEOUT, ConfigConstants.TIMEOUT)
      log.info(ConfigConstants.TIMEOUT_TIME, ConfigConstants.TIMEOUT_TIME)
      log.info(ConfigConstants.TIMEOUT_UNIT, ConfigConstants.TIMEOUT_UNIT)
      log.info(ConfigConstants.TOOL_MAVEN, ConfigConstants.TOOL_MAVEN)
      log.info(ConfigConstants.TOOL_JDK, ConfigConstants.TOOL_JDK)
    }
  }

  stage("Result overview") {
    integrationTestUtils.logTestResults(IntegrationTestHelper.getResults())
  }
  stage("Check") {
    integrationTestUtils.processFailedTests(IntegrationTestHelper.getResults())
  }
}
