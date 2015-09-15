package controllers

import play.api._
import play.api.mvc._

class UserController extends Controller {

  def user(name : String) = Action {
    Ok(s"You query about: $name.")
  }
}
