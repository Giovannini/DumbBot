package linkedin

import java.io.PrintWriter

import scala.io.StdIn
import scalaj.http.Http
import spray.json._

/**
  * Created by tgi on 03/02/2016.
  */
object Api {

  val clientId = "77708llwdywl7d"
  val clientSecret = "ELnrWBW32yaxncZf"
  val redirectUri = "http://example.com/result"

  private def generateUrl(url: String, params: Map[String, String]) = {
    val ok = params.map { case (k, v) => s"$k=${v.replace(" ", "%20")}"}.mkString("&")
    s"$url?$ok"
  }

  private def requestAuthorizationCode: String = {
    val url = "https://www.linkedin.com/uas/oauth2/authorization"
    val scope = "r_basicprofile r_emailaddress" //TODO: maybe add scopes if some rights are not granted
    val params = Map(
      "response_type" -> "code",
      "client_id" -> clientId,
      "redirect_uri" -> redirectUri,
      "scope" -> scope,
      "state" -> "badaboumstate"
    )

    println("\n###################################")
    println(s"Go to : ${generateUrl(url, params)}")
    println("###################################\n")

    val verifier = StdIn.readLine("Enter verifier: ").trim

    verifier
  }

  private def exchangeAuthorizationCodeForAccessToken(code: String) = {
    val url = "https://www.linkedin.com/uas/oauth2/accessToken"

    val request = Http(url).postForm(Seq(
      "grant_type" -> "authorization_code",
      "code" -> code,
      "redirect_uri" -> redirectUri,
      "client_id" -> clientId,
      "client_secret" -> clientSecret
    )).asString
    request.body.parseJson.asJsObject.getFields("access_token") match {
      case Seq(JsString(token)) => new PrintWriter("src/main/resources/token") { write(token); close() }
      case other => println(other)
    }
  }

  def goOAuth = {
    val code = requestAuthorizationCode
    exchangeAuthorizationCodeForAccessToken(code)
  }
}
