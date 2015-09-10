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
  val userDataForm: Form[NameRecord] = Form(
    mapping(
      "Name" -> nonEmptyText,                       //name can not be empty
      "Age" -> number.verifying(min(0), max(140)),   //age is between 0 and 140
    )(NameRecord.apply)(NameRecord.unapply)
  )

//use default Action
  def getUsers = Action{
    var ret = "Registered user list:\n"
    
    val nameList = NameRecord.nameRecordList
    nameList foreach(record => {
        ret += "Name:" + record.name + ",age:" + record.age + "\n"
    })
    Ok(ret)
  }
  
//use LoggingAction
  def getUser(name:Option[String]) = LoggingAction{
    var ret = "No user name specialfied"
      name foreach({ nameInOption =>
        ret = 
        try
        {
          NameRecord.getUser(nameInOption)
        }
        catch
        {
          //NameRecord.getUser throw a IllegalArgumentException if there is no such user
          case ex: IllegalArgumentException => "Unknow user name"
        }
    })
    Ok(ret)
  }
  
  def addUserPage = LoggingAction{
    //use userDataForm to init AddUser.html
    Ok(views.html.AddUser(userDataForm))
  }
  
  def addUser = LoggingAction{implicit request=>
    //get data from userDataForm
      userDataForm.bindFromRequest.fold(
      //if error
      formWithErrors => Ok("commit error"),
      //if all goes well
      {
        myData => {
            NameRecord.addName(myData.name,myData.age,myData.mail,myData.tel,myData.gender)
            Ok("receive a user,name:" + myData.name + " age:" + myData.age)
        }
      }
    ) 
  }
}