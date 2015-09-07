package actions

import play.api._
import play.api.mvc._
import play.api.Play.current
import scala.concurrent._

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Calling action")
    Logger.info("maybe you want to do more things here")
    //do more thing
    block(request)
  }
}