# What to do in Step 6
We can add user, and get one single user,or all user now.You may also want to query a special list of users.

In this step, we create a page.Every page have 10 user data record.Add a route to query user data on special page.

Now reset project to step 6.
```shell
cd play-coach
git checkout -f step6
```

# How to do
As usual, add a new route:

`GET			/users											controllers.UserController.getUserList(page:Option[Int])`
The page parameter is optional,default value is 0. Since we want to get a list of user, we use a GET method and 'users' URI.

We just abort the getAllUser request, and use this route instead. It's not good to get all user's data if there are lots of users.

And the code of getUserList:

In `app/controllers/UserController.scala`:
```scala
def getUserList(page:Option[Int]) = Action {
  val pageNum = page.getOrElse(0)
  try{
    Ok(UserData.getUserAtPage(pageNum))
  }
  catch{
    case e:IllegalArgumentException => BadRequest("no such page")
  }
}
```
The getUserAtPage method of UserData throw an IllegalArgumentException if the argument is too large or too little:

In `data_model/UserData.scala`
```scala
var userIndex:List[String] = List("Tom")

...

val recordInOnePage = 10

def getUserAtPage(pageNum:Int) =
{
  if(userIndex.size / recordInOnePage <= pageNum - 1 ||
    pageNum < 0)
    throw new IllegalArgumentException

  val beginIndex = pageNum * recordInOnePage
  val endIndex = {
    if((pageNum + 1) * recordInOnePage > userIndex.size)
      userIndex.size
    else
      (pageNum + 1)* recordInOnePage
  }
  var ret = ""
  for(index <- beginIndex until endIndex){
    ret += userData.name + ",age:"+ userData.age + ",tel:" + userData.tel + ",mail:" + userData.mail + ",gender:" + userData.gender + "\n"
  }
  ret
}
```
To make it easier to get all user's data on a page, add a list to record new user's name.You can see more details in UserData.scala.

This app is ready to query a list of users now.


# Testing
In `test/UserSpec.scala`:
```scala
"get a user list" in new WithApplication{
   val res = route(FakeRequest(GET,"/users")).get

   status(res) must equalTo(OK)

   contentAsString(res) must contain ("Tom")
}

"get a bad request " in new WithApplication{
   val res = route(FakeRequest(GET,"/users?page=1")).get

   status(res) must equalTo(BAD_REQUEST)

   contentAsString(res) must contain ("no such page")
}

"get second page" in new WithApplication{
   for(i <- 1 to 10)
   {
     val jsonBody = Json.parse(
     s"""
       {"name":"Lion$i","age":10,"tel":"13245","mail":"Tom@123.com","gender":"male"}
     """
     )
     val req = FakeRequest(POST, "/user").withJsonBody(jsonBody)
     val jsonResult = route(req).get

     status(jsonResult) must equalTo(OK)
     contentAsString(jsonResult) must contain ("Add")
   }
   val res = route(FakeRequest(GET,"/users?page=1")).get

   status(res) must equalTo(OK)

   contentAsString(res) must contain ("Lion")
   contentAsString(res) must contain ("mail")
}
```
There are three test case for this step.First one test if this route work.Second one test if it can get an error if the page parameter is too large.The last one test if page parameter work .

# summary
In this step ,we add an action to query a list of user.
