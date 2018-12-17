package com.trackStreamer.Controllers

import java.io.File
import java.util.logging.Logger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.FileIO
import com.trackStreamer.Data.FileHandler
import spray.json._

trait TrackController extends SprayJsonSupport with DefaultJsonProtocol {
  private val logger = Logger.getLogger(this.getClass.getSimpleName)
  var fileHandler: FileHandler = _

  val route = (tracksDir: String) =>
    path("track" / Segment) { name =>
      get {
        logger.info(s"GET Request Received for track [$name]")

        fileHandler = FileHandler(tracksDir)
        val trackFile = fileHandler.getTrackFile(name)
        complete(buildHttpResponseForTrack(trackFile))
      }
    } ~
      pathPrefix("getTracks") {
        pathEndOrSingleSlash {
          get {

            fileHandler = FileHandler(tracksDir)
            val trackList = fileHandler.getAvaliableTracks.sorted.mkString("\n")
            complete(trackList)
          }
        }
      }

  private def buildHttpResponseForTrack(track: File, extraHeaders: collection.immutable.Seq[RawHeader] = collection.immutable.Seq()) = {
    val contentType = ContentType(MediaTypes.`audio/mpeg`)
    val fileStream = FileIO.fromPath(track.toPath)
    val rangeHeader: scala.collection.immutable.Seq[HttpHeader] = scala.collection.immutable.Seq(RawHeader("Accept-Ranges", "bytes"))
    val headers = rangeHeader ++ extraHeaders
    val entity = HttpEntity(contentType, track.length, fileStream)

    HttpResponse(StatusCodes.OK, headers = headers, entity = entity)
  }

}
