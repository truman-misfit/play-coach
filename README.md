# play-coach

# Intention
This is internal play-coach project.A project for new employee training

# Pre-install
- Scala 2.11.6
- Play! Framework 2.4.x
- sbt 0.13.8

# Requirement
- Tools and frameword in Pre-install.
- Application will run on port 9000,so make sure this port is free.
- If you want to change your port,use -Dhttp.port=1234 to run your app.Change 1234 to any port that you want to use:
`./activity run -Dhttp.port=1234`

# Git commit sample
`git commit -m [MC-1506] add a request test case.`

# Usage
cd to the root of this project ,and run command:
`./activator run`
Or you can just run it from your IDE.

Then you can use your browser to send request to app(if you use other port to run your app,change 9000 to that port):

http://localhost:9000  
Open the index.html.You can use the form to post a data to server,too.Just write some data in the inputbox,and push the TestPost button.

http://localhost:9000/composeAction 
This is a request for a composed action,you can see two logs if you set <root level> to "DEBUG" in play-coach/conf/logback.xml.

http://localhost:9000/asynAction 
This action will return after the server sleep for one second. You can use the follow one to set the sleep time,too.

http://localhost:9000/asynAction?time=5000 
This action will return after the server sleep for 5000 microsecond.Change 5000 to any time you want the server sleep.

http://localhost:9000/json 
Test the json parse function,see "getJsonValue" in  controllers/JsonController for more details.

http://localhost:9000/addName/[name]/[age] 
add (name,age) pair to the server's NameRecord list,and it returns the json string for the NameRecord Json module.

http://localhost:9000/parseStr 
parse the json string get from above.See parseStr in controllers/JsonController for more details.

http://localhost:9000/jsonInHttp
Demo for json parameter in http request.See sendJsonRequest in controllers/WSController and httpJsonReq in controllers/JsonController for more details.

# Future
Add a more complex route.
Maybe we will implement this repo to a user-age managing system

# Author:
Truman.

Contributor:
John
