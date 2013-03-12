package code.builder.project

import java.util.zip.ZipInputStream


object Project {
  def main(args : Array[String]) {
    loadFromZip("TestProjScala.zip")
  }
  def loadFromZip(str : String) : Project = {
    val stream = Project.getClass.getResourceAsStream(str)
    val zis = new ZipInputStream(stream)
    var entry  = zis.getNextEntry()

    while(entry != null) {
      println(entry.getName())
      entry  = zis.getNextEntry()
    }

    null;
  }
}

/** Represents a project during generation
  */
class Project {
  val rootDir = Directory()


}
