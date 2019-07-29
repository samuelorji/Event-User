name := "maze"

version := "0.1"

scalaVersion := "2.12.6"

val scalaTestVersion = "3.0.8"

lazy val maze = (project in file("."))
  .aggregate(core,service,web)

lazy val core = (project in file("core")).
  settings(
    libraryDependencies ++= Seq (
      "com.typesafe.scala-logging" %% "scala-logging"        % "3.5.0",
      "com.typesafe"               % "config"                % "1.3.4",
      "ch.qos.logback"             %  "logback-classic"      % "1.2.3",
      "ch.qos.logback"             %  "logback-core"         % "1.2.1",
      "org.scalatest"              %% "scalatest"            % scalaTestVersion % Test
    )
  )

lazy val web = (project in file("web")).
  settings(
    libraryDependencies ++= Seq(
      "org.mockito" % "mockito-all" % "1.10.19" % Test,
      "org.scalatest"       %%  "scalatest"        % scalaTestVersion % Test
    )
  ).dependsOn(core,service)

lazy val service = (project in file("service")).
  settings(
    libraryDependencies ++= Seq(
      "org.mockito" % "mockito-all" % "1.10.19" % Test,
      "org.scalatest"        %% "scalatest"        % scalaTestVersion % Test
    )
  ).dependsOn(core)