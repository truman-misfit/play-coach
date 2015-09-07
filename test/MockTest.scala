import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

import org.mockito.Mockito._

class TestRootClass{
  val testObj = new TestObjClass()
  def get = testObj.getSize
}

class TestObjClass{
  val size: Int = 10
  def getSize = size
}

class MockitoSpec extends PlaySpec with MockitoSugar{
  "MockTest obj " should{
    "return 20 ,not 10 as the return of get " in {
      val mockTestObj = mock[TestObjClass]
      when(mockTestObj.getSize) thenReturn 20
      
      val testObj = new TestRootClass{
          override val testObj = mockTestObj
      }
      
      val actual = testObj.get
      actual mustBe 20
    }
  }
}
