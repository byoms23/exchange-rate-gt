package com.byoms23.bots.rates.gt.lib

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import spray.json._

import scala.concurrent.Future


// domain model
case class Health(status: String, description: String)
case object GetHealthRequest
case class HealthResponse(health: Health)

final case class TelegramResponse[T](ok: Boolean, result: T)
final case class User(id: Long, is_bot: Boolean, first_name: String, username: String)
//{"ok":true,"result":{"id":391874379,"is_bot":true,"first_name":"exchange-rate-gt","username":"ExchangeRateGTBot"}}

// collect your json format instances into a support trait:
trait TelegramJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User)
  implicit val getMeFormat: RootJsonFormat[TelegramResponse[User]] = jsonFormat2(TelegramResponse[User])
}

object TelegramService {
  def props(): Props = {
    Props(classOf[TelegramService])
  }
}

class TelegramService extends Actor with ActorLogging with TelegramJsonSupport {

  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  def receive: Receive = {
    case GetHealthRequest =>
      log.debug("Received GetHealthRequest")
//      sender() ! HealthResponse(Health("Meeeh!", "Really ok."))//getBotInfo()
      val currentSender = sender
      getBotInfo.foreach { currentSender ! _ }
//      getBotInfo.foreach {
//        case Some(user) => println(user); sender() ! user
//        case _ => sender() ! "Nope"
//      }
  }

  def getBotInfo: Future[Option[User]] =
    http.singleRequest(HttpRequest(uri = s"https://api.telegram.org/bot391874379:AAFI4TlfUeaUXz9X-loix-iv37uvnMmqjC0/getMe", method = POST))
      .flatMap {
        case HttpResponse(StatusCodes.OK, headers, entity, _) => {
          Unmarshal(entity).to[TelegramResponse[User]].map {
            case TelegramResponse(true, result) => Some(result)
            case _ => None
          }
        }
        case resp @ HttpResponse(code, _, _, _) => {
          log.info("Request failed, response code: " + code)
          resp.discardEntityBytes()

          Future.successful(None)
        }
      }

}
