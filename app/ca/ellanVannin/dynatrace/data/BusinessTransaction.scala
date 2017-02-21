package ca.ellanVannin.dynatrace.data

import play.api.libs.json._

/**
  * Created by Chris on 2017-02-06.
  */


sealed abstract class Type(value: Int) {
  def getValue = value
}

case object PUREPATH extends Type(0)

case object USER_ACTION extends Type(1)

case object VISIT extends Type(2)

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

  implicit val typeWrites = new Writes[Type] {
    def writes(t: Type) = JsNumber(t.getValue)
  }

  implicit val businessTransactionWrites = new Writes[BusinessTransaction] {
    def writes(o: BusinessTransaction) = Json.obj(
      NAME_KEY.name -> o.name,
      TYPE_KEY.name -> o.`type`,
      APPLICATION_KEY.name -> o.application,
      MEASURE_NAMES_KEY.name -> o.measureNames,
      DIMENSION_NAMES_KEY.name -> o.dimensionNames,
      OCCURENCES_KEY.name -> o.occurrences,
      SYSTEM_PROFILE_KEY.name -> o.systemProfile
    )
  }
}