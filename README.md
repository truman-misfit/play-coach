# What to do in Step 3
In step 2, we already added an action to query a user's data.But we don't have any data ,yet. So, it's time for us to add a data struct, to save user's data.As we use JSON value to transform data, it's important to serialize this data struct as a JSON value,too.

Now reset project to step 3.
```shell
cd play-coach
git checkout -f step3
```

# How to do
Add a new directory named 'data_model' ,and a new file named 'UserData.scala' under this folder.

In `data_model/UserData.scala`, create a new class:
```scala
case class UserData(name:String, age:Int, tel:String, mail:String, gender:String)
```
So we can save user data as UserData now.It will contain five fields:name,age,tel,mail,and gender.
And we make some require for this Data struct,like ,name should not be empty, age is between 1 and 140(Including 1 and 140), telephone numbers should contain only number,a mail address should contain one and only one '@'.

In `data_model/UserData.scala`,we add a method to test if the data is validate:
```scala
case class UserData(name:String, age:Int, tel:String, mail:String, gender:String)
{
	def isNameValidated:Boolean = (name.size != 0)

	def isAgeValidated:Boolean = (age > 0 && age <= 140)

	def isMailValidated:Boolean =
	{
		var atNum = 0
		for(i <- 0 until mail.length if mail.charAt(i) == '@'){
			atNum = atNum+1
		}
		if(atNum != 1) {
			false
		}
		else
		{
			true
		}
	}

	def isTelValidated:Boolean =
	{
		if(tel.size == 0){
			false
		}
		else
		{
			var ret = true
			for(i <- 0 until tel.length)
			{
				if(false == tel.charAt(i).isDigit)ret = false
			}
			ret
		}

	}

	def isGenderValidated:Boolean =
	{
		gender.toLowerCase match{
			case "female" => true
			case "male" => true
			case _ => false
		}
	}

	def validation:Boolean =
	{
	  isNameValidated && isAgeValidated && isTelValidated && isMailValidated && isGenderValidated
	}

	require(validation)
}
```

A server should save lots of user's data, so we need a Map to save all user's data.And for convenience,we add a companion object for UserData

In `data_model/UserData.scala`:
```scala
object UserData{
  var data:Map[String,UserData] = Map("Tom" -> UserData("Tom",10,"13245","Tom@123.com","male"))
 ...
 }
```
Init the data map with a new user,"Tom". We don't have any way to add user now,so just init the data map with a user.You can use `Map[String,UserData].empty` to init an empty map.

We can save our user's data on server now.Let's add a request to get all user's data.
First we add a new route:

In `conf/routes`

`GET     /users                      controllers.UserController.getAllUsers`

This URI allow us to get users on server.

Then add a new action in UserController:

In `app/controllers/UserController`
```scala
def getAllUsers = Action{
  Ok(UserData.toString)
}
```

The toString method in object UserData looks like this:
```scala
override def toString = {
  var ret = ""
  data.foreach(user => ret = ret + "Name:" + user._1 + ",gender:" + user._2.gender + ",tel:" + user._2.tel + ",mail:" + user._2.mail + "\n")
  ret
}
```

# Testing
In `test/UserSpec.scala`:
```scala
"return a string" in new WithApplication{
  val res = route(FakeRequest(GET,"/users")).get

  status(res) must equalTo(OK)

  contentAsString(res) must contain ("Tom")
  contentAsString(res) must contain ("gender")
  contentAsString(res) must contain ("mail")

}

```
As we already have a user named "Tom", so our request should return Tom's data.

# summary
In this step ,we design the UserData data struct.
