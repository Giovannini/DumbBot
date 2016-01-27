package bots

import commands.UserCreated
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{BaseMessage, Command, OutboundMessage, UndefinedMessage}
import models.{User, UserStorage}

class DumbBot(override val bus: MessageEventBus, _userStorage: UserStorage) extends AbstractBot {

  val id = "U0K07KRPY"
  var storage = _userStorage //TODO find a cleaner way to do that

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name has no effect yet...")

  val Mention = """<@([A-Z0-9]+)>""".r

  //Command's names

  override def act: Receive = {
    case Command("skill", params, bm) =>
      handleSkillCommand(params, bm)
    case UserCreated(user) => save(user) //TODO handle UserDeleted
    case bm@BaseMessage(text, channel, user, dateTime, edited) if text startsWith  s"<@$id>" =>
      val args = text.substring(s"<@$id>".length).trim.split(" ")
      val response = args.headOption.fold(
        OutboundMessage(channel, s"Why are you talking to me privatly ${ getUser(user) }?")
      )(subject => OutboundMessage(channel, s"So you want to know about '$subject', ${ getUser(user) }?"))
      publish(response)
    case um@UndefinedMessage(s) =>
    //TODO deal with created channel message
    // {"type":"channel_created","channel":{"id":"C0KA9K31D","is_channel":true,"name":"tuttu","created":1453726060,"creator":"U0JTSDAAH"},"event_ts":"1453726060.154318"}
    //TODO deal with deleted channel message
    // {"type":"channel_deleted","channel":"C0KA9K31D","event_ts":"1453726789.173064"}
    //TODO handle created users
  }

  private def getUser(user: String) = storage.find(user).map(_.firstName).getOrElse(user)
  private def save(user: User) = {
    storage = storage.save(user)
  }

  def handleSkillCommand(params: List[String], bm: BaseMessage) = params match{
    case Nil =>
      publish(OutboundMessage(bm.channel, s"Please tell me what skill you want to know about"))
    case Mention(user)::tail =>
      publish(OutboundMessage(bm.channel, s"You want to know ${ getUser(user) }'s skills."))
    case skill::tail => publish(OutboundMessage(bm.channel, s"You want to know about skill '$skill' in the group."))
  }
}