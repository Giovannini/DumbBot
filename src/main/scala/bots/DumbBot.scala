package bots

import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{BaseMessage, Command, OutboundMessage, UndefinedMessage}



class DumbBot(override val bus: MessageEventBus) extends AbstractBot {

  val id = "U0K07KRPY"

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name has no effect yet...")

  val Mention = """<@([A-Z0-9]+)>""".r

  //Command's names

  override def act: Receive = {
    case Test => println("YOOOOOOOOOOOOO")
    case Command("skill", params, bm) =>
      handleSkillCommand(params, bm)
    case bm@BaseMessage(text, channel, user, dateTime, edited) if text startsWith  s"<@$id>" =>
      val args = text.substring(s"<@$id> ".length).split(" ")
      val response = args.headOption.fold(
        OutboundMessage(channel, s"Why are you talking to me privatly $user?")
      )(subject => OutboundMessage(channel, s"So you want to know about '$subject', $user?"))
      publish(response)
    case um@UndefinedMessage(s) =>
    //TODO deal with created channel message
    // {"type":"channel_created","channel":{"id":"C0KA9K31D","is_channel":true,"name":"tuttu","created":1453726060,"creator":"U0JTSDAAH"},"event_ts":"1453726060.154318"}
    //TODO deal with deleted channel message
    // {"type":"channel_deleted","channel":"C0KA9K31D","event_ts":"1453726789.173064"}
    //TODO handle created users
  }

  def handleSkillCommand(params: List[String], bm: BaseMessage) = params match{
    case Nil =>
      publish(OutboundMessage(bm.channel, s"Please tell me what skill you want to know about"))
    case Mention(user)::tail =>
      publish(OutboundMessage(bm.channel, s"You want to know $user's skills."))
    case skill::tail => publish(OutboundMessage(bm.channel, s"You want to know about skill '$skill' in the group."))

  }
}