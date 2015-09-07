import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import play.api.libs.json.Reads._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class JsonSpecs extends Specification {
  "Json " should {
    "return OK" in new WithApplication{
      val json : JsValue = Json.parse("""
      [
        {"name":"john","age":20}
      ]
        
      """
      )
      val request = FakeRequest(POST, "/names").withJsonBody(json)
      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("john")
    }
    
    "return error because age is too old" in  new WithApplication{
      val json : JsValue = Json.parse("""
      [
        {"name":"john","age":200}
      ]
        
      """
      )
      val request = FakeRequest(POST, "/names").withJsonBody(json)
      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("List is empty,may get error")
    }
    
    "return error because age is too little" in  new WithApplication{
      val json : JsValue = Json.parse("""
      [
        {"name":"john","age":-1}
      ]
        
      """
      )
      val request = FakeRequest(POST, "/names").withJsonBody(json)
      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("List is empty,may get error")
    }
    
    "return error because name is empty" in  new WithApplication{
      val json : JsValue = Json.parse("""
      [
        {"name":"","age":20}
      ]
        
      """
      )
      val request = FakeRequest(POST, "/names").withJsonBody(json)
      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("List is empty,may get error")
    }
    
    "return error because name is too long" in  new WithApplication{
      val json : JsValue = Json.parse("""
      [
        {"name":"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz","age":20}
      ]
        
      """
      )
      val request = FakeRequest(POST, "/names").withJsonBody(json)
      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) must contain ("List is empty,may get error")
    }
  }
}