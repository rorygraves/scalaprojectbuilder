import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "scalaprojectbuilder"
  val appVersion = "1.0-SNAPSHOT"

  // from http://stackoverflow.com/questions/10436815/how-to-use-twitter-bootstrap-2-with-play-framework-2-0
  // Only compile the bootstrap bootstrap.less file and any other *.less file in the stylesheets directory 
  def customLessEntryPoints(base: File): PathFinder = (
    (base / "app" / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++
    (base / "app" / "assets" / "stylesheets" / "bootstrap" * "responsive.less")  +++ 
    (base / "app" / "assets" / "stylesheets" * "*.less")
    )

  val appDependencies = Seq()
    //"nl.rhinofly" %% "session-cache" % "1.0.0")

  val main = play.Project(appName, appVersion, appDependencies).settings(
//    resolvers += "Rhinofly Internal Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local",

    lessEntryPoints <<= baseDirectory(customLessEntryPoints))

}
