name := "maze"

version := "0.1"

scalaVersion := "2.12.6"

val akkaVersion      = "2.5.19"
val akkaHttpVersion  = "10.1.7"
val scalaTestVersion = "3.0.5"

lazy val maze = (project in file("."))
  .aggregate(core,handler,web)

lazy val core = (project in file("core")).
  settings(
    libraryDependencies ++= Seq (
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "com.typesafe"           % "config"                % "1.3.4",
      "ch.qos.logback"         %  "logback-classic"      % "1.2.3",
      "ch.qos.logback"         %  "logback-core"         % "1.2.1",
      "org.scalatest"          %% "scalatest"            % scalaTestVersion % Test
    )
  )

lazy val web = (project in file("web")).
  settings(
    libraryDependencies ++= Seq(
      "org.scalatest"       %%  "scalatest"        % scalaTestVersion % Test
    )
  ).dependsOn(core,handler)

lazy val handler = (project in file("handler")).
  settings(
    libraryDependencies ++= Seq(
      "org.scalatest"        %% "scalatest"        % scalaTestVersion % Test
    )
  ).dependsOn(core)