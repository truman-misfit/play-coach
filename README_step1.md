# guide for step 1
So far we have a data model called NameRecord, it has two parameter: name and age.Our system will base on this model.(See NameRecord.scala for more detail)
- I have just added a Composite action,who will log all request before handle it.See LoggingAction.scala for more details.
- Add a test sample : UserSpec.scala.You can see this file to know more about how to use this application

Assume that you use 9000 as the http request's port,you can visit the app from your browser like this:

- localhost:9000/
You can see the welcome page of Play. See index in controller/Application.scala and template engine for more details.

- localhost:9000/users
Get all users' data. See getUsers in controllers/UserDataController.scala for more details.

- localhost:9000/addUser
Open a page which have two input box, one for name ,another for age.Fill in and push "Add user"button to send a post request.
See userDataForm in controllers/UserDataController.scala ,views/AddUser.scala.html and data_model/NameRecord.scala for details about this page.You may also need some knowlage about template engine, too.
See addUser in contollers/UserDataController.scala for more details about a post request.

- localhost:9000/user?name=Tom
Get a user's data,whose name is Tom.Change Tom to any user name you'd like to query.See getUser in controllers/UserDataController for more details.You may also curiouse routes ,too.