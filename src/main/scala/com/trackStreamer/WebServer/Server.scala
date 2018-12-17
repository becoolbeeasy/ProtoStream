package com.trackStreamer.WebServer

import java.util.logging.Logger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.trackStreamer.Controllers.TrackController

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


object Server extends TrackController {
  implicit val system: ActorSystem = ActorSystem("TrackStreamer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val logger: Logger = Logger.getLogger(this.getClass.getSimpleName)


  def main (args: Array[String] ): Unit = {

    if (args.length == 1) {

      val bindingFuture = Http().bindAndHandle(route(args(0)), "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    } else{
      println("Missing Music Directory")
      System.exit(-1)
    }
  }
}
