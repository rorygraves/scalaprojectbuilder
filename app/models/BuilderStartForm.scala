package models

case class BuilderStartForm(projectName : String,projectVersion : String,projectType : String,
    includeHerokuConfig : Boolean,enableRemoting : Boolean,submitType : String) {
	def this() = this("ExampleProject","1.0-SNAPSHOT","",false,false,"")
}