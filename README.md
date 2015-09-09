# play-coach

# Intention
This is internal play-coach project.We use this tutorial to make a user age record system, based on Play framework.

# Pre-install
- Scala 2.11.6
- Play! Framework 2.4.x
- sbt 0.13.8

# Requirement
- Tools in Pre-install
- Play use 9000 as its default port, make sure this port is free for use
- If you can not use port 9000,You can run this project from the console like this:
`./activator run -Dhttp.port=1234`
and then you can use 1234 port for http request
- If you hang on 'Setting up Play fork run ... (use Ctrl+D to cancel)' when you try to run this project from console,
change the value of 'fork in run' to false in build.sbt:
`fork in run := false`

# Tutorial
- step1:
In this step, we need to know how Play framework work with action.We add a simple GET action to get user list, add a form page to add user, and an action to handle post data, and finally we add a LoggingAction to log all request
See README_step1.md for further info.

# Usage
1 Enter this project
2 run `./activator run`
3 If you wanna use other port then 9000, use `./activator run -Dhttp.port=1234` instead.
4 If you wanna test this project, run `./activator test`
