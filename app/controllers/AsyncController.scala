package controllers

import play.api.mvc._
import scala.concurrent._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class AsyncController extends Controller{
  def hello = Action { request =>
    Ok("hello")
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