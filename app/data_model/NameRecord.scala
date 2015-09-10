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
        
    )(NameRecord.apply _)
        
    implicit val placeWrites: Writes[NameRecord] = (
        
    )(unlift(NameRecord.unapply))
    
    //calidate function for form
    def validateNameRecord(record:NameRecord): Boolean = {
        
    }
}