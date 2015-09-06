package controllers

import play.api._
import play.api.mvc._
import scala.concurrent._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import actions._

class Application extends Controller{

  val dataForm: Form[MyDataForm] = Form{
    mapping(
      "data" -> nonEmptyText
    )(MyDataForm.apply)(MyDataForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(dataForm))
  }
  
  def composeAction = LoggingAction {
      Ok("This is a compose Action,you just write a log")
  }

  def postData = Action { implicit request =>
    println("request is :" + request.toString)
    dataForm.bindFromRequest.fold(
      formWithErrors => Ok("commit error"),
      {
        myData => Ok("receive a data:" + myData.input)
      }
    )
  }
  
  def calWithSleep(time: Int) : Int = {
    Thread sleep time
    time
  }
  
  //asyn action,sleep for time microseconds,and then return the time
  def asynAct(time: Int) = Action.async {request =>
    val futureInt = scala.concurrent.Future { calWithSleep(time) }
    futureInt.map(i => Ok(views.html.AsyncRet(i.toString)))
  }

}

object MyObj
{
  //return to the AsyncRet page
  def returnHello : String = "hello From my obj"
}

/*class for the from,I don't know other way to post a request*/

case class MyDataForm(input: String)
