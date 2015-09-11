package actions

import play.api._
import play.api.mvc._
import play.api.Play.current
import scala.concurrent._

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Request is : " + request.toString)
    block(request)
  }
}
