package controllers

import play.api.mvc._
import play.api.libs.json._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import module._

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
    val keyVal = (parsedJsonValue \ "key2").asOpt[String]
    keyVal match{
      case Some(jsval) => Ok("ret is " + jsval)
      case None => Ok("can not get key")
    }
  }
  
  var nameList :List[NameRecord] = Nil
  def jsString :String = NameRecord.listToString(nameList)
  
  def addName(name: String,age: Int) = Action{request =>
    val record = NameRecord(name,age)
    nameList = nameList ::: List(record) 
//    Ok(Json.toJson(nameList))
    Ok("jsString is :" + jsString)
  }
  
  def parseStr = Action{request =>
    
    
    Ok(NameRecord.parseJsonValueIntoNameRecord(jsString))
      
  }
  
  

}