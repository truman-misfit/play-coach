package data_model

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

case class UserData(name:String, age:Int, tel:String, mail:String, gender:String)
{
	def isNameValidated:Boolean = (name.size != 0)

	def isAgeValidated:Boolean = (age > 0 && age <= 140)

	def isMailValidated:Boolean = 
	{
		var atNum = 0
		for(i <- 0 until mail.length if mail.charAt(i) == '@'){
			atNum = atNum+1
		}
		if(atNum != 1) {
			false
		}
		else
		{
			true
		}
	}

	def isTelValidated:Boolean = 
	{
		if(tel.size == 0){
			false
		}
		else
		{
			var ret = true 
			for(i <- 0 until tel.length)
			{
				if(false == tel.charAt(i).isDigit)ret = false
			}
			ret
		}

	}

	def isGenderValidated:Boolean = 
	{
		gender.toLowerCase match{
			case "female" => true
			case "male" => true
			case _ => false
		}
	}

	def validation:Boolean = 
	{
	  isNameValidated && isAgeValidated && isTelValidated && isMailValidated && isGenderValidated
	}

	require(validation)
}

object UserData{
	var data:Map[String,UserData] = Map("Tom" -> UserData("Tom",10,"13245","Tom@123.com","male"))

	override def toString = {
		var ret = ""
	  data.foreach(user => ret = ret + "Name:" + user._1 + ",gender:" + user._2.gender + ",tel:" + user._2.tel + ",mail:" + user._2.mail + "\n")
		ret
	}
}


