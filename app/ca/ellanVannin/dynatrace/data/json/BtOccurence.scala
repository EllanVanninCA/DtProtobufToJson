package ca.ellanVannin.dynatrace.data.json

import play.api.libs.json.{Json, Writes}

/**
  * Created by Chris on 2017-02-14.
  */
case class BtOccurence(startTime: Long,
                       endTime: Option[Long],
                       purePathId: Option[String],
                       values: Seq[Double],
                       dimensions: Seq[String],
                       actionName: Option[String],
                       url: Option[String],
                       query: Option[String],
                       visitId: Option[Long],
                       user: Option[String],
                       apdex: Option[Double],
                       converted: Option[Boolean],
                       responseTime: Option[Double],
                       cpuTime: Option[Double],
                       syncTime: Option[Double],
                       waitTime: Option[Double],
                       suspensionTime: Option[Double],
                       execTime: Option[Double],
                       duration: Option[Double],
                       failed: Option[Boolean],

                       nrOfActions: Option[Int],
                       clientFamily: Option[String],
                       clientIP: Option[String],
                       continent: Option[String],
                       country: Option[String],
                       city: Option[String],
                       failedActions: Option[Int],
                       clientErrors: Option[Int],
                       exitActionFailed: Option[Boolean],
                       bounce: Option[Boolean],
                       osFamily: Option[String],
                       osName: Option[String],
                       connectionType: Option[String],
                       convertedBy: Seq[String],

                       clientTime: Option[Double],
                       networkTime: Option[Double],
                       serverTime: Option[Double],
                       urlRedirectionTime: Option[Int],
                       dnsTime: Option[Int],
                       connectTime: Option[Int],
                       sslTime: Option[Int],
                       documentRequestTime: Option[Int],
                       documentResponseTime: Option[Int],
                       processingTime: Option[Int]
                      )

object BtOccurence {
  val START_TIME_KEY = 'startTime
  val END_TIME_KEY = 'endTime
  val PUREPATH_ID_KEY = 'purePathId
  val VALUES_KEY = 'values
  val DIMENSIONS_KEY = 'dimensions
  val ACTION_NAME_KEY = 'actionName
  val URL_KEY = 'url
  val QUERY_KEY = 'query
  val VISIT_ID_KEY = 'visitId
  val USER_KEY = 'user
  val APDEX_KEY = 'apdex
  val CONVERTED_KEY = 'converted
  val RESPONSE_TIME_KEY = 'responseTime
  val CPU_TIME_KEY = 'cpuTime
  val SYNC_TIME_KEY = 'syncTime
  val WAIT_TIME_KEY = 'waitTime
  val SUSPENSION_TIME_KEY = 'suspensionTime
  val EXEC_TIME_KEY = 'execTime
  val DURATION_KEY = 'duration
  val FAILED_KEY = 'failed

  val NR_OF_ACTIONS_KEY = 'nrOfActions
  val CLIENT_FAMILY_KEY = 'clientFamily
  val CLIENT_IP_KEY = 'clientIP
  val CONTINENT_KEY = 'continent
  val COUNTRY_KEY = 'country
  val CITY_KEY = 'city
  val FAILED_ACTIONS_KEY = 'failedActions
  val CLIENT_ERRORS_KEY = 'clientErrors
  val EXIT_ACTION_FAILED = 'exitActionFailed
  val BOUNCE_KEY = 'bounce
  val OS_FAMILY_KEY = 'osFamily
  val OS_NAME_KEY = 'osName
  val CONNECTION_TYPE_KEY = 'connectionType
  val CONVERTED_BY_KEY = 'convertedBy

  val CLIENT_TIME_KEY = 'clientTime
  val NETWORK_TIME_KEY = 'networkTime
  val SERVER_TIME_KEY = 'serverTime
  val URL_REDIRECTION_TIME_KEY = 'urlRedirectionTime
  val DNS_TIME_KEY = 'dnsTime
  val CONNECT_TIME_KEY = 'connectTime
  val SSL_TIME_KEY = 'sslTime
  val DOCUMENT_REQUEST_TIME = 'documentRequestTime
  val DOCUMENT_RESPONSE_TIME = 'documentResponseTime
  val PROCESSING_TIME_KEY = 'processingTime

  implicit val btOccurenceWrites = new Writes[BtOccurence] {
    def writes(o: BtOccurence) = Json.obj(
      START_TIME_KEY.name -> o.startTime,
      END_TIME_KEY.name -> o.endTime,
      PUREPATH_ID_KEY.name -> o.purePathId,
      VALUES_KEY.name -> o.values,
      DIMENSIONS_KEY.name -> o.dimensions,
      ACTION_NAME_KEY.name -> o.actionName,
      URL_KEY.name -> o.url,
      QUERY_KEY.name -> o.query,
      VISIT_ID_KEY.name -> o.visitId,
      USER_KEY.name -> o.user,
      APDEX_KEY.name -> o.apdex,
      CONVERTED_KEY.name -> o.converted,
      RESPONSE_TIME_KEY.name -> o.responseTime,
      CPU_TIME_KEY.name -> o.cpuTime,
      SYNC_TIME_KEY.name -> o.syncTime,
      WAIT_TIME_KEY.name -> o.waitTime,
      SUSPENSION_TIME_KEY.name -> o.suspensionTime,
      EXEC_TIME_KEY.name -> o.execTime,
      DURATION_KEY.name -> o.duration,
      FAILED_KEY.name -> o.failed,

      NR_OF_ACTIONS_KEY.name -> o.nrOfActions,
      CLIENT_FAMILY_KEY.name -> o.clientFamily,
      CLIENT_IP_KEY.name -> o.clientIP,
      CONTINENT_KEY.name -> o.continent,
      COUNTRY_KEY.name -> o.country,
      CITY_KEY.name -> o.city,
      FAILED_ACTIONS_KEY.name -> o.failedActions,
      CLIENT_ERRORS_KEY.name -> o.clientErrors,
      EXIT_ACTION_FAILED.name -> o.exitActionFailed,
      BOUNCE_KEY.name -> o.bounce,
      OS_FAMILY_KEY.name -> o.osFamily,
      OS_NAME_KEY.name -> o.osName,
      CONNECTION_TYPE_KEY.name -> o.connectionType,
      CONVERTED_BY_KEY.name -> o.convertedBy,

      CLIENT_TIME_KEY.name -> o.clientTime,
      NETWORK_TIME_KEY.name -> o.networkTime,
      SERVER_TIME_KEY.name -> o.serverTime,
      URL_REDIRECTION_TIME_KEY.name -> o.urlRedirectionTime,
      DNS_TIME_KEY.name -> o.dnsTime,
      CONNECT_TIME_KEY.name -> o.connectTime,
      SSL_TIME_KEY.name -> o.sslTime,
      DOCUMENT_REQUEST_TIME.name -> o.documentRequestTime,
      DOCUMENT_RESPONSE_TIME.name -> o.documentResponseTime,
      PROCESSING_TIME_KEY.name -> o.processingTime
    )
  }
}
