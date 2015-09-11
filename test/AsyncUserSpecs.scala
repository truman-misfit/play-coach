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
class AsyncUsersSpec extends Specification {

    "Application" should {
        "return default user list" in new WithApplication{
            val users = route(FakeRequest(GET, "/users/async")).get
        
            status(users) must equalTo(OK)
            contentAsString(users) must contain ("Tom")
        }
    }
}