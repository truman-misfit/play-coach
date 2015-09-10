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
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import actions._

class AsyncUserDataController extends Controller {
    
    def getUserData = Action.async{
        
    }
    
}