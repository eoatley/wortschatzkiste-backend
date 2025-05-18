ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "wortschatzkiste-backend"
  )

val DoobieVersion = "1.0.0-RC9"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.6.1",
  "org.http4s" %% "http4s-blaze-server" % "0.23.17",
  "org.http4s" %% "http4s-dsl" % "0.23.17",
  "org.http4s" %% "http4s-circe" % "0.23.17",
  "io.circe" %% "circe-generic" % "0.14.13",
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
  "org.slf4j" % "slf4j-simple" % "2.0.13"

)

Compile / mainClass := Some("Main")

scalacOptions += "-Wnonunit-statement"

Compile / run / fork := true
