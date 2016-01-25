import akka.actor.{ActorContext, ActorRef, ActorSystem, Props}
import bots.{DumbBot, RecognizerBot}
import io.scalac.slack.api.{BotInfo, Start}
import io.scalac.slack.common.Shutdownable
import io.scalac.slack.common.actors.SlackBotActor
import io.scalac.slack.websockets.WebSocket
import io.scalac.slack.{BotModules, MessageEventBus}


object BotRunner extends Shutdownable {
  val system = ActorSystem("SlackBotSystem")
  val eventBus = new MessageEventBus
  val slackBot = system.actorOf(Props(classOf[SlackBotActor], new ExampleBotsBundle(), eventBus, this, None), "slack-bot")
  var botInfo: Option[BotInfo] = None

  def main(args: Array[String]) {
    try {
      slackBot ! Start

      system.awaitTermination()
      println("Shutdown successful...")
    } catch {
      case e: Exception =>
        println("An unhandled exception occurred...", e)
        system.shutdown()
        system.awaitTermination()
    }
  }

  sys.addShutdownHook(shutdown())

  override def shutdown(): Unit = {
    slackBot ! WebSocket.Release
    system.shutdown()
    system.awaitTermination()
  }

  class ExampleBotsBundle() extends BotModules {
    override def registerModules(context: ActorContext, websocketClient: ActorRef) = {
      context.actorOf(Props(classOf[RecognizerBot], eventBus), "recognizerBot")
      context.actorOf(Props(classOf[DumbBot], eventBus), "DumbBot")
    }
  }
}

