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
class UsersSpec extends Specification {
  "Application" should {
    "Get A Inited name list" in new WithApplication{
        val users = route(FakeRequest(GET, "/users")).get
        
        status(users) must equalTo(OK)
        contentAsString(users) must contain ("Registered user list:")
    }
    
    "Get a user's data if this user is in the list" in new WithApplication{
        val users = route(FakeRequest(GET, "/user?name=Tom")).get
        
        status(users) must equalTo(OK)
        contentAsString(users) must contain ("Find user :Tom,age:10")
    }
    
    "Return error if the user is not the list" in new WithApplication{
        val users = route(FakeRequest(GET, "/user?name=Jerry")).get
        
        status(users) must equalTo(OK)
        contentAsString(users) must contain ("Unknow user name")
    }
    
    "Return error if the user name is no specialfied" in new WithApplication{
        
        val users = route(FakeRequest(GET, "/user")).get
        
        status(users) must equalTo(OK)
        contentAsString(users) must contain ("No user name specialfied")
        
    }
    
    //Test sample for form submit
    "submit a form and return submitted data" in new WithBrowser {

      browser.goTo("http://localhost:" + port + "/addUser")
    
      browser.pageSource must contain("Minimum value: 0")
      
      browser.fill("#Name").`with`("Max")
      browser.fill("#Age").`with`("10")
      browser.fill("#Tel").`with`("123456")
      browser.fill("#Gender").`with`("male")
      browser.fill("#Mail").`with`("Longlon@123.com")
      
      browser.submit("#up")
      browser.pageSource must contain("receive a user,name:Max age:10")
      
      browser.goTo("http://localhost:" + port + "/users")
      
      browser.pageSource must contain("Max")
    }
  }
}






