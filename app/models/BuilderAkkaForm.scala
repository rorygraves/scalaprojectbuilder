package models

case class BuilderAkkaForm(enableRemoting : Boolean,submitType : String) {
	def this() = this(false,"")
}