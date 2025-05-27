ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "wortschatzkiste-backend"
  )

val DoobieVersion = "1.0.0-RC9"
val http4sVersion = "0.23.17"
val circeVersion = "0.14.13"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.6.1",
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.apache.commons" % "commons-text" % "1.10.0",
  "org.jsoup" % "jsoup" % "1.16.1",
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
  "org.slf4j" % "slf4j-simple" % "2.0.13"

)

Compile / mainClass := Some("Main")

scalacOptions += "-Wnonunit-statement"

Compile / run / fork := true
