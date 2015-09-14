import scala.io.Source
import java.io.PrintWriter

import play.api._
import data_model._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    
    Logger.info("Application is started!!!")
    try{
        //What I wanna tell you is that ,Source.fromFile returns a BufferedSource
        //BufferedSource.toString returns "empty iterator" or "non-empty iterator",
        //not the file's data...
        //Strange hah?
        
        val source = Source.fromFile("json.txt", "UTF-8").mkString
        Logger.info("source is :"+source)
        NameRecord.load(source)
        Logger.info("load end")
    }
    catch{
        case e:java.io.FileNotFoundException => {
            Logger.info("No Json data")
        }
    }
  }
  
  override def onStop(app: Application){
    Logger.info("Application is stopped!!")
    val out = new PrintWriter("json.txt")
    out.println(NameRecord.toJson.toString)
    out.close()
  }

}