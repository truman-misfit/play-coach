package controllers

import play.api.mvc._
import play.api.libs.json._

class SecondController extends Controller{
  def hello = Action { request =>
    Ok("hello")
  }

}