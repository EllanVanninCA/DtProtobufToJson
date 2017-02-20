package ca.ellanVannin.dynatrace.shared

import java.io.File
import java.text.SimpleDateFormat

import com.typesafe.config.ConfigFactory

/**
  * Created by Chris on 2017-02-04.
  */
object Configuration {
  val conf = ConfigFactory.load()
  val KEY_PREFIX = "ca.ellanVannin.dynatrace"
}
