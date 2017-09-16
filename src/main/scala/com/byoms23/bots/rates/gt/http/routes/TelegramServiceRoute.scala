package com.byoms23.bots.rates.gt.http.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import com.byoms23.bots.rates.gt.lib.TelegramService
import spray.json.DefaultJsonProtocol

// domain model
final case class Item(name: String, id: Long)
final case class Order(items: List[Item])


// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order) // contains List[Item]
}

class TelegramServiceRoute(val telegramService: TelegramService) extends Directives with JsonSupport {

  val route = pathPrefix("bots" / Segment) { botToken =>
    path("providers" / "telegram" / Segment) { botSecret =>
      pathEndOrSingleSlash {
        get {
//          entity(as[LoginPassword]) { loginPassword =>
//            complete(signIn(loginPassword.login, loginPassword.password).map(_.asJson))
//          }
          complete(s"OK, botToken:$botToken botSecret:$botSecret ")
        }
      }
    }
  }


}
