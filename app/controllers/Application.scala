package controllers

import _root_.models.{BuilderBuildForm, BuilderPlayForm, BuilderAkkaForm, BuilderStartForm}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import fly.play.sessionCache.SessionCache
import code.builder.{BuildValidationError, ZipBuilder, BuildConfig}
import play.api.libs.iteratee.Enumerator
import java.io.ByteArrayInputStream


// TODO IDEA Plugin for sbt projects
// TODO cleanup akka inports
// TODO Read/filter/export zip file
// TODO Validation checks on build page
// TODO sort out margins on display
// TODO Scala 2.10/Akka 2.1 project (do we even bother with 2.9?

/********************************************************************
 * Main application entry point
 */
object Application extends Controller {

  // builder start page form definition
  val builderStartForm = Form(
    mapping(
      "projectName" -> text,
      "projectVersion" -> text,
      "projectType" -> text,
      "submitType" -> nonEmptyText)(BuilderStartForm.apply)(BuilderStartForm.unapply))

  // builder start page form definition
  val builderAkkaForm = Form(
    mapping(
      "enableRemoting" -> boolean,
      "submitType" -> nonEmptyText)(BuilderAkkaForm.apply)(BuilderAkkaForm.unapply))

  // builder start page form definition
  val builderPlayForm = Form(
    mapping(
      "includeHerokuConfig" -> boolean,
      "submitType" -> nonEmptyText)(BuilderPlayForm.apply)(BuilderPlayForm.unapply))

  val builderBuildForm = Form(
    mapping(
      "submitType" -> nonEmptyText)(BuilderBuildForm.apply)(BuilderBuildForm.unapply))

      

  /********************************************************************************************
   * Display action for builder start configuration page
   */
  def builder_start =  SessionCache { sessionCache => 
    Action {
        val buildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val startForm = builderStartForm.fill(buildConfig.startForm)
    	Ok(views.html.builder_start(startForm))
    }
  }
    
      
  /********************************************************************************************
   * Handling of post clicks from start form
   */
  def builderStartPost = SessionCache { sessionCache => Action { implicit request =>
    builderStartForm.bindFromRequest.fold(
      errors => BadRequest("Errors : " + errors),
      formValue => {
        val currentBuildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val newBuildConfig = currentBuildConfig.updateStartForm(formValue)
        sessionCache.set("buildConfig", newBuildConfig, 0)
        processDirect(formValue.submitType)
      })
  } }

  
  /********************************************************************************************
   * Display action for builder play configuration page
   */
  def builder_play = SessionCache { sessionCache =>
    Action {
        val buildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val playForm = builderPlayForm.fill(buildConfig.playForm)
    	Ok(views.html.builder_play(playForm))
    }
  }


  /********************************************************************************************
   * Handling of post clicks from builder play page
   */
  def builderPlayPost = SessionCache { sessionCache => Action { implicit request =>
    builderPlayForm.bindFromRequest.fold(
      errors => BadRequest("Errors : " + errors),
      formValue => {
        val currentBuildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val newBuildConfig = currentBuildConfig.updatePlayForm(formValue)
        sessionCache.set("buildConfig", newBuildConfig, 0)
        processDirect(formValue.submitType)
      })
  }}

  
  /********************************************************************************************
   * Display action for builder play configuration page
   */
  def builder_akka = SessionCache { sessionCache => 
    Action {
        val buildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val akkaForm = builderAkkaForm.fill(buildConfig.akkaForm)
    	Ok(views.html.builder_akka(akkaForm))
    }
  }
  
  /********************************************************************************************
   * Handling of post clicks from builder akka page
   */
  def builderAkkaPost = SessionCache { sessionCache =>
    Action { implicit request =>
      builderAkkaForm.bindFromRequest.fold(
        errors => BadRequest("Errors : " + errors),
        formValue => {
        val currentBuildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
        val newBuildConfig = currentBuildConfig.updateAkkaForm(formValue)
        sessionCache.set("buildConfig", newBuildConfig, 0)
        processDirect(formValue.submitType)
        })
    }
  }

  /********************************************************************************************
   * Display action for builder build page
   */
  def builder_build = SessionCache { sessionCache =>
    Action {
      val currentBuildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
      val errors: List[BuildValidationError] = currentBuildConfig.errors
    	Ok(views.html.builder_build(builderBuildForm,errors))
    }
  }

  /********************************************************************************************
   * Handling of post clicks from builder akka page
   */
  def builderBuildPost = SessionCache { sessionCache =>
    Action { implicit request =>
    builderBuildForm.bindFromRequest.fold(
      errors => BadRequest("BuildPOSTErrors : " + errors),
      value => {
    	  sessionCache.set("buildForm",value)
    	  if(value.submitType == "Build") {
          val bytes = ZipBuilder.build();
          val file = new java.io.File("test.zip")
          val fileContent: Enumerator[Array[Byte]] = Enumerator.fromStream(new ByteArrayInputStream(bytes))

          SimpleResult(
            header = ResponseHeader(200, Map(CONTENT_LENGTH -> bytes.length.toString)),
            body = fileContent
          )
    	  } else
    		  processDirect(value.submitType)
      })
    }
  }

  def processDirect(submitType: String) = {
    println("Submit type = '" + submitType + "'")
    submitType match {
      case "Akka configuration" =>
        Redirect(routes.Application.builder_akka())
      case "Play configuration" =>
        Redirect(routes.Application.builder_play())
      case "Build Project" =>
        Redirect(routes.Application.builder_build())
      case "Start" =>
        Redirect(routes.Application.builder_start())
      case _ =>
        BadRequest("Unknown redirect")
    }
  }

  /**
   * ******************************************************************************************
   * Display action for index page
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   * ******************************************************************************************
   * Index in dev mode has link to default index page to allow quick access to online docs
   */
  def oldindex = Action {
    Ok(views.html.oldindex("The original page."))
  }

}