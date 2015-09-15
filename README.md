# What to do in Step 1
In this step ,we just init this project and see how play work.How play's controllers work.
Clone the repo by:
`git clone https://github.com/truman-misfit/play-coach.git`
Then reset project to step 1. 
```
cd play-coach
git checkout -f step1
```

# How to do
So we have a play project now.You can run this project by this command:
`./activator run`
Or you can start the Typesafe ui by:
`./activator ui`
and then run the app in the IDE.
If all goes well ,you should see the following output:
`Listening for HTTP on /0:0:0:0:0:0:0:0:9000`

Now you can visit `http://localhost:9000` in your broswer, you should get play's welcome page.

- How the app work
When you visit `http://localhost:9000`, you actually access to `http://localhost:9000/`.If you see the routes in conf,you should see this:
In `app/conf/routes`:
`GET     /                           controllers.Application.index`
So when you request '/', you call the index method in controllers.Application:
In `controllers/Application`:
```
def index = Action {
  Ok(views.html.index("Your new application is ready."))
}
```

Method index returns an Ok(views.html.index("Your new application is ready.")) to the client.
You can find code about views.html.index in `app/views/index.scala.html`, this is a template, we will discuss about template in the following step.

# Testing
Run this command to test if your app work right:
`./activator test`
If your app pass all test case , you will get an output just like the following one:
`[success] Total time: 10 s, completed 2015-9-14 18:34:19`

If you use TypeSafe IDE, you can use test in IDE instead.

Test codes are under the 'test' dierectory,
In `test/ApplicationSpec.scala`:
```
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beSome.which (status(_) == NOT_FOUND)
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }
  }
}
```
In this test,it has two sample,one request for the '/boum' URI,which is not exist.So the returned status is NO_FOUND.
And another test request for '/',which is the index.html,containing a message 'Your new application is ready.' . So the response should have a OK statue, and the content should contain a message:'Your new application is ready.'


# summary
In this very first step, we know about how a Play app response to a request .

