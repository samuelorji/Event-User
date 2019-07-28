package com.soundcloud.maze.core.util

import com.typesafe.config.ConfigFactory

trait ConfigT {
  val config = ConfigFactory.load()

}
