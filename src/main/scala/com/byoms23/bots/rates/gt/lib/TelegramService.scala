package com.byoms23.bots.rates.gt.lib

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import com.byoms23.bots.rates.gt.Config
import spray.json.DefaultJsonProtocol


// domain model
final case class Item(name: String, id: Long)
final case class Order(items: List[Item])


// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order) // contains List[Item]
}


class TelegramService {
//  def getBotInfo(): Future[HttpResponse] =
//    Http().singleRequest(HttpRequest(uri = s"https://api.telegram.org/bot391874379:AAFI4TlfUeaUXz9X-loix-iv37uvnMmqjC0/getMe", method = POST))

}
