package controllers

import play.api.mvc._
class SecondController extends Controller{
    def hello = Action { request =>
        Ok("hello")
    }
}