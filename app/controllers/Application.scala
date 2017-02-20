package controllers

import java.io._
import javax.inject.Inject

import ca.ellanVannin.dynatrace.DataForwarder
import ca.ellanVannin.dynatrace.data._
import ca.ellanVannin.dynatrace.shared.Loggable
import com.dynatrace.diagnostics.core.realtime.export.BtExport
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction.Type
import com.google.protobuf.GeneratedMessage
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.collection.JavaConversions._
import scala.concurrent.Future

class Application @Inject()(ws: WSClient) extends Controller with Loggable {
  val MAX_MEMORY: Long = 10 * 1024 * 1024l
  val dataForwarder: DataForwarder = new DataForwarder()

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def transformToJson = Action.async(parse.anyContent(Option(MAX_MEMORY))) {
    req =>
      req.body.asRaw flatMap {
        _.asBytes(MAX_MEMORY) map getJsonFromByteArray
      } match {
        case Some(j) =>
          dataForwarder.sendData(j)(ws)
          System.out.println(s"Response: $j")
          Future.successful(Ok(j))
        case _ =>
          System.err.println("None returned")
          Future.successful(BadRequest("Error"))
      }
  }

  def getJsonFromByteArray(array: Array[Byte]): JsValue = {
    val bos = new ByteArrayInputStream(array)

    val protobuf = BtExport.BusinessTransactions.parseFrom(bos)
    getJsonFromProtobufObject(protobuf)
  }

  def getJsonFromProtobufObject(btExport: BtExport.BusinessTransactions): JsValue = {
    val serverName = getOptionalValue(btExport.hasServerName)(btExport.getServerName)
    val lostTransactions = getOptionalValue(btExport.hasLostTransactions)(btExport.getLostTransactions)
    val bt = btExport.getBusinessTransactionsList.map(convertBusinessTransaction)

    val btCaseClass = BusinessTransactions(bt, lostTransactions, serverName)
    getJsonFromCaseClasses(btCaseClass)
  }

  private def convertBusinessTransaction(bt: BtExport.BusinessTransaction): BusinessTransaction = {
    val name = bt.getName
    val `type` = getOptionalValue(bt.hasType){
        bt.getType match {
          case Type.PUREPATH => PUREPATH
          case Type.USER_ACTION => USER_ACTION
          case Type.VISIT => VISIT
        }
      }

    val application = getOptionalValue(bt.hasApplication)(bt.getApplication)
    val measureNames = bt.getMeasureNamesList.toIndexedSeq
    val dimensionNames = bt.getDimensionNamesList.toIndexedSeq
    val occurences = bt.getOccurrencesList.map(convertBtOccurence)
    val systemProfile = getOptionalValue(bt.hasSystemProfile)(bt.getSystemProfile)

    BusinessTransaction(name, `type`, application, measureNames, dimensionNames, occurences, systemProfile)
  }

