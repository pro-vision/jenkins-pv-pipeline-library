# getDefaultMavenDefines

This step is a helper step that returns the default maven defines for
the current project.

Per default all of our maven projects are building with
`-DcontinuousIntegration=true` so the step will return per default:

```groovy
[
    defines : ["continuous-integration": true]
]
```

# Table of contents
* [nodejs project detection](#nodejs-project-detection)
* [Configuration Options](#configuration-options)

## nodejs project detection

Many of our projects are using nodejs for building frontend during the
maven build.

These projects need an additional define for the nodejs directory e.g.
`-Dnodejs.directory=${WORKSPACE}/target/.nodejs`

The step searches inside the workspace for `package.json` files and when
at least one result is found the define is added:

```groovy
import static io.wcm.devops.jenkins.pipeline.utils.ConfigConstants.*
[
    (MAVEN_DEFINES) : [ 
        "continuous-integration": true,
        "nodejs.directory": '${WORKSPACE}/target/.nodejs' 
    ]
]
```

## Configuration Options

The step has no configuration options at the moment.
