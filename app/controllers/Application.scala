package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.BuilderStartForm
import models.BuilderPlayForm
import models.BuilderAkkaForm
import play.api.data._
import play.api.data.Forms._
import models.BuilderBuildForm
import play.api.Play.current
import fly.play.sessionCache.SessionCache
import code.builder.ZipBuilder

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
   * Handling of post clicks from start form
   */
  def builderStartPost = SessionCache { sessionCache => Action { implicit request =>
    builderStartForm.bindFromRequest.fold(
      errors => BadRequest("Errors : " + errors),
      formValue => {
        sessionCache.set("startForm", formValue, 0);
        processDirect(formValue.submitType)
      })
  } }

  /********************************************************************************************
   * Handling of post clicks from builder play page
   */
  def builderPlayPost = SessionCache { sessionCache => Action { implicit request =>
    builderPlayForm.bindFromRequest.fold(
      errors => BadRequest("Errors : " + errors),
      formValue => {
        sessionCache.set("playForm", formValue, 0);
        processDirect(formValue.submitType)
      })
  }}

  /********************************************************************************************
   * Handling of post clicks from builder akka page
   */
  def builderAkkaPost = SessionCache { sessionCache =>
    Action { implicit request =>
      builderAkkaForm.bindFromRequest.fold(
        errors => BadRequest("Errors : " + errors),
        value => {
          sessionCache.set("akkaForm",value)
          processDirect(value.submitType)
        })
    }
  }

  /********************************************************************************************
   * Display action for builder build page
   */
  def builder_build = SessionCache { sessionCache =>
    Action {
        val formValue = sessionCache.get("buildForm") match {
        	case Some(bbf : BuilderBuildForm ) =>
        		builderBuildForm.fill(bbf)
        	case _ =>
        	  builderBuildForm
    	} 
        
    	Ok(views.html.builder_build(formValue))
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
    	    Ok(ZipBuilder.build())
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

  /********************************************************************************************
   * Display action for builder start configuration page
   */
  def builder_start =  SessionCache { sessionCache => 
    Action {
        val formValue = sessionCache.get("startForm") match {
        	case Some(bsf : BuilderStartForm ) =>
        		builderStartForm.fill(bsf)
        	case _ =>
        	  builderStartForm
    	} 
    	Ok(views.html.builder_start(formValue))
    }
  }
    

  /********************************************************************************************
   * Display action for builder play configuration page
   */
  def builder_play = SessionCache { sessionCache =>
    Action {
        val formValue = sessionCache.get("playForm") match {
        	case Some(bpf : BuilderPlayForm ) =>
        		builderPlayForm.fill(bpf)
        	case _ =>
        	  builderPlayForm
    	} 
    	Ok(views.html.builder_play(formValue))
    }
  }

  /********************************************************************************************
   * Display action for builder play configuration page
   */
  def builder_akka = SessionCache { sessionCache => 
    Action {
        val formValue = sessionCache.get("akkaForm") match {
        	case Some(baf : BuilderAkkaForm ) =>
        		builderAkkaForm.fill(baf)
        	case _ =>
        	  builderAkkaForm
    	} 
    	Ok(views.html.builder_akka(formValue))
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