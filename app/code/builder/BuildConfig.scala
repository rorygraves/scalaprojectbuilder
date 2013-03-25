package code.builder

import models.BuilderStartForm
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import java.io.ByteArrayOutputStream
import java.util.zip.{ZipInputStream, ZipOutputStream}

/**
 * A complete build configuration, based on the various form submissions
 */
case class BuildConfig(startForm : BuilderStartForm) {
  
  def this()  = this(new BuilderStartForm()) 
  
  def updateStartForm(newStartForm : BuilderStartForm) = new BuildConfig(newStartForm)
  

  def isEmpty(s : String) = s == null || s.trim.equals("")
  def errors : List[BuildValidationError] = {    
    val tmpList  = new ListBuffer[BuildValidationError]()

    if(isEmpty(startForm.projectName))
      tmpList += new BuildValidationError("Project name must be set","start")

    if(isEmpty(startForm.projectVersion))
      tmpList += new BuildValidationError("Project version must be set","start")

    if(isEmpty(startForm.projectType))
      tmpList += new BuildValidationError("Project type must be set","start")

    tmpList.toList
  }

  def buildProject : Option[Array[Byte]] = {
    if (errors.size != 0)
      None
    else Some(buildArchive)
  }

  private def buildArchive : Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val zos = new ZipOutputStream(baos)


    // close and return the archive
    zos.close()
    baos.toByteArray
  }
}