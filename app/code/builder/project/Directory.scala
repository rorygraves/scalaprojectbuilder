package code.builder.project

/**
 * Represents a directory in a project creation
 * TODO Find out how to primary make constructor private
 */

object Directory {
  def apply() : Directory = new RootDirectory
  def apply(name : String,parent : Directory) : Directory = new NormalDirectory(name,parent)
}

abstract class Directory(name : String) extends ProjectItem(name) {
  private var children : Map[String,ProjectItem] = Map()

  def addChild(item : ProjectItem) {
    children += (item.name -> item)
  }

}

private class RootDirectory extends Directory("/") {

}


private class NormalDirectory(name : String,val parent : Directory) extends Directory(name) {
  parent.addChild(this)

}
