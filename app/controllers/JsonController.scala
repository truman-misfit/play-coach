package controllers

import play.api.mvc._
import play.api.libs.json._

class JsonController extends Controller{
  val parsedJsonValue = Json.parse("""
  {
      "key":"This is a json value",
      "array":{
        "keyInArray":"hello",
        "key2InArray":"helloKey2"
      }
  }
  """
  )
  def getJsonValue = Action{
      Ok("ret is " + (parsedJsonValue \ "key").toString + (parsedJsonValue \\ "keyInArray"))
  }
}