package ca.ellanVannin.dynatrace.shared

import scala.util.{Failure, Success, Try}

/**
  * Created by Chris on 2017-01-18.
  */
object IoUtils extends Loggable {
  def using[A <: AutoCloseable, B](res: A)(execution: (A) => B): B = {
    val result = Try {
      execution(res)
    }

    Option(res).map {
      res => Try {
        res.close()
      } match {
        case Failure(exc) => log.error(s"An error occured while trying to close resource '$res'", exc)
        case Success(_) =>log.debug(s"Closed resource '$res'")
      }
    }

    result match {
      case Success(r) => r
      case Failure(exc) =>
        log.info("An error has occured", exc)
        throw exc
    }
  }
}
