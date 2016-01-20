import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{BaseMessage, OutboundMessage}

class DumbBot(override val bus: MessageEventBus) extends AbstractBot {

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name will help you to solve difficult math problems \\n" +
        "Usage: $calc {operation} {arguments separated by space}")

  val possibleOperations = Map(
    "+" -> ((x: Double, y: Double) => x+y),
    "-" -> ((x: Double, y: Double) => x-y),
    "*" -> ((x: Double, y: Double) => x*y),
    "/" -> ((x: Double, y: Double) => x/y)
  )

  override def act: Receive = {
    case bm@BaseMessage(text, channel, user, dateTime, edited) =>
      val response = OutboundMessage(channel, s"Are you talking to me?")
      publish(response)
  }
}