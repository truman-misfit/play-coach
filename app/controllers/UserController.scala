package controllers

import play.api._
import play.api.mvc._

import datamodel._

class UserController extends Controller {

  def getUser(name : String) = Action {
    Ok(s"You query about: $name.")
  }

	def getAllUsers = Action{
		Ok(UserData.toString)
	}
}
