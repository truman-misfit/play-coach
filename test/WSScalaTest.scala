import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

import org.mockito.Mockito._

import controllers._

class ExampleMockitoSpec extends PlaySpec with MockitoSugar{
  "WS Controller" should{
    "return a list if " in {
      val mockTestObj = mock[MockTestObj]
      when(mockTestObj.getSize) thenReturn 20

      val actual = mockTestObj.getSize
      actual mustBe 20
    }
  } 
}