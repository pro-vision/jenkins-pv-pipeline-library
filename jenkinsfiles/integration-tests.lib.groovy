import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
import static de.provision.devops.jenkins.pipeline.utils.ConfigConstants.*
import io.wcm.devops.jenkins.pipeline.utils.logging.*
import groovy.transform.Field

@Field Logger log = new Logger(this)

Logger.init(this, config)

// the following values must be overwritten by the loading script
@Field Map externalConfig = [:]
@Field ansiblePlaybook = null
@Field ansibleInventory = null
@Field targetHosts = []

@Field Map config = [

  (PROPERTIES): [
    (PROPERTIES_PIPELINE_TRIGGERS): [],
    (PROPERTIES_PARAMETERS)       : []
  ],
  (NOTIFY)    : [:],
  (SCM)       : [
    (SCM_USE_SCM_VAR): true
  ],
  (NODE)      : null,
  (LOGLEVEL)  : LogLevel.INFO,
]

def executePipeline() {
  log.info("executePipeline")
}

return this
