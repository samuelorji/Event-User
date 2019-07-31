package com.soundcloud.maze.core.config

import com.soundcloud.maze.core.util.ConfigT

object MazeConfig extends MazeConfigT

/**
  * Contains configurations that are shared across The whole project */
private[config] trait MazeConfigT extends ConfigT {

  val mazeEventsHost = config.getString("maze.interface.web.events.host")
  val mazeUsersHost = config.getString("maze.interface.web.user.host")

  val mazeEventPort = config.getInt("maze.interface.web.events.port")
  val mazeUsersPort = config.getInt("maze.interface.web.user.port")

}
