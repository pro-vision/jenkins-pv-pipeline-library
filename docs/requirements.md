# Requirements

* [Ansible utility role](#ansible-utility-role)

## Ansible utility role

It is recommended to use the Ansible role
[pro_vision.jenkins_pv_pipeline_library](https://github.com/pro-vision/ansible-jenkins-pv-pipeline-library)
to automatically setup/configure a Jenkins instance to work with the
pv-pipeline-library.

This role will also setup the requirements for the wcm-io-devops
[jenkins-pipeline-library](https://github.com/wcm-io-devops/jenkins-pipeline-library)
by using the role [wcm_io_devops.jenkins_pipeline_library](https://github.com/wcm-io-devops/ansible-jenkins-pipeline-library)

This role will also setup the needed script approvals!

## Jenkins + Plugins

Please refer to the
[pro_vision.jenkins_pv_pipeline_library role defaults](https://github.com/pro-vision/ansible-jenkins-pv-pipeline-library/blob/master/defaults/main.yml)
for a up to date list for the required plugins and their supported versions.

:exclamation: The library may run with newer versions, but this is not
tested.

You also have to fulfil the wcm-io-devops jenkins-pipeline-library
(requirements)[https://github.com/wcm-io-devops/jenkins-pipeline-library/blob/master/docs/requirements.md]

:bulb: For older Jenkins versions and their tested plugins you can have
a look here:
https://github.com/pro-vision/ansible-jenkins-pv-pipeline-library/releases.
