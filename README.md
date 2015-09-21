# What to do in Step 7
We have enough method to operate UserData now.But as a developer,it's important to know how many requests' in and how long does it take to response a request.

In this step,we add a filter to log all request ,and calculate how long does it take to response a request.

Now reset project to step 7.
```shell
cd play-coach
git checkout -f step7
```

# How to do
Create a new filter in dir `custom_filter`:

In `app/custom_filter/LogFilter.scala`:
```scala
class LoggingFilter extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>

      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      Logger.info(s"From:${requestHeader.remoteAddress},${requestHeader.method} ${requestHeader.uri} " +
        s"took ${requestTime}ms and returned ${result.header.status}")

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
```
The LoggingFilter extends the Filter, and finished the apply(RequestHeader =>Future[Result])(RequestHeader):Future[Result] method.We can add a Logger.info call in the map call.

We also add a Request-Time in the response's header, so client can know how long does it take the server to response.

Now the filter is ready.Add this filter to the filter sequence:

In `app/Global.scala`
```scala
class Filters @Inject() (
  log: LoggingFilter
) extends HttpFilters {

  val filters = Seq(log)
}
```
Now our filter work.

# Testing
In `test/FilterSpec.scala`:
```scala
"Respone should have time" in new WithApplication{
  val res = route(FakeRequest(GET,"/users")).get
  status(res) must equalTo(OK)
  val getHeaderResult = res.value.get.get.header.headers.get("Request-Time") match{
    case Some(timeInHeader) => "Success"
    case _ => "error"
  }
  getHeaderResult must equalTo("Success")
}
```
As we can not test the log, we just test if the response contains a Request-Time in Header.

# summary
In this step ,we add a filter,to log every request and add the response time in the header.
