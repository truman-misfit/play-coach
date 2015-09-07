package module

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import play.api.libs.json.Reads._


case class NameRecord(name: String, age: Int)
{
    
}

object NameRecord{
  implicit val nameRecordWrite: Writes[NameRecord] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "age").write[Int]
  )(unlift(NameRecord.unapply))
  
  implicit val nameRecordRead: Reads[NameRecord] = (
    (JsPath \ "name").read[String](minLength[String](1) keepAnd maxLength[String](50)) and
    (JsPath \ "age").read[Int](min(0) keepAnd max(121))
  )(NameRecord.apply _)
  
  def listToString(nameList:List[NameRecord]) = listToJson(nameList).toString
    
  def parseJsonValueIntoNameRecord(jsString: String) = {
    var retStr = ""
    val myList = parseJsonValueIntoNameRecordList(jsString)
    if(myList.length == 0)
    {
      retStr = "List is empty,may get error"
    }
    else
    {
      myList.foreach(jsrecord => {
        retStr += ("name :" + jsrecord.name + " age:" + jsrecord.age + "\n")
      })
    }
   
    retStr
  }
  
  def listToJson(nameList:List[NameRecord]) = Json.toJson(nameList)
  
  def parseJsonValueIntoNameRecordList(jsString: String) : List[NameRecord] =
  {
    val json = Json.parse(jsString)
    val jsonList = json.validate[List[NameRecord]]
    var myList : List[NameRecord] = Nil
    jsonList match {
      case success: JsSuccess[List[NameRecord]] => 
      {
        myList = success.get
      }
      case e: JsError => 
      {
        Logger.error("Errors")
      }
    }
    myList
  }
}