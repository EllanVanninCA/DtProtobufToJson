package controllers

import java.io._
import javax.inject.Inject

import ca.ellanVannin.dynatrace.DataForwarder
import ca.ellanVannin.dynatrace.data.json._
import ca.ellanVannin.dynatrace.data.protobuf.Protobuf
import ca.ellanVannin.dynatrace.shared.Loggable
import com.dynatrace.diagnostics.core.realtime.export.BtExport
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.Future

class Application @Inject()(ws: WSClient) extends Controller with Loggable {
  val MAX_MEMORY: Long = 10 * 1024 * 1024l
  val dataForwarder: DataForwarder = new DataForwarder()

  def transformToJson = Action.async(parse.anyContent(Option(MAX_MEMORY))) {
    req =>
      req.body.asRaw flatMap {
        _.asBytes(MAX_MEMORY) map getJsonFromByteArray
      } match {
        case Some(j) =>
          dataForwarder.sendData(j)(ws)
          log.info(s"Response: $j")
          Future.successful(Ok(j))
        case _ =>
          log.error("Message could not be transformed into a JSON")
          Future.successful(BadRequest("Error"))
      }
  }

  def getJsonFromByteArray(array: Array[Byte]): JsValue = {
    val bos = new ByteArrayInputStream(array)

    val protobuf = BtExport.BusinessTransactions.parseFrom(bos)
    getJsonFromProtobufObject(protobuf)
  }

  def getJsonFromProtobufObject(btExport: BtExport.BusinessTransactions): JsValue = {
    val btCaseClass = Protobuf.convertBusinessTransactions(btExport)
    getJsonFromCaseClasses(btCaseClass)
  }

  def getJsonFromCaseClasses(caseClass: BusinessTransactions): JsValue = {
    Json.toJson(caseClass)
  }
}