package controllers

import play.api._
import play.api.mvc._
import scala.concurrent._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  object LoggingAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      Logger.info("Calling action")
      Logger.info("maybe you want to do more things here")
      //do more thing
      block(request)
    }
  }
  
  def composeAction = LoggingAction {
      Ok("This is a compose Action,you just write a log")
  }
}
