# guide for step 1
Now let's reset our project to setp1:
`git checkout step1`

# 1,We first define the NameRecord class:
`case class NameRecord(name:String, age:Int)`
So we have a data struct now,which can use to storage user's info(now we only have name and age)
And then we create a companion object so we can easily access to it.Add a list of NameRecord in it:

`var nameRecordList:List[NameRecord]`

We add a method to add record in this list:

def addName(name:String, age:Int) = {
    nameRecordList = nameRecordList :+ NameRecord(name,age)
}
    
We also add some method to get info of NameRecordList,like:
def getUser(name:String) : String = {
    nameRecordList foreach({ record =>
        if(name == record.name)
            return "Find user :" + record.name + ",age:" + record.age
    })
    throw new IllegalArgumentException
}

def getAllUser:String = {
    var ret = ""
    nameRecordList foreach(record =>{
        ret += "Name:" + record.name + " ,age:" + record.age + "\n"
    })
    ret
}

# 2,Add a controller to control user data:
In controllers/UserDataController.scala:
`class UserDataController extends Controller`

# 3,Add an action to get the full user list:
In controllers/UserDataController.scala,insert these codes under UserDataController:
def getUsers = Action{
    var ret = "Registered user list:\n"
    
    val nameList = NameRecord.nameRecordList
    nameList foreach(record => {
        ret += "Name:" + record.name + ",age:" + record.age + "\n"
    })
    Ok(ret)
}

and then add a new URI in conf/routes:
'GET     /users                      controllers.UserDataController.getUsers'

Now run you app and open your browser,visit 
`http://localhost/users`

# 4,Add an compose action:
To log each request,we may need to add
`Logger.info("Request is : " + request.toString)`
to every respone.
But you can do it easier,like ,use a composite action.
Try to add a new action,like:
In action/LoggingAction.scala:

object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Request is : " + request.toString)

    //do more thing
    block(request)
  }
}

Now use this new action to get a user's data:
In controllers/UserDataController.scala,insert these codes under UserDataController:

def getUser(name:Option[String]) = LoggingAction{
  var ret = "No user name specialfied"
  name foreach({ nameInOption =>
      ret = 
      try
      {
          NameRecord.getUser(nameInOption)
      }
      catch
      {
      //NameRecord.getUser throw a IllegalArgumentException if there is no such user
          case ex: IllegalArgumentException => "Unknow user name"
      }
  })
  Ok(ret)
}

Don't forget to import actions._ 

Add a new URI in routes:
GET     /user                       controllers.UserDataController.getUser(name:Option[String])

Now run your app,and visit:
`http://localhost:9000/user?name=Tom`
You can see you log in console

# 5,Add a page to add user to our system.
We will need a page with form,which have two input box,one for name ,another for age.
So we define a form in controller/UserDataController.scala:
val userDataForm: Form[NameRecord] = Form{
    mapping(
      "Name" -> nonEmptyText,                       //name can not be empty
      "Age" -> number.verifying(min(0), max(140))   //age is between 0 and 140
    )(NameRecord.apply)(NameRecord.unapply)
}

Add a template in veiw:
AddUser.scala.html:
```
@import data_model._
@(myForm :Form[NameRecord])(implicit messages: Messages)

@import helper._

@main("Add User"){
    @helper.form(action = routes.UserDataController.addUser()) {
      @helper.inputText(myForm("Name"))
      @helper.inputText(myForm("Age"))
      <div class="buttons" >
      <input type="submit" id = "up" value="Add User"/>
      </div>
    }

}
```
so we add an action to show this page:
In controllers/UserDataController.scala,insert these codes under UserDataController:
def addUserPage = LoggingAction{
    //use userDataForm to init AddUser.html
    Ok(views.html.AddUser(userDataForm))
}

Add to route:
GET     /addUser                    controllers.UserDataController.addUserPage

and you nedd an action to recive the post data:
In controllers/UserDataController.scala,insert these codes under UserDataController:
def addUser = LoggingAction{implicit request=>
      //get data from userDataForm
      userDataForm.bindFromRequest.fold(
      //if error
      formWithErrors => Ok("commit error"),
      //if all goes well
      {
        myData => {
            NameRecord.addName(myData.name,myData.age)
            Ok("receive a user,name:" + myData.name + " age:" + myData.age)
        }
      }
    )
}

Add to route:
POST    /user                       controllers.UserDataController.addUser
so far, run you app, visit:
`http://localhost:9000/addUser`
fill the input box and click the Add user button.See what happens











