package controllers

import play.api._
import play.api.mvc._
import models.SBProject

object Application extends Controller {

  import play.api.data._
  import play.api.data.Forms._

  val builderForm = Form(
      mapping(
    		  "projectName" -> text,
    		  "playProject" -> boolean
    		  )(SBProject.apply)(SBProject.unapply)
	  )
  
  // The task handling the form submission and building of the project
  def builderBuild=TODO

  def builder = Action {
    Ok(views.html.builder(builderForm))
  }
  
  def index = Action {
    Ok(views.html.index())
  }
  def oldindex = Action {
    Ok(views.html.oldindex("The original page."))
  }
  
}