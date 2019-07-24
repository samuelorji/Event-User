package com.soundcloud.maze.util

import com.typesafe.config.ConfigFactory

trait ConfigT {
  val config = ConfigFactory.load()

}
