package ca.ellanVannin.dynatrace

import ca.ellanVannin.dynatrace.shared.Configuration._
import play.api.libs.json.JsValue
import play.api.libs.ws.WSAuthScheme.BASIC
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

/**
  * Created by Chris on 2017-02-15.
  */
object DataForwarder {
  val FORWARD_URL_KEY: String = KEY_PREFIX + ".forwardURL"
  val AUTH_ACTIVATE_KEY: String = KEY_PREFIX + ".auth.activate"
  val AUTH_USERNAME_KEY: String = KEY_PREFIX + ".auth.username"
  val AUTH_PASSWORD_KEY: String = KEY_PREFIX + ".auth.password"

  def getForwardUrl: String = conf.getString(FORWARD_URL_KEY)

  def getAuthActivate: Boolean = conf.getBoolean(AUTH_ACTIVATE_KEY)

  def getAuthUser: String = conf.getString(AUTH_USERNAME_KEY)

  def getAuthPassword: String = conf.getString(AUTH_PASSWORD_KEY)

}

class DataForwarder(url: String = DataForwarder.getForwardUrl,
                    authActive: Boolean = DataForwarder.getAuthActivate,
                    authUser: String = DataForwarder.getAuthUser,
                    authPassword: String = DataForwarder.getAuthPassword) {
  def sendData(json: JsValue)(implicit ws: WSClient): Future[WSResponse] = {
    val ws1 = if (authActive)
      ws.url(url).withAuth(authUser, authPassword, BASIC)
    else
      ws.url(url)

    ws1.post(json)
  }
}
