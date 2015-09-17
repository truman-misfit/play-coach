package data_model

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

case class UserData(name:String, age:Int, tel:String, mail:String, gender:String)
{
	def nameValidated:Boolean = (name.size != 0)

	def ageValidated:Boolean = (age > 0 && age <= 140)

	def mailValidated:Boolean =
	{
		val mailPattern = """(^\S+@[^@]+$)""".r
		mail match{
			case mailPattern(_)	 => true
			case _ => false
		}
	}

	def telValidated:Boolean =
	{
		val telPattern = """(^\d+$)""".r
		tel match{
			case telPattern(_) => true
			case _ => false
		}
	}

	def genderValidated:Boolean =
	{
		gender.toLowerCase match{
			case "female" => true
			case "male" => true
			case _ => false
		}
	}

	def validation:Boolean =
	{
	  nameValidated && ageValidated && telValidated && mailValidated && genderValidated
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
  //from JSON to UserData
	implicit val userReads: Reads[UserData] = (
		(JsPath \ "name").read[String](minLength[String](2)) and
		(JsPath \ "age").read[Int](min(1) keepAnd max(140)) and
		(JsPath \ "tel").read[String](pattern("""(^\d+$)""".r)) and
		(JsPath \ "mail").read[String](pattern("""(^\S+@[^@]+$)""".r)) and
		(JsPath \ "gender").read[String](pattern("""(?i)(^(fe)?male$)""".r))
	)(UserData.apply _)

  //from UserData to JSON value
	implicit val userWrites: Writes[UserData] = (
		(JsPath \ "name").write[String] and
		(JsPath \ "age").write[Int] and
		(JsPath \ "tel").write[String] and
		(JsPath \ "mail").write[String] and
		(JsPath \ "gender").write[String]
	)(unlift(UserData.unapply))

	def addUser(user:UserData){
		data = data + (user.name -> user)
	}
}
