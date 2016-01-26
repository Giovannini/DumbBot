package bots

import commands.UserCreated
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.IncomingMessageListener
import io.scalac.slack.common.{BaseMessage, Command, UndefinedMessage}
import spray.json._


class RecognizerBot(override val bus: MessageEventBus) extends IncomingMessageListener {

  def receive: Receive = {
    case bm@BaseMessage(text, channel, user, dateTime, edited) if isCommand(text)=>
      changeIntoCommand(text, bm)
    case um@UndefinedMessage(s) =>
      val json = s.parseJson.asJsObject
      if(UserCreated.isUserCreated(json)) UserCreated.changeIntoUserCreated(json).foreach(publish)
  }

  //Command
  val commandPattern = "$"
  def isCommand(text: String): Boolean = text.trim.startsWith(commandPattern)
  def changeIntoCommand(text: String, baseMessage: BaseMessage) = {
    val tokenized = text.trim.drop(commandPattern.length).trim.split("\\s")
    publish(Command(tokenized.head, tokenized.tail.toList.filter(_.nonEmpty), baseMessage))
  }

}
