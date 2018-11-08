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

/**
 * Constants for steps
 */
class PVStepConstants {

  public final static String BUILD_FEATURE = "buildFeature"
  public final static String BUILD_DEFAULT = "buildDefault"
  public final static String SETUP_PVTOOLS = "setupPVTools"
  public final static String DELETE_ARTIFACTS_FROM_REPOSITORY = "deleteArtifactsFromRepository"
  public final static String PURGE_SNAPSHOTS_FROM_REPOSITORY = "purgeSnapshotsFromRepository"
  public final static String DELETE_PROJECT_ARTIFACTS_FROM_REPOSITORY = "deleteProjectArtifactsFromRepository"
  public final static String SET_DEFAULT_PROPERTIES = "setDefaultProperties"

}
