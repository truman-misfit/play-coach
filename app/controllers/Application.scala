package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    val intArray = Array(1,2,3)
    for (num <- intArray)println(num)
    Ok(views.html.index("Your new application is ready."))
  }

}
