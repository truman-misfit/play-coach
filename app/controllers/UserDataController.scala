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

import actions._
class UserDataController extends Controller {

//init a user data form ,to accept data from AddUser.html    
  val userDataForm: Form[NameRecord] = Form{

  }

//use default Action
  def getUsers = Action{
    //@TODO
  }
  
//use LoggingAction
  def getUser(name:Option[String]) = LoggingAction{
    //@TODO 
  }
  
  def addUserPage = LoggingAction{
    //use userDataForm to init AddUser.html
    
  }
  
  def addUser = LoggingAction{
     
  }
}