package ca.ellanVannin.dynatrace.data

import play.api.libs.json._

/**
  * Created by Chris on 2017-02-06.
  */


sealed abstract class Type()

case object PUREPATH extends Type

case object USER_ACTION extends Type

case object VISIT extends Type

case class BusinessTransaction(name: String,
                               `type`: Option[Type],
                               application: Option[String],
                               measureNames: Seq[String],
                               dimensionNames: Seq[String],
                               occurrences: Seq[BtOccurence],
                               systemProfile: Option[String]
                              )

object BusinessTransaction {
  val NAME_KEY = 'name
  val TYPE_KEY = 'type
  val APPLICATION_KEY = 'application
  val MEASURE_NAMES_KEY = 'measureNames
  val DIMENSION_NAMES_KEY = 'dimensionNames
  val OCCURENCES_KEY = 'occurrences
  val SYSTEM_PROFILE_KEY = 'systemProfile

  implicit val businessTransactionWrites = new Writes[BusinessTransaction] {
    def writes(o: BusinessTransaction) = Json.obj(
      NAME_KEY.name -> o.name,
      TYPE_KEY.name -> (convertType (o.`type`)),
      APPLICATION_KEY.name -> o.application,
      MEASURE_NAMES_KEY.name -> o.measureNames,
      DIMENSION_NAMES_KEY.name -> o.dimensionNames,
      OCCURENCES_KEY.name -> o.occurrences,
      SYSTEM_PROFILE_KEY.name -> o.systemProfile
    )
  }

  def convertType(t: Option[Type]): Option[JsNumber] = t map {
    case PUREPATH => JsNumber(0)
    case USER_ACTION => JsNumber(1)
    case VISIT => JsNumber(2)
  }
}