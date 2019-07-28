package com.soundcloud.maze.config

import com.soundcloud.maze.util.{ConfigT, MazeUtil}

object MazeConfig extends MazeConfigT
private[config] trait MazeConfigT extends ConfigT {

  val mazeEventsHost = config.getString("maze.interface.web.events.host")
  val mazeUsersHost  = config.getString("maze.interface.web.user.host")

  val mazeEventPort  = config.getInt("maze.interface.web.events.port")
  val mazeUsersPort  = config.getInt("maze.interface.web.user.port")

 // val socketsTimeout = MazeUtil.parseFiniteDuration(config.getString("maze.web.sockets-timeout")).get
}
