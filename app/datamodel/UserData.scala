package datamodel
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
	var userIndex:List[String] = List("Tom")

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
		val savedData = data.get(user.name)
		savedData match {
			case Some(old) => //do nothing
			case None => userIndex = userIndex ::: List(user.name)
		}
		data = data + (user.name -> user)
	}

	val recordInOnePage = 10

	def getUserAtPage(pageNum:Int) = 
	{
		if(userIndex.size / recordInOnePage <= pageNum - 1 ||
			pageNum < 0)
			throw new IllegalArgumentException
		
		val beginIndex = pageNum * recordInOnePage
		val endIndex = {
			if((pageNum + 1) * recordInOnePage > userIndex.size)
				userIndex.size
			else
				(pageNum + 1)* recordInOnePage
		}

		var ret = ""
		for(index <- beginIndex until endIndex){
			val userData = data.get(userIndex(index)).get
			ret += userData.name + ",age:"+ userData.age + ",tel:" + userData.tel + ",mail:" + userData.mail + ",gender:" + userData.gender + "\n"
		}
		ret
	}
}


