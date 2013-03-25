package controllers

import _root_.models.{ BuilderStartForm }
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import fly.play.sessionCache.SessionCache
import code.builder.{ BuildValidationError, ZipBuilder, BuildConfig }
import play.api.libs.iteratee.Enumerator
import java.io.ByteArrayInputStream

// TODO IDEA Plugin for sbt projects
// TODO cleanup akka imports
// TODO Read/filter/export zip file
// TODO Validation checks on build page
// TODO sort out margins on display
/**
 * ******************************************************************
 * Main application entry point
 */
object Application extends Controller {

  // builder start page form definition
  val builderStartForm = Form(
    mapping(
      "projectName" -> text,
      "projectVersion" -> text,
      "projectType" -> text,
      "includeHerokuConfig" -> boolean,
      "enableRemoting" -> boolean,
      "submitType" -> nonEmptyText)(BuilderStartForm.apply)(BuilderStartForm.unapply))

  /**
   * ******************************************************************************************
   * Display action for builder start configuration page
   */
  def builder_start = SessionCache { sessionCache =>
    Action {
      val buildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
      val startForm = builderStartForm.fill(buildConfig.startForm)
      Ok(views.html.builder_start(startForm))
    }
  }

  /**
   * ******************************************************************************************
   * Handling of post clicks from start form
   */
  def builderStartPost = SessionCache { sessionCache =>
    Action { implicit request =>
      builderStartForm.bindFromRequest.fold(
        errors => BadRequest("Errors : " + errors),
        formValue => {
          val currentBuildConfig = sessionCache.getOrElse("buildConfig")(new BuildConfig())
          val newBuildConfig = currentBuildConfig.updateStartForm(formValue)
          sessionCache.set("buildConfig", newBuildConfig, 0)
          if (formValue.submitType == "Build") {
        	val zb = new ZipBuilder()
        	
            val bytes = zb.build();
            val file = new java.io.File("test.zip")
            val fileContent: Enumerator[Array[Byte]] = Enumerator.fromStream(new ByteArrayInputStream(bytes))

            SimpleResult(
              header = ResponseHeader(200, Map(CONTENT_LENGTH -> bytes.length.toString)),
              body = fileContent)
          } else
            processDirect(formValue.submitType)
        })
    }
  }


  def processDirect(submitType: String) = {
    println("Submit type = '" + submitType + "'")
    submitType match {
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
}