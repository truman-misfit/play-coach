package controllers

import play.api.mvc._
import scala.concurrent._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class AsyncController extends Controller{
    
  val longTimeCalculator = new LongTimeCal()
  
  def getReturn(time:Int) = longTimeCalculator.calWithSleep(time)
  
  //asyn action,sleep for time microseconds,and then return the time
  def asynAct(time: Int) = Action.async {request =>
    val futureInt = scala.concurrent.Future { getReturn(time) }
    futureInt.map(i => Ok(views.html.AsyncRet(i.toString)))
  }
}

class LongTimeCal {
  def calWithSleep(time: Int) : Int = {
    Thread sleep time
    time
  }
}