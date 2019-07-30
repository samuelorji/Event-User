package com.soundcloud.maze
package core.config

import core.TestServiceT

class MazeConfigSpec extends TestServiceT {

  MazeConfig.mazeEventPort should be(9090)
  MazeConfig.mazeUsersPort should be(9099)

  MazeConfig.mazeEventsHost should be("localhost")
  MazeConfig.mazeUsersHost should be("localhost")
}
