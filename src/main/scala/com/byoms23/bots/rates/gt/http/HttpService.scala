package com.byoms23.bots.rates.gt.http

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives
import com.byoms23.bots.rates.gt.http.routes.TelegramServiceRoute
import com.byoms23.bots.rates.gt.lib.TelegramService

import scala.concurrent.ExecutionContext


class HttpService(val telegramService: ActorRef)(implicit executionContext: ExecutionContext) extends Directives {

  val telegramRouter = new TelegramServiceRoute(telegramService)

  val routes =
    pathPrefix("v1") {
      pathEndOrSingleSlash {
        get {
            complete("v1")
        }
      } ~
      telegramRouter.route
    }
}
