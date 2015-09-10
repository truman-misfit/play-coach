import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import play.api.Logger

import data_model._
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class JSONSpecs extends Specification {
    "Json" should {
        "Get all user data" in WithApplication{
            val req = FakeRequest(GET, "/json")
            val ret = route(req).get
            
            status(ret) must equalTo(OK)
            contentAsString(ret) must contain ("Tom")
        }
        
        "Receive and add user" in new WithApplication{
            val jsonBody = Json.toJson(NameRecord("Loli",10,"loli@lolicon.com","2345","female"))
            Logger.info("Json str:"+jsonBody.toString)
            val req = FakeRequest(POST, "/json").withJsonBody(jsonBody)
            val jsonResult = route(req).get
            
            status(jsonResult) must equalTo(OK)
            contentAsString(jsonResult) must contain ("copy")
        }
        "Receive error " in new WithApplication{
            val jsonBody = Json.toJson(NameRecord("Loli",200,"loli@lolicon.com","2345","female"))
            Logger.info("Json str:"+jsonBody.toString)
            val req = FakeRequest(POST, "/json").withJsonBody(jsonBody)
            val jsonResult = route(req).get
            
            status(jsonResult) must equalTo(OK)
            contentAsString(jsonResult) must contain ("error")
        }
    }
}