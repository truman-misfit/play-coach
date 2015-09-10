package controllers

import play.api._
import play.api.mvc._

import data_model._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import play.api.Play.current
import play.api.i18n._
import play.api.i18n.Messages.Implicits._


import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax


import actions._
class JsonController extends Controller {
    def getJson = LoggingAction{
    
    }
    
    def recvJson = LoggingAction{ request =>
       
    }
}