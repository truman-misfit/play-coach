import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

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
  }
}
