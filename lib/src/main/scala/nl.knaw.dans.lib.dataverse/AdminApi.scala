/**
 * Copyright (C) 2020 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.dataverse

import nl.knaw.dans.lib.dataverse.model.{ DatabaseSetting, Workflow }
import nl.knaw.dans.lib.logging.DebugEnhancedLogging

import java.net.URI
import scala.util.Try

class AdminApi private[dataverse](configuration: DataverseInstanceConfig) extends HttpSupport with DebugEnhancedLogging {
  protected val connectionTimeout: Int = configuration.connectionTimeout
  protected val readTimeout: Int = configuration.readTimeout
  protected val baseUrl: URI = configuration.baseUrl
  protected val apiToken: String = configuration.apiToken
  protected val sendApiTokenViaBasicAuth = false
  protected val unblockKey: Option[String] = configuration.unblockKey
  protected val builtinUserKey: Option[String] = Option.empty
  protected val apiPrefix: String = "api/admin"
  protected val apiVersion: Option[String] = Option.empty // No version allowed here

  /**
   * Returns the account data for a single user.
   *
   * @see [[https://guides.dataverse.org/en/latest/api/native-api.html#list-single-user]]
   * @param id the user ID
   * @return
   */
  def getSingleUser(id: String): Try[DataverseResponse[model.AuthenticatedUser]] = {
    trace(id)
    get[model.AuthenticatedUser](s"authenticatedUsers/$id")
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/installation/config.html#database-settings]]
   * @param settingName the name of the setting
   * @param value       the value to set
   * @return
   */
  def putDatabaseSetting(settingName: String, value: String): Try[DataverseResponse[DatabaseSetting]] = {
    trace(settingName, value)
    put[DatabaseSetting](s"settings/${ settingName }", value)
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/installation/config.html#database-settings]]
   * @param settingName the name of the setting
   * @param value       the boolean value to set
   * @return
   */
  def putDatabaseSetting(settingName: String, value: Boolean): Try[DataverseResponse[DatabaseSetting]] = {
    trace(settingName, value)
    putDatabaseSetting(settingName, value.toString)
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/installation/config.html#database-settings]]
   * @param settingName the name of the setting
   * @return
   */
  def deleteDatabaseSetting(settingName: String): Try[DataverseResponse[Nothing]] = {
    trace(settingName)
    deletePath[Nothing](s"setting/${ settingName }")
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/installation/config.html#database-settings]]
   * @param settingName the name of the setting
   * @return the current value
   */
  def getDatabaseSetting(settingName: String): Try[DataverseResponse[String]] = {
    ???
    // TODO: getDatabaseSetting
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/api/native-api.html#workflows]]
   * @return
   */
  def getWorkflows: Try[DataverseResponse[List[Workflow]]] = {
    trace(())
    get[List[Workflow]]("workflows")
  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/api/native-api.html#workflows]]
   * @return
   */
  def getWorkflow(id: Int): Try[DataverseResponse[Workflow]] = {
    trace(id)
    get[Workflow](s"workflows/$id")
  }

  //  def addWorkflow(workflow: Workflow): Try[DataverseResponse[Any]] = {
  //    trace(workflow)
  //    postJson[Workflow]()
  //  }

  /**
   * @see [[https://guides.dataverse.org/en/latest/api/native-api.html#workflows]]
   * @param triggerType PrePublishDataset or PostPublishDataset
   * @return
   */
  def getDefaultWorkflow(triggerType: String): Try[DataverseResponse[Workflow]] = {
    trace(triggerType)
    get[Workflow](s"workflows/default/$triggerType")
  }
}
