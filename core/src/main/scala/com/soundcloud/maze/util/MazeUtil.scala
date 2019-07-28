package com.soundcloud.maze.util

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.Try

object MazeUtil {

  def parseFiniteDuration(str: String) : Option[FiniteDuration] =
    try {
      Some(Duration(str)).collect { case d: FiniteDuration => d }
    } catch {
      case _: NumberFormatException => None
    }

  def parseInt(number : String) = Try(number.toInt).toOption

}
