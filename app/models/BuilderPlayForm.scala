package models

case class BuilderPlayForm(includeHerokuConfig : Boolean,submitType : String) {
	def this() = this(true,"")
}