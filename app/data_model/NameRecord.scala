package data_model

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

import play.api.Logger

case class NameRecord(name:String, age:Int, mail:String, tel:String, gender:String)

object NameRecord
{
    var nameRecordList:List[NameRecord] = List[NameRecord]{
        new NameRecord("Tom",10,"Tom@Catoon.com","119","male")
    }
    
    def addName(name:String, age:Int, mail:String, tel:String, gender:String) = {
        nameRecordList = nameRecordList :+ NameRecord(name,age,mail,tel,gender)
    }
    
    def getUser(name:String) : String = {
        nameRecordList foreach({ record =>
            if(name == record.name)
                return "Find user :" + record.name + ",age:" + record.age
        })
        throw new IllegalArgumentException
    }
    
    def getAllUser:String = {
        var ret = ""
        nameRecordList foreach(record =>{
            ret += "Name:" + record.name + " ,age:" + record.age + "\n"
        })
        //Thread sleep 5000
        ret
    }
    
    def toJson = Json.toJson(nameRecordList)
    def toNameRecordList(str:String) = {
        val parseResult = Json.parse(str).validate[List[NameRecord]]
        parseResult match{
            case s: JsSuccess[List[NameRecord]] => {
                s.get
            }
            case e: JsError => {
                throw new IllegalArgumentException
                List.empty[NameRecord]
            }
        }
        
    }
    
    def load(str:String){
        nameRecordList = toNameRecordList(str)
    }
    
    implicit val locationReads: Reads[NameRecord] = (
        (JsPath \ "name").read[String] and
        (JsPath \ "age").read[Int] and
        (JsPath \ "mail").read[String] and
        (JsPath \ "tel").read[String] and
        (JsPath \ "gender").read[String]
    )(NameRecord.apply _).filter(validateNameRecord(_))
        
    implicit val placeWrites: Writes[NameRecord] = (
        (JsPath \ "name").write[String] and
        (JsPath \ "age").write[Int] and
        (JsPath \ "mail").write[String] and
        (JsPath \ "tel").write[String] and
        (JsPath \ "gender").write[String]
    )(unlift(NameRecord.unapply))
    
    //calidate function for form
    def validateNameRecord(record:NameRecord): Boolean = {
        Logger.info("In validate func")
        if(record.age < 0 || record.age > 140) return false
        
        {
            var atNum = 0
            for(i <- 0 until record.mail.length if record.mail.charAt(i) == '@'){
                atNum = atNum+1
            }
            if(atNum != 1) {
                Logger.info("mail adress is not validate")
                return false
            }
        }
            
        {
            for(i <- 0 until record.tel.length)
            {
                if(false == record.tel.charAt(i).isDigit)return false
            }
        }
        
        
        record.gender.toLowerCase match{
            case "female" => true
            case "male" => true
            case _ => false
        }
    }
}