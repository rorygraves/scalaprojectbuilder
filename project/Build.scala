import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "scalaprojectbuilder"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
		"nl.rhinofly" %% "session-cache" % "1.0.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    		resolvers += "Rhinofly Internal Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local"
 )

}
