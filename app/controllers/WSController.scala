package controllers
import javax.inject.Inject
import scala.concurrent.Future

import play.api.mvc._
import play.api.libs.ws._
import json_module._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Logger

class WSController @Inject() (ws: WSClient) extends Controller {
  
  def sendJsonRequest = Action.async{
    Logger.debug("hello")
    val nameLsit : List[NameRecord] = List(
        NameRecord("Tome", 20) ,
        NameRecord("Jerry", 10)
    )
    val request: WSRequest = ws.url("http://localhost:9000/names")
    
    val toSendJson = NameRecord.listToJson(nameLsit)
    
    request.post(toSendJson).map { response =>
      Ok(response.body)
    }
  }
}

class MockTestRootClass{
  val testObj = new MockTestObjClass()
  def get = testObj.getSize
}

class MockTestObjClass{
  val size: Int = 10
  def getSize = size
}