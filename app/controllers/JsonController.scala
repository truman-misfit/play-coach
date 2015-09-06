package controllers

import play.api.mvc._
import play.api.libs.json._

import play.api.libs.json._
import play.api.libs.functional.syntax._
case class NameRecord(name: String, age: Int)

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
  def jsString :String = Json.toJson(nameList).toString
  
  def addName(name: String,age: Int) = Action{request =>
    val record = NameRecord(name,age)
    nameList = nameList ::: List(record) 
//    Ok(Json.toJson(nameList))
    Ok("jsString is :" + jsString)
  }
  
  def parseStr = Action{request =>
    val json = Json.parse(jsString)
    val jsonList = json.validate[List[NameRecord]]
    
    jsonList match {
      case success: JsSuccess[List[NameRecord]] => 
      {
        val myList = success.get
        myList.foreach(jsrecord => {
          println("name :" + jsrecord.name + " age:" + jsrecord.age)
        })

      }
      case e: JsError => println("Errors")
    }
    
    Ok("ok")
      
  }
  
  implicit val nameRecordWrite: Writes[NameRecord] = (
      (JsPath \ "name").write[String] and
      (JsPath \ "age").write[Int]
  )(unlift(NameRecord.unapply))
  
  implicit val nameRecordRead: Reads[NameRecord] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "age").read[Int]
  )(NameRecord.apply _)

}