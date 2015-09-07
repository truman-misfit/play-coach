import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import org.specs2.mock._

import controllers._
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class AsyncActionSpecs extends Specification with Mockito{

  "AsyncAction" should {
    "return in 1 second" in new WithApplication{
      val home = route(FakeRequest(GET, "/asynAction")).get

      status(home) must equalTo(OK)
      contentAsString(home) must contain ("Sleep for")  
    }
    
    "return 10" in new WithApplication {
      val mockLongTimeCal = mock[LongTimeCal]
      mockLongTimeCal.calWithSleep(1) returns 10
      
      //The following is compileable ,too.But this means another method,
      //so when you call myController.getReturn(1),the mock object can get nothing,so return in 0
      
      //mockLongTimeCal.calWithSleep(_:Int) returns 10
      
      val myController = new AsyncController() {
        override val longTimeCalculator = mockLongTimeCal
      }
      
      val actual = myController.getReturn(1)
      actual must equalTo(10)
    }
  }
}