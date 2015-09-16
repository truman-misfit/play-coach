import data_model._
import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

import collection.mutable.Stack
import org.scalatestplus.play._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */

class JsonSpec extends PlaySpec {

  "A JSON" must {
    "Convert to UserData" in {
			val json = Json.parse(
			"""
				{"name":"Tom","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
			"""
			)
			val jsonResult = json.validate[UserData]
			jsonResult.get mustBe UserData("Tom",10,"13245","Tom@123.com","male")

			val jsonstr = Json.toJson(jsonResult.get).toString
			jsonstr must include("Tom")
			jsonstr must include("10")
			jsonstr must include("13245")

    }

    "throw NoSuchElementException if an empty stack is popped" in {
      a [IllegalArgumentException] must be thrownBy {
				UserData("Tom",10,"13245","Tom@123.com","lmale")
      }
      a [IllegalArgumentException] must be thrownBy {
				UserData("Tom",210,"13245","Tom@123.com","male")
      }
      a [IllegalArgumentException] must be thrownBy {
				val json = Json.parse(
				"""
					{"name":"","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
				"""
				)
				val jsonResult = json.validate[UserData]
      }
    }
  }
}
