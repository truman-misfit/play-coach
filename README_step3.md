# guide for step3
Now let's reset our project to setp3:
`git checkout step3`

# 1 make NameRecord more complex
A user can have mail,telephone record ,and gender.So add these fieds to NameRecord:
`case class NameRecord(name:String, age:Int, mail:String, tel:String, gender:String)`

# 2 modify old codes
NameRecord is changed, so old codes may get error, like Form[NameRecord] in controllers/UserDataController.scala, browser test in test/UsersSpec.scala.
Just make it compileable,like this:

In controllers/UserDataController.scala:
```
val userDataForm: Form[NameRecord] = Form(
    mapping(
      "Name" -> nonEmptyText,                       //name can not be empty
      "Age" -> number.verifying(min(0), max(140)),   //age is between 0 and 140
      "Mail" -> nonEmptyText,
      "Tel" -> nonEmptyText,
      "Gender" -> nonEmptyText
    )(NameRecord.apply)(NameRecord.unapply) 
  )
```

We will add validation in follow step
 
# 3 convert NameRecord to Json
To save NameRecord in Json stlye ,we should convert NameRecord to json.We add Reads[NameRecord] and Writes[NameRecord] to NameRecord:
In data_model/NameRecord.scala, add these codes:
```
implicit val locationReads: Reads[NameRecord] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "age").read[Int] and
    (JsPath \ "mail").read[String] and
    (JsPath \ "tel").read[String] and
    (JsPath \ "gender").read[String]
)(NameRecord.apply _)
        
implicit val placeWrites: Writes[NameRecord] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "age").write[Int] and
    (JsPath \ "mail").write[String] and
    (JsPath \ "tel").write[String] and
    (JsPath \ "gender").write[String]
)(unlift(NameRecord.unapply))
```
so we can convert NameRecord to JsValue like this:
`val josn = Json.toJson(NameRecord(....))`

and JsValue to NameRecord:
```
val json = Json.parse("....")
val jsonResult = json.validate[NameRecord]
    
jsonResult match{
    case j : JsSuccess[NameRecord] => {
        val ret = j.get
        //ret is a NameRecord now
    }
    case e: JsError => {
        ....
        //if json can not convert to NameRecord,
        //handle error here
    }
}
```

# 4 save json data and load it
We just load json data when our app start and save it when app stop.
Add a new file under app,name it GlobalSetting.scala
Add these codes in it:
```
override def onStart(app: Application) {
    Logger.info("Application is started!!!")
    try{
        //What I wanna tell you is that ,Source.fromFile returns a BufferedSource
        //BufferedSource.toString returns "empty iterator" or "non-empty iterator",
        //not the file's data...
        //Strange hah?
        
        val source = Source.fromFile("json.txt", "UTF-8").mkString
        Logger.info("source is :"+source)
        NameRecord.load(source)
        Logger.info("load end")
    }
    catch{
        case e:java.io.FileNotFoundException => {
            Logger.info("No Json data")
        }
    }
}
  
override def onStop(app: Application){
    Logger.info("Application is stopped!!")
    val out = new PrintWriter("json.txt")
    out.println(NameRecord.toJson.toString)
    out.close()
}
```

# 5 Validate the data of NameRecord
As we all know ,mail address should look like this:
"xxx@xxxx"
and telephone no. contains only numbers,gender should be "male" or "female", and so on.

In our old codes,we have a form to submit new user data,now we want to check if the data is validated
We add a method to check if a NameRecord is validated:
In data_model/NameRecord.scala,add these codes:
```
//calidate function for form
def validateNameRecord(record:NameRecord): Boolean = {
    Logger.info("In validate func")
    if(record.age < 0 || record.age > 140) return false
    
    {
        var atNum = 0
        for(i <- 0 until record.mail.length if record.mail.charAt(i) == '@'){
            atNum = atNum+1
        }
        if(atNum != 1) {
            Logger.info("mail adress is not validate")
            return false
        }
    }
        
    {
        for(i <- 0 until record.tel.length)
        {
            if(false == record.tel.charAt(i).isDigit)return false
        }
    }
    
    
    record.gender.toLowerCase match{
        case "female" => true
        case "male" => true
        case _ => false
    }
}
```
You can write you own validation function,too.

Now validate form data like this:
```
val userDataForm: Form[NameRecord] = Form(
    mapping(
      "Name" -> nonEmptyText,                       //name can not be empty
      "Age" -> number.verifying(min(0), max(140)),   //age is between 0 and 140
      "Mail" -> nonEmptyText,
      "Tel" -> nonEmptyText,
      "Gender" -> nonEmptyText
    )(NameRecord.apply)(NameRecord.unapply) verifying("Illegal input!", fields => fields match{
         case input => NameRecord.validateNameRecord(input)
     })
)
```
You can also validate Json input like this:

```
implicit val locationReads: Reads[NameRecord] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "age").read[Int] and
    (JsPath \ "mail").read[String] and
    (JsPath \ "tel").read[String] and
    (JsPath \ "gender").read[String]
)(NameRecord.apply _).filter(validateNameRecord(_))
```

# 6 Try to get json value of NameRecord list
Add a new controller in controllers/JsonController.scala:
`class JsonController extends Controller `

Add an action to it:
```
def getJson = LoggingAction{
    Ok(NameRecord.toJson.toString)
}
```
NameRecord.toJson = Json.toJson(nameRecordList)

Add new route to conf/routes:
GET     /json                       controllers.JsonController.getJson

Now you can see the json value of your nameRecordList.

# 7 Post json data as parameter
We can use json data as parameter in http request,
add these codes in JsonController:
```
def recvJson = LoggingAction{ request =>
    Logger.info(request.body.toString)
    val json = request.body.asJson.get.validate[NameRecord]
        
    json match{
        case j : JsSuccess[NameRecord] => {
            val ret = j.get
            NameRecord.addName(ret.name,ret.age,ret.mail,ret.tel,ret.gender)
            Ok("copy")
        }
        case e: JsError => {
            Ok("data error!Can not add")
        }
    }
}
```

Add a new route
POST    /json                       controllers.JsonController.recvJson

Now others can post you a request with json in body,who can be validated as a nameRecord.
See test case in test/JSONSpecs.scala:
```
"Receive error " in new WithApplication{
    val jsonBody = Json.toJson(NameRecord("Loli",20,"loli@lolicon.com","2345","female"))
    Logger.info("Json str:"+jsonBody.toString)
    val req = FakeRequest(POST, "/json").withJsonBody(jsonBody)
    val jsonResult = route(req).get
    
    status(jsonResult) must equalTo(OK)
    contentAsString(jsonResult) must contain ("copy")
}
```

Fill the codes and run test to see if your codes do the right job.


















