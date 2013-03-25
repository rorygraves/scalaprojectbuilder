package code.builder.fileset

import java.util.zip.ZipInputStream
import java.io.FileInputStream
import java.io.InputStream

object FileSet {
  def main(args: Array[String]) {
    loadFromZip("/tools/workspace/TestProjScala.zip")
  }

  def loadFromZip(str: String): FileSet = {
    val stream = new FileInputStream(str)
    loadFromZip(stream);

  }

  def loadFromZip(stream : InputStream): FileSet = {
    val zis = new ZipInputStream(stream)
    var entry = zis.getNextEntry()

    val fs = new FileSet()

    while (entry != null) {
      val entryName = entry.getName()
      val lastSlashIdx = entryName.lastIndexOf('/')
      val entryDirs = entryName.substring(0,lastSlashIdx).split("/").toList;

      val targetDir = fs.getDir(entryDirs)

      if (!entry.isDirectory()) {
        // its a file
        val fname = entryName.substring(lastSlashIdx + 1)
        println("FILE " + entry.getName()  + "  " + fname)
      }

      entry = zis.getNextEntry()
    }

    fs
  }
}

/**
 * Represents a project during generation
 */
class FileSet {
  val rootDir = Directory()

  def getDir(dirNames: List[String]): Directory = {
    rootDir.getDir(dirNames)
  }

}