  private def convertBtOccurence(btOccurrence: BtExport.BtOccurrence): BtOccurence = {
    val startTime = btOccurrence.getStartTime
    val endTime = getOptionalValue(btOccurrence.hasEndTime)(btOccurrence.getEndTime)
    val purePathId = getOptionalValue(btOccurrence.hasPurePathId)(btOccurrence.getPurePathId)

    val values: Seq[Double] = btOccurrence.getValuesList.foldLeft(List[Double]()) {
      (seq, entry) => entry :: seq
    }
    val dimensions = btOccurrence.getDimensionsList.toIndexedSeq
    val actionName = getOptionalValue(btOccurrence.hasActionName)(btOccurrence.getActionName)
    val url = getOptionalValue(btOccurrence.hasUrl)(btOccurrence.getUrl)
    val query = getOptionalValue(btOccurrence.hasQuery)(btOccurrence.getQuery)

    val visitId = getOptionalValue(btOccurrence.hasVisitId)(btOccurrence.getVisitId)
    val user = getOptionalValue(btOccurrence.hasUser)(btOccurrence.getUser)
    val apdex = getOptionalValue(btOccurrence.hasApdex)(btOccurrence.getApdex)
    val converted = getOptionalValue(btOccurrence.hasConverted)(btOccurrence.getConverted)
    val responseTime = getOptionalValue(btOccurrence.hasResponseTime)(btOccurrence.getResponseTime)
    val cpuTime = getOptionalValue(btOccurrence.hasCpuTime)(btOccurrence.getCpuTime)
    val syncTime = getOptionalValue(btOccurrence.hasSyncTime)(btOccurrence.getSyncTime)
    val waitTime = getOptionalValue(btOccurrence.hasWaitTime)(btOccurrence.getWaitTime)
    val suspensionTime = getOptionalValue(btOccurrence.hasSuspensionTime)(btOccurrence.getSuspensionTime)
    val execTime = getOptionalValue(btOccurrence.hasExecTime)(btOccurrence.getExecTime)
    val duration = getOptionalValue(btOccurrence.hasDuration)(btOccurrence.getDuration)
    val failed = getOptionalValue(btOccurrence.hasFailed)(btOccurrence.getFailed)

    val nrOfActions = getOptionalValue(btOccurrence.hasNrOfActions)(btOccurrence.getNrOfActions)
    val clientFamily = getOptionalValue(btOccurrence.hasClientFamily)(btOccurrence.getClientFamily)
    val clientIP = getOptionalValue(btOccurrence.hasClientIP)(btOccurrence.getClientIP)
    val continent = getOptionalValue(btOccurrence.hasContinent)(btOccurrence.getContinent)
    val country = getOptionalValue(btOccurrence.hasCountry)(btOccurrence.getCountry)
    val city = getOptionalValue(btOccurrence.hasCity)(btOccurrence.getCity)
    val failedAction = getOptionalValue(btOccurrence.hasFailedActions)(btOccurrence.getFailedActions)
    val clientErrors = getOptionalValue(btOccurrence.hasClientErrors)(btOccurrence.getClientErrors)
    val exitActionFailed = getOptionalValue(btOccurrence.hasExitActionFailed)(btOccurrence.getExitActionFailed)
    val bounce = getOptionalValue(btOccurrence.hasBounce)(btOccurrence.getBounce)
    val osFamily = getOptionalValue(btOccurrence.hasOsFamily)(btOccurrence.getOsFamily)
    val osName = getOptionalValue(btOccurrence.hasOsName)(btOccurrence.getOsName)
    val connectionType = getOptionalValue(btOccurrence.hasConnectionType)(btOccurrence.getConnectionType)
    val convertedBy = btOccurrence.getConvertedByList.toIndexedSeq

    val clientTime = getOptionalValue(btOccurrence.hasClientTime)(btOccurrence.getClientTime)
    val networkTime = getOptionalValue(btOccurrence.hasNetworkTime)(btOccurrence.getNetworkTime)
    val serverTime = getOptionalValue(btOccurrence.hasServerTime)(btOccurrence.getServerTime)
    val urlRedirectionTime = getOptionalValue(btOccurrence.hasUrlRedirectionTime)(btOccurrence.getUrlRedirectionTime)
    val dnsTime = getOptionalValue(btOccurrence.hasDnsTime)(btOccurrence.getDnsTime)
    val connectTime = getOptionalValue(btOccurrence.hasConnectTime)(btOccurrence.getConnectTime)
    val sslTime = getOptionalValue(btOccurrence.hasSslTime)(btOccurrence.getSslTime)
    val documentRequestTime = getOptionalValue(btOccurrence.hasDocumentRequestTime)(btOccurrence.getDocumentRequestTime)
    val documentResponseTime = getOptionalValue(btOccurrence.hasDocumentResponseTime)(btOccurrence.getDocumentResponseTime)
    val processingTime = getOptionalValue(btOccurrence.hasProcessingTime)(btOccurrence.getProcessingTime)

    BtOccurence(startTime,
      endTime,
      purePathId,
      values,
      dimensions,
      actionName,
      url,
      query,
      visitId,
      user,
      apdex,
      converted,
      responseTime,
      cpuTime,
      syncTime,
      waitTime,
      suspensionTime,
      execTime,
      duration,
      failed,

      nrOfActions,
      clientFamily,
      clientIP,
      continent,
      country,
      city,
      failedAction,
      clientErrors,
      exitActionFailed,
      bounce,
      osFamily,
      osName,
      connectionType,
      convertedBy,

      clientTime,
      networkTime,
      serverTime,
      urlRedirectionTime,
      dnsTime,
      connectTime,
      sslTime,
      documentRequestTime,
      documentResponseTime,
      processingTime)
  }

  private def getOptionalValue[V](notNullChecker: Boolean)(getter: => V): Option[V] = {
    if (notNullChecker)
      Option(getter)
    else
      None
  }

  def getJsonFromCaseClasses(caseClass: BusinessTransactions): JsValue = {
    Json.toJson(caseClass)
  }
}