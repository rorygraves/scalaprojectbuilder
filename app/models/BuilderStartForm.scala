package models

case class BuilderStartForm(projectName : String,projectVersion : String,projectType : String,submitType : String) {
	def this() = this("ExampleProject","1.0-SNAPSHOT","","")
}