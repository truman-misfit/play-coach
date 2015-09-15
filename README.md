# What to do in Step 2
In this step ,we will begin our RESTful play app design.Our final aim is to design a User Data system,which can get user's data, add user data, and query a special user's data.

As we already know how play works, we will add a controller and an action to get user's data. When a client request to GET user's data,we return who is the user.
For example, we query about Tom,and server return a string:"You query about Tom"

Now reset project to step 2.
```shell
cd play-coach
git checkout -f step2
```

# How to do
As we already know about how Play framework work,we first add a new route:

In `conf/routes`:

`GET    	/user/:name					controllers.UserController.user(name:String)`

This route means that we want to get a resource, which is one of user, and the name is [name].And this route will call controllers.UserController.user(name:String)

```
What is the meaning of ':name'?
':name' means this URI reads a string as a parameter called 'name'.
So you can visit this URI like:
'/user/Tom',this request will finally call to 'controllers.UserController.user("Tom")'

```

So we need a controller called 'UserController',who will have a method called 'user'.

In controllers, create a new file called UserController.scala.Ctreate a new class called UserController:

`class UserController extends Controller`

This controller extends 'Controller',so it can be a 'Controller'.

Then we add an action called 'user'

In `controllers/UserController.scala`:
```scala
def user(name : String) = Action {
  Ok(s"You query about: $name.")
}
```
This action accept a String as input ,and response with a String:"You query about: $name."

That's all, you can run you app to get a query now.

# Testing
In `test/UserSpec.scala`:
```scala
class UserSpec extends Specification {

  "Application" should {
    "return a query result" in new WithApplication{
      val home = route(FakeRequest(GET, "/user/Tom")).get

      status(home) must equalTo(OK)
      contentAsString(home) must contain ("You query about: Tom.")
    }
  }
}
```
In this test,we expect the app return "You query about: Tom." when we visit a URI /user/Tom.

# summary
In this step ,we learn about how to get a resource.
