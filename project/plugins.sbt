// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.0-RC2")

// Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")

// Heroku Plugin
addSbtPlugin("com.heroku" % "sbt-heroku" % "1.0.1")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

// Play Ebean support, to enable, uncomment this line, and enable in your build.sbt using
// enablePlugins(PlayEbean).
// addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "3.0.2")
