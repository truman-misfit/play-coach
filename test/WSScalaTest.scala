import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

import org.mockito.Mockito._

import controllers._

class ExampleMockitoSpec extends PlaySpec with MockitoSugar{
  "WS Controller" should{
    "return 20 ,not 10 as the return of get " in {
      val mockTestObj = mock[MockTestObjClass]
      when(mockTestObj.getSize) thenReturn 20
      
      val testObj = new MockTestRootClass{
          override val testObj = mockTestObj
      }
      
      val actual = testObj.get
      actual mustBe 20
    }
  } 
}