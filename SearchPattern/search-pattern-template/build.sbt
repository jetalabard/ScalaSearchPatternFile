val Slf4jVersion = "1.7.7"

resolvers += Classpaths.typesafeReleases


lazy val commonSettings = Seq(
  organization := "com.m2i.search",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.11"
)

lazy val root = Project("search", sbt.file("."))
  .aggregate(searchPattern)
  .settings(
    name := "m2i-multi-module",
    commonSettings
  )


lazy val searchPattern = Project("search-pattern", sbt.file("search-pattern"))
  .settings(
    commonSettings,
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.0",
    libraryDependencies += "org.slf4j" % "slf4j-api" % Slf4jVersion,
    libraryDependencies += "org.slf4j" % "slf4j-simple" % Slf4jVersion
  )



