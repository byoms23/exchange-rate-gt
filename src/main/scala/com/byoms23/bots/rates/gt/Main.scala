package com.byoms23.bots.rates.gt

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString
import com.byoms23.bots.rates.gt.http.HttpService
import com.byoms23.bots.rates.gt.lib.TelegramService

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Random

object Main {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

//    // streams are re-usable so we can define it here
//    // and use it for every request
//    val numbers = Source.fromIterator(() =>
//      Iterator.continually(Random.nextInt()))

//    val route =
//      path("random") {
//        get {
//          complete(
//            HttpEntity(
//              ContentTypes.`text/plain(UTF-8)`,
//              // transform each number to a chunk of bytes
//                numbers.map(n => ByteString(s"$n\n"))
//            )
//          )
//        }
//      }
//
//    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
//    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
//    StdIn.readLine() // let it run until user presses return
//    bindingFuture
//      .flatMap(_.unbind()) // trigger unbinding from the port


    val telegramService = system.actorOf(TelegramService.props(), "telegramService")

    val httpService = new HttpService(telegramService)

    val bindingFuture = Http().bindAndHandle(httpService.routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}
