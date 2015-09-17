package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

import data_model._

class UserController extends Controller {

  def user(name : String) = Action {
    Ok(s"You query about: $name.")
  }

	def getAllUsers = Action{
		Ok(UserData.toString)
	}

	def addUser = Action {request =>
		try{
			val body = request.body
			val jsonValue = body.asJson
			jsonValue match{
				case Some(jsonResult)  =>{
					val jsonReadResult = jsonResult.validate[UserData]
					jsonReadResult match{
						case success:JsSuccess[UserData] => {
							  UserData.addUser(success.get)
							  Ok("Add a new user:" + success.get.name)
							}
						case e:JsError => BadRequest("Illegal argument!")

					}
				 }
				case None => BadRequest("Illegal argument!")
			}
		}
		catch{
			case e:IllegalArgumentException => BadRequest("Illegal argument!")
		}

	}
}
