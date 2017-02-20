package controllers

import java.io._
import javax.inject.Inject

import ca.ellanVannin.dynatrace.DataForwarder
import ca.ellanVannin.dynatrace.data._
import ca.ellanVannin.dynatrace.shared.Loggable
import com.dynatrace.diagnostics.core.realtime.export.BtExport
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction.Type
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
    val serverName = Option(btExport.getServerName)
    val lostTransactions = Option(btExport.getLostTransactions)
    val bt = btExport.getBusinessTransactionsList.map(convertBusinessTransaction)

    val btCaseClass = BusinessTransactions(bt, lostTransactions, serverName)
    getJsonFromCaseClasses(btCaseClass)
  }

  private def convertBusinessTransaction(bt: BtExport.BusinessTransaction): BusinessTransaction = {
    val name = bt.getName
    val `type` = Option {
      bt.getType match {
        case Type.PUREPATH => PUREPATH
        case Type.USER_ACTION => USER_ACTION
        case Type.VISIT => VISIT
      }
    }

    val application = Option(bt.getApplication)
    val measureNames = bt.getMeasureNamesList.toIndexedSeq
    val dimensionNames = bt.getDimensionNamesList.toIndexedSeq
    val occurences = bt.getOccurrencesList.map(convertBtOccurence)
    val systemProfile = Option(bt.getSystemProfile)

    BusinessTransaction(name, `type`, application, measureNames, dimensionNames, occurences, systemProfile)
  }

  private def convertBtOccurence(btOccurrence: BtExport.BtOccurrence): BtOccurence = {
    val startTime = btOccurrence.getStartTime
    val endTime = Option(btOccurrence.getEndTime)
    val purePathId = Option(btOccurrence.getPurePathId)
    val values: Seq[Double] = btOccurrence.getValuesList.foldLeft(List[Double]()) {
      (seq, entry) => entry :: seq
    }
    val dimensions = btOccurrence.getDimensionsList.toIndexedSeq
    val actionName = Option(btOccurrence.getActionName)
    val url = Option(btOccurrence.getUrl)
    val query = Option(btOccurrence.getQuery)
    val visitId = Option(btOccurrence.getVisitId)
    val user = Option(btOccurrence.getUser)
    val apdex = Option(btOccurrence.getApdex)
    val converted = Option(btOccurrence.getConverted)
    val responseTime = Option(btOccurrence.getResponseTime)
    val cpuTime = Option(btOccurrence.getCpuTime)
    val syncTime = Option(btOccurrence.getSyncTime)
    val waitTime = Option(btOccurrence.getWaitTime)
    val suspensionTime = Option(btOccurrence.getSuspensionTime)
    val execTime = Option(btOccurrence.getExecTime)
    val duration = Option(btOccurrence.getDuration)
    val failed = Option(btOccurrence.getFailed)

    val nrOfActions = Option(btOccurrence.getNrOfActions)
    val clientFamily = Option(btOccurrence.getClientFamily)
    val clientIP = Option(btOccurrence.getClientIP)
    val continent = Option(btOccurrence.getContinent)
    val country = Option(btOccurrence.getCountry)
    val city = Option(btOccurrence.getCity)
    val failedAction = Option(btOccurrence.getFailedActions)
    val clientErrors = Option(btOccurrence.getClientErrors)
    val exitActionFailed = Option(btOccurrence.getExitActionFailed)
    val bounce = Option(btOccurrence.getBounce)
    val osFamily = Option(btOccurrence.getOsFamily)
    val osName = Option(btOccurrence.getOsName)
    val connectionType = Option(btOccurrence.getConnectionType)
    val convertedBy = btOccurrence.getConvertedByList.toIndexedSeq

    val clientTime = Option(btOccurrence.getClientTime)
    val networkTime = Option(btOccurrence.getNetworkTime)
    val serverTime = Option(btOccurrence.getServerTime)
    val urlRedirectionTime = Option(btOccurrence.getUrlRedirectionTime)
    val dnsTime = Option(btOccurrence.getDnsTime)
    val connectTime = Option(btOccurrence.getConnectTime)
    val sslTime = Option(btOccurrence.getSslTime)
    val documentRequestTime = Option(btOccurrence.getDocumentRequestTime)
    val documentResponseTime = Option(btOccurrence.getDocumentResponseTime)
    val processingTime = Option(btOccurrence.getProcessingTime)

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

  def getJsonFromCaseClasses(caseClass: BusinessTransactions): JsValue = {
    Json.toJson(caseClass)
  }
}