name := "DtProtobufToJson"

version := "1.0"

lazy val `dtprotobuftojson` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.1.0"

libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  