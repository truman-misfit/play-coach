package data_model

case class NameRecord(name:String, age:Int)

object NameRecord
{
    var nameRecordList:List[NameRecord] = List[NameRecord]{
        new NameRecord("Tom",10)
    }
    
    def addName(name:String, age:Int) = {
        nameRecordList = nameRecordList :+ NameRecord(name,age)
    }
    
    def getUser(name:String) : String = {
        nameRecordList foreach({ record =>
            if(name == record.name)
                return "Find user :" + record.name + ",age:" + record.age
        })
        throw new IllegalArgumentException
    }
}