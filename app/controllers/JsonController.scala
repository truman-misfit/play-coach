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
    var retStr = ""
    
    //null field
    val firstVal = (parsedJsonValue \ "key1").asOpt[String]
    firstVal match{
      case Some(jsval) => retStr += "ret is " + jsval + "for key1"
      case None => retStr += "can not get value for key1"
    }
    retStr += "\n"
    
    //not null field
    val secondVal = (parsedJsonValue \ "key").asOpt[String]
    secondVal match{
      case Some(jsval) => retStr += "ret is " + jsval + "for key"
      case None => retStr += "can not get value for key"
    }
    Ok(retStr)
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