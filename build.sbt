import Dependencies._
import sbt.Def

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val AkkaVersion = "2.5.31"
val AkkaHttpVersion = "10.1.11"

val value: Def.Setting[String] = name := "snstest"
val value1: Def.Setting[Seq[ModuleID]] = libraryDependencies += scalaTest % Test
lazy val root = (project in file("."))
  .settings(
    value,

    value1,
      libraryDependencies ++= Seq(
      "com.lightbend.akka" %% "akka-stream-alpakka-sns" % "2.0.0",
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
