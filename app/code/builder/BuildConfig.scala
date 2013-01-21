package code.builder

import models.BuilderPlayForm
import models.BuilderStartForm
import models.BuilderAkkaForm
import scala.collection.mutable
import scala.collection.immutable.List
import scala.collection.mutable.LinkedList
import scala.collection.mutable.ListBuffer
/** 
 * A complete build configuration, based on the various form submissions
 */
case class BuildConfig(val startForm : BuilderStartForm,val playForm : BuilderPlayForm,val akkaForm : BuilderAkkaForm) {
  
  def this()  = this(new BuilderStartForm(),new BuilderPlayForm(),new BuilderAkkaForm()) 
  
  def updateStartForm(newStartForm : BuilderStartForm) = new BuildConfig(newStartForm,playForm,akkaForm)
  def updatePlayForm(newPlayForm : BuilderPlayForm) = new BuildConfig(startForm,newPlayForm,akkaForm)
  def updateAkkaForm(newAkkaForm : BuilderAkkaForm) = new BuildConfig(startForm,playForm,newAkkaForm)
  

  def errors : List[BuildValidationError] = {    
    val tmpList  = new ListBuffer[BuildValidationError]()

    if(startForm.projectName == null || startForm.projectName == "")
      tmpList += new BuildValidationError("Project name must be set","start")

    if(startForm.projectVersion == null || startForm.projectVersion == "")
      tmpList += new BuildValidationError("Project version must be set","start")

    tmpList.toList
  } 
}