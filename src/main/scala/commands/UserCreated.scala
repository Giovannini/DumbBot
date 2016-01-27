package commands

import io.scalac.slack.common.IncomingMessage
import models.User
import spray.json._

case class UserCreated(user: User) extends IncomingMessage

case class Profile(name: String, firstName: String, lastName: String, email: String)

object UserCreated {
  def isUserCreated(json: JsObject): Boolean = json.fields.get("type").contains(JsString("team_join"))

  def changeIntoUserCreated(json: JsObject): Option[UserCreated] = {
    val maybeUser = json.fields.get("user")
    maybeUser.flatMap(createUserCreatedMessage)
  }

  private def createProfile(profile: JsValue): Option[Profile] = {
    profile.asJsObject.getFields("name", "first_name", "last_name", "email") match {
      case Seq(JsString(name), JsString(firstName), JsString(lastName), JsString(email)) =>
        Some(Profile(name = name, firstName = firstName, lastName = lastName, email = email))
      case _ => None
    }
  }

  private def createUserCreatedMessage(user: JsValue): Option[UserCreated] = {
    user.asJsObject.getFields("id", "team_id", "profile") match {
      case Seq(JsString(id), JsString(teamId), profile: JsValue) =>
        createProfile(profile)
          .map { case Profile(name, firstName, lastName, email) =>
            val user = User(slackId = id, name = name, firstName = firstName, lastName = lastName, email = email)
            UserCreated(user)
          }
      case _ => None
    }
  }
}