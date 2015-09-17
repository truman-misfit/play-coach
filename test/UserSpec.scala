import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {

  "Application" should {
    "return a query result" in new WithApplication{
      val home = route(FakeRequest(GET, "/user/Tom")).get

      status(home) must equalTo(OK)
      contentAsString(home) must contain ("You query about: Tom.")
    }
//		"return a string" in new WithApplication{
//			val res = route(FakeRequest(GET,"/users")).get
//
//      status(res) must equalTo(OK)
//
//      contentAsString(res) must contain ("Tom")
//      contentAsString(res) must contain ("gender")
//      contentAsString(res) must contain ("mail")
//		}

		"Receive and add user" in new WithApplication{
			val jsonBody = Json.parse(
			"""
				{"name":"Tim","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
			"""
			)
			val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
			val jsonResult = route(req).get
			
			status(jsonResult) must equalTo(OK)
			contentAsString(jsonResult) must contain ("Add")
		}

		"return illegal argument" in new WithApplication{
			val jsonBody = Json.parse(
			"""
				{"name":"Tom","age":210,"tel":"13245","mail":"Tom@123.com","gender":"male"}
			"""
			)
			val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
			val jsonResult = route(req).get
			
			status(jsonResult) must equalTo(BAD_REQUEST)
			contentAsString(jsonResult) must contain ("Illegal argument")
		
		}

		"get a user list" in new WithApplication{
			val res = route(FakeRequest(GET,"/users")).get

      status(res) must equalTo(OK)

      contentAsString(res) must contain ("Tom")
		}

		"get a bad request " in new WithApplication{
			val res = route(FakeRequest(GET,"/users?page=1")).get

      status(res) must equalTo(BAD_REQUEST)

      contentAsString(res) must contain ("no such page")
		}

		"get second page" in new WithApplication{
			for(i <- 1 to 10)
			{
				val jsonBody = Json.parse(
				s"""
					{"name":"Lion$i","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
				"""
				)
				val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
				val jsonResult = route(req).get
				
				status(jsonResult) must equalTo(OK)
				contentAsString(jsonResult) must contain ("Add")
			}
			val res = route(FakeRequest(GET,"/users?page=1")).get

      status(res) must equalTo(OK)

      contentAsString(res) must contain ("Lion")
      contentAsString(res) must contain ("mail")
		}
	}
}
