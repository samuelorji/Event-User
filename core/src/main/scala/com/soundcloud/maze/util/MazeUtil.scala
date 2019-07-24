package com.soundcloud.maze.util

import scala.concurrent.duration.{Duration, FiniteDuration}

object MazeUtil {

  def parseFiniteDuration(str: String) : Option[FiniteDuration] =
    try {
      Some(Duration(str)).collect { case d: FiniteDuration => d }
    } catch {
      case _: NumberFormatException => None
    }
}
