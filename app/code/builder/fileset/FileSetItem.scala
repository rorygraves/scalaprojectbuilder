package code.builder.fileset

/** An item that belongs in a FileSet
 *  
 *  N.b. A sealed class to improve matches
 */
sealed abstract class FileSetItem(val name : String) {

}


/** 
 *  A File that is part of a FileSet
 */
class File(name : String,parent : Directory) extends FileSetItem(name) {

}

/** Class representing a text file within a project */
class TextFile(parent : Directory,name: String) extends File(name,parent) {


}

/**
 * Represents a directory in a project creation
 */ 

object Directory {
  def apply() : Directory = new RootDirectory
  def apply(name : String,parent : Directory) : Directory = new NormalDirectory(name,parent)
}

abstract class Directory protected (name : String) extends FileSetItem(name) {
  private var children : Map[String,FileSetItem] = Map()

  def addChild(item : FileSetItem) {
    children += (item.name -> item)
  }
  
  def getDir(dirNames : List[String]) : Directory = {
        dirNames match {
      case Nil => this
      case dirName :: xs =>
        children.get(dirName) match {
          case Some(childDir : Directory) => childDir.getDir(xs);
          case Some(file : File) => throw new IllegalStateException("File found when expecting directory")
          case None => 
            val dir = new NormalDirectory(dirName,this);
            addChild(dir)
            dir
        }
    }
  }

  

}

private class RootDirectory extends Directory("/") {

}


private class NormalDirectory(name : String,val parent : Directory) extends Directory(name) {
  parent.addChild(this)

}
