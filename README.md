# play-coach

# Intention
This is internal play-coach project.We use this tutorial to make a user age record system, based on Play framework.

# Pre-install
- Scala 2.11.6
- Play! Framework 2.4.x
- sbt 0.13.8

# Usage
This is a inited project,you can simple run it from console,like this:
`cd $my_project_dir`
`./activator run`

You can also run test to check if this project is working on your mechine:
`./activator test`

- Play use 9000 as its default port, make sure this port is free for use
- If you can not use port 9000,You can run this project from the console like this:
`./activator run -Dhttp.port=1234`
and then you can use 1234 port for http request
- If you hang on 'Setting up Play fork run ... (use Ctrl+D to cancel)' when you try to run this project from console,
change the value of 'fork in run' to false in build.sbt:
`fork in run := false`