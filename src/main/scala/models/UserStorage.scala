package models

import spray.json._


case class UserStorage(val users: List[User]) {
  def find(id: String): Option[User] = users.find(_.slackId == id)

  def findAll(): List[User] = users

  def save(m: User): UserStorage = copy(users = m :: users)
}

object UserStorage {

  def load: UserStorage = {
    val lines = scala.io.Source.fromFile("scripts/users.json").mkString
    val users = lines.parseJson.asJsObject.getFields("users") match {
      case Seq(JsArray(elements)) => elements.toList.flatMap(parseUser)
      case _ => Nil
    }
    UserStorage(users)
  }

  private def parseUser(jsValue: JsValue): Option[User] = {
    jsValue.asJsObject.getFields("name", "slackId", "first_name", "last_name", "email") match {
      case Seq(JsString(name), JsString(slackId), JsString(firstName), JsString(lastName), JsString(email)) =>
        Some(User(name, slackId, firstName, lastName, email))
      case Seq(JsString(name), JsString(slackId), JsString(firstName), JsString(lastName)) =>
        Some(User(name, slackId, firstName, lastName, ""))
      case _ => None
    }
  }

}
