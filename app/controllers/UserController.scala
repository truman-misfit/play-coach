package controllers

import play.api._
import play.api.mvc._

import data_model._

class UserController extends Controller {

  def user(name : String) = Action {
    Ok(s"You query about: $name.")
  }

	def getAllUsers = Action{
		Ok(UserData.toString)	
	}
}
