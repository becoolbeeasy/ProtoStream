package com.trackStreamer.Data

import java.io.File

class FileHandler(musicDirLocation: String) {

  def getTrackFile(trackName: String): File = {
    //Perform checks
    new File(musicDirLocation + trackName)
  }

  def getAvaliableTracks: Array[String] = {
    val dir = new File(musicDirLocation)
    if (dir.exists && dir.isDirectory){
      dir.listFiles.map(_.getName)
    }else{
      throw new Exception(s"$musicDirLocation is not a valid directory")
    }
  }
}

object FileHandler {
  def apply(musicDirLocation: String): FileHandler = new FileHandler(musicDirLocation)
}
