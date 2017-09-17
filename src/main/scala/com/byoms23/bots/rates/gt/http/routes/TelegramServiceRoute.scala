package com.byoms23.bots.rates.gt.http.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.pattern.ask
import akka.util.Timeout
import com.byoms23.bots.rates.gt.lib._

import scala.concurrent.duration._


class TelegramServiceRoute(val telegramService: ActorRef) extends Directives with TelegramJsonSupport {

  implicit val timeout = Timeout(5.seconds)

  val route = pathPrefix("bots" / Segment) { botToken =>
    path("providers" / "telegram" / Segment) { botSecret =>
      pathEndOrSingleSlash {
        get {
//          entity(as[LoginPassword]) { loginPassword =>
//            complete(signIn(loginPassword.login, loginPassword.password).map(_.asJson))
//          }
          onSuccess(telegramService ? GetHealthRequest) {
            case response: HealthResponse =>
              complete(StatusCodes.OK, s"Everything is ${response.health.status}!")
            case Some(user: User) =>
              complete(user)
            case _ =>
              complete(StatusCodes.InternalServerError)
          }
//          complete(s"OK, botToken:$botToken botSecret:$botSecret ")
        }
      }
    }
  }


}
