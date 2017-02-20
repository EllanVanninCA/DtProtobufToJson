package ca.ellanVannin.dynatrace.data

import play.api.libs.json.{Json, Writes}
/**
  * Created by Chris on 2017-02-14.
  */
case class BusinessTransactions(businessTransactions: Seq[BusinessTransaction],
                                lostTransactions: Option[Int],
                                serverName: Option[String])

object BusinessTransactions {
  val BUSINESS_TRANSACTIONS_KEY = 'businessTransactions
  val LOST_TRANSACTIONS_KEY = 'lostTransactions
  val SERVER_NAME_KEY = 'serverName

  implicit val businessTransactionsWrites = new Writes[BusinessTransactions] {
    def writes(o: BusinessTransactions) = {
      Json.obj(
        BUSINESS_TRANSACTIONS_KEY.name -> o.businessTransactions,
        LOST_TRANSACTIONS_KEY.name -> o.lostTransactions,
        SERVER_NAME_KEY.name -> o.serverName
      )
    }
  }
}


