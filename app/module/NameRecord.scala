package module

import play.api.libs.json._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger

case class NameRecord(name: String, age: Int)
{
    
}

object NameRecord{
  implicit val nameRecordWrite: Writes[NameRecord] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "age").write[Int]
  )(unlift(NameRecord.unapply))
  
  implicit val nameRecordRead: Reads[NameRecord] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "age").read[Int]
  )(NameRecord.apply _)
  
  def listToString(nameList:List[NameRecord]) = Json.toJson(nameList).toString
    
  def parseJsonValueIntoNameRecord(jsString: String) = {
    val json = Json.parse(jsString)
    val jsonList = json.validate[List[NameRecord]]
    var retStr = ""
    jsonList match {
      case success: JsSuccess[List[NameRecord]] => 
      {
        val myList = success.get
        myList.foreach(jsrecord => {
          retStr += ("name :" + jsrecord.name + " age:" + jsrecord.age + "\n")
        })
      }
        case e: JsError => Logger("Errors")
    }
    retStr
  }
}