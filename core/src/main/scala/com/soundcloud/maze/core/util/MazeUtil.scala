package com.soundcloud.maze.core.util

import scala.util.Try

object MazeUtil {
  //for all utility functions that may be used all through the project
  def parseInt(number: String) = Try(number.toInt).toOption

}
