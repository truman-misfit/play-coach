# What to do in Step 4
Until now, we already have a UserData data type.It's time to convert it to a JSON value,so we can send a UserData as JSON value in a request.

Now reset project to step 4.
```shell
cd play-coach
git checkout -f step4
```

# How to do
First we convert UserData to a JSON value:

In `data_model/UserData.scala`
```scala
//from JSON to UserData
implicit val userReads: Reads[UserData] = (
  (JsPath \ "name").read[String] and
  (JsPath \ "age").read[Int] and
  (JsPath \ "tel").read[String] and
  (JsPath \ "mail").read[String] and
  (JsPath \ "gender").read[String]
)(UserData.apply _).filter(_.jsonValidation)

//from UserData to JSON value
implicit val userWrites: Writes[UserData] = (
  (JsPath \ "name").write[String] and
  (JsPath \ "age").write[Int] and
  (JsPath \ "tel").write[String] and
  (JsPath \ "mail").write[String] and
  (JsPath \ "gender").write[String]
)(unlift(UserData.unapply))
```
Play's JSON lib needs a implicit Reads[T] and implicit Writes[T] value to convert between JSON value and other data type.JsPath represents the location of data in a JsValue structure.

In the Reads value ,there is a filter, use to check if the result is validated."jsonValidation" looks like :

In `data_model/UserData.scala`:
```scala
def jsonValidation:Boolean = {
  validation
}
```
Yes we just use the same method in last step to check if the data is validated.Of course you can rewrite this method.

We can convert UserData to JSON value like this:
```scala
Json.toJson(UserData("Tom",10,"13245","Tom@123.com","male"))
```
Convert JSON value to UserData like this:
```scala
try{
  val jsonResult = Json.parse("....").validate[UserData]
  jsonResult match{
      case j : JsSuccess[UserData] => {
          val ret:UserData = j.get
      }
      case e: JsError => {
          //handle error
      }
  }
}
catch{
  case e:IllegalArgumentException => Logger.info("create fail")
}
```
The validate[UserData] method in JsValue try to read the JsValue as a UserData,and return it as a JsSuccess[UserData] if everything goes well.

We use try-catch here because the 'require' method called in class UserData throws an IllegalArgumentException if validation return false.


# Testing
In `test/JSONSpec.scala`:
```scala
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

    "throw IllegalArgumentException if an illegal argument is used to create UserData" in {
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
```
In the first test,we test if we can convert between JSON value and UserData.

In the second test, we test about validation.When the data is not validated, throw an exception.

# summary
In this step ,we make conversion between UserData and JSON value.
