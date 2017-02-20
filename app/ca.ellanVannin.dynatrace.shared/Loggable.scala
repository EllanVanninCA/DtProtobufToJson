package ca.ellanVannin.dynatrace.shared

import play.api.Logger

/**
  * Created by Chris on 2017-01-16.
  */
trait Loggable {
  lazy val log = Logger(this.getClass)
}
