import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class FilterSpec extends Specification {

  "Filter" should {
		"Respone with Requeste-Time" in new WithApplication{
			val res = route(FakeRequest(GET,"/users")).get
      status(res) must equalTo(OK)
			val getHeaderResult = res.value.get.get.header.headers.get("Request-Time") match{
				case Some(timeInHeader) => "Success"	
				case _ => "error"
			} 
			getHeaderResult must equalTo("Success")
		}
	}
}
