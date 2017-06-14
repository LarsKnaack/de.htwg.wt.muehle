name := "htwg-morris"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  //TODO: Reenable Filters after implementation
  .disablePlugins(PlayFilters)
  .aggregate(lib)
  .dependsOn(lib)

lazy val lib = project

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  guice,
  openId,
  javaWs,
  //some dependencies moved to seperate projects in play 2.6
  "com.typesafe.play" % "play-json_2.11" % "2.6.0-RC2",
  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.0-RC1"
)
