# guide for step 2
Now let's reset our project to setp1:
`git checkout step2`

# 1 Add a new controller to handle async request
In controllers/AsyncUserDataController.scala,we define a new controller to handle async request:
`class AsyncUserDataController extends Controller`

You can use default action and async aciton in a same controller, but for our tutorial, it's good to define a new controller in one step,so you can easily see the difference between each step.

# 2 Add an async action to handle long time calculation.

In controllers/AsyncUserDataController.scala,insert the following codes:
```
def getUserData = Action.async{ request =>
    Logger.info("Before Future calculation")
    //create a future
    val futureRet = scala.concurrent.Future { 
        Logger.info("In Future calculation")
        val ret = NameRecord.getAllUser 
        Logger.info("After Future calculation")
        ret
    }
    Logger.info("Out of the future")
    
    //get the result from the future
    futureRet.map(
        {ret => Ok(views.html.AllUser(ret))
    })
}
```
Method getAllUser in NameRecord looks like this:
def getAllUser:String = {
    var ret = ""
    nameRecordList foreach(record =>{
        ret += "Name:" + record.name + " ,age:" + record.age + "\n"
    })
    ret
}

If you don't have so many data in list, and you want to see how async acton work,you can add sleep in getAllUser,like this:
def getAllUser:String = {
    var ret = ""
    nameRecordList foreach(record =>{
        ret += "Name:" + record.name + " ,age:" + record.age + "\n"
    })
    //sleep for 5 second
    Thread sleep 5000
    ret
}

# 3 Add the template
We use a template called AllUser in views/AllUser.scala.html,this template should looks like this:
```
@(allUserStr:String)
@import data_model._
@main("All user"){
    <h1>Now we show all users here: </h1>
    <h5>@allUserStr</h5>
    <H1>Default user is : </H1>

    <H5>Tom, @NameRecord.getUser("Tom")</H5>

}
```
Be attention to the magic '@' mark.
First line,announce that this template needs a string as input.
Fifth line,use the input:allUserStr as part of this html page.
Eighth line,call NameRecord.getUser to get Tom's data.

# 4 Add route
Add this route in your conf/routes file
`GET     /users/async                controllers.AsyncUserDataController.getUserData`

Now you can test you app by visiting
`http://localhost:9000/users/async`

# 5 Action is non-blocking,so why do I need Action.async?
Actually, Action is non-blocking ,too.But Actin returns Result, and Actin.async returns Future[Result].
So if you use Future in you method, you can return it directly by using map. 
You can also use action to handle Future,too.In our case,code should looks like this:
```
def getUserData = Action{ request =>
    Logger.info("Before Future calculation")
    
    val futureRet = scala.concurrent.Future { 
        Logger.info("In Future calculation")
        val ret = NameRecord.getAllUser 
        Logger.info("After Future calculation")
        ret
    }
    Logger.info("Out of the future")
    //futureRet.map(
      //  {ret => Ok(views.html.AllUser(ret))
    //})
    
    //get the Future's return by yourself
    while(false == futureRet.isCompleted)
    {
        
    }
    val ret = futureRet.value.get.get
    Ok(views.html.AllUser(ret))
}
```

This code works fine,too.


Fill your code in the files,and run test to see if your code runs well.


