import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{BaseMessage, OutboundMessage}

class DumbBot(override val bus: MessageEventBus) extends AbstractBot {

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name has no effect yet...")

  override def act: Receive = {
    case bm@BaseMessage(text, channel, user, dateTime, edited) =>
      val response = OutboundMessage(channel, s"Are you talking to me $user?")
      publish(response)
  }
}