# What to do in Step 5
As we can convert between JSON value and UserData now, we can now add request to add user data to our server now.

As a RESTful system, we use POST method to add a resource.In this step, we add an action to handle post request,if all goes well,we add the user to our system.

Now reset project to step 5.
```shell
cd play-coach
git checkout -f step5
```

# How to do
First of all ,add a new route:

In `conf/routes`

`POST    /user                       controllers.UserController.addUser`

Then add a new Action:

In `controllers/UserController.scala`
```scala
def addUser = Action {request =>
  try{
    val body = request.body
    val jsonValue = body.asJson
    jsonValue match{
      case Some(jsonResult)  =>{
        val jsonReadResult = jsonResult.validate[UserData]
        jsonReadResult match{
          case success:JsSuccess[UserData] => {
              UserData.addUser(success.get)
              Ok("Add a new user:" + success.get.name)
            }
         case e:JsError => Ok("Illegal argument!")

         }
      }
      case None => Ok("Illegal argument!")
    }
  }
  catch{
    case e:IllegalArgumentException => Ok("Illegal argument!")
  }

}
```
It accept a request with JSON value, and convert the JSON value to UserData.

If everything goes well, add this user to UserData:

In `data_model/UserData.scala`:
```scala
def addUser(user:UserData){
  data = data + (user.name -> user)
}
```
When somebody post a /user ,we add a new user in our server if the data is validated.
That's all.
# Testing
In `test/UserSpec.scala`:
```scala
"Receive and add user" in new WithApplication{
  val jsonBody = Json.parse(
  """
    {"name":"Tim","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
  """
  )
  val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
  val jsonResult = route(req).get

  status(jsonResult) must equalTo(OK)
  contentAsString(jsonResult) must contain ("Add")
}

"return illegal argument" in new WithApplication{
  val jsonBody = Json.parse(
  """
    {"name":"Tom","age":210,"tel":"13245","mail":"Tom@123.com","gender":"male"}
  """
  )
  val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
  val jsonResult = route(req).get

  status(jsonResult) must equalTo(OK)
  contentAsString(jsonResult) must contain ("Illegal argument")

}
```
In the first test,we post a request to add a new user named "Tim".This request can success because all arguments are validated.

In the second test, we are expected to get a response with "Illegal argument" because the age is too old.
# summary
In this step ,we add a post method to add user.
