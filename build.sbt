name := "ScalaWebServer"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
libraryDependencies += "org.markushauck" %% "mockitoscala" % "0.3.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.22"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.22" % Test