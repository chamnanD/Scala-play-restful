package controllers

import play.api._
import play.api.mvc._
import task.TaskController
import services.task.TaskServiceComponentImpl
import repositories.task.TaskRepositoryComponentImpl

object Application extends TaskController
                   with TaskServiceComponentImpl
                   with TaskRepositoryComponentImpl {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}