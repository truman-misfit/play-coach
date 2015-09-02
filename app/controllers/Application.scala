package controllers

import play.api._
import play.api.mvc._
import scala.concurrent._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class Application extends Controller{

  val dataForm: Form[MyDataForm] = Form{
    mapping(
      "data" -> nonEmptyText
    )(MyDataForm.apply)(MyDataForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(dataForm))
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

  def postData = Action { implicit request =>
    println("request is :" + request.toString)
    dataForm.bindFromRequest.fold(
      formWithErrors => Ok("commit error"),
      {
        mydata => Ok("receive a data:" + mydata.input)
      }
    )
  }
}

/*class for the from,I don't know other way to post a request*/

case class MyDataForm(input: String)
