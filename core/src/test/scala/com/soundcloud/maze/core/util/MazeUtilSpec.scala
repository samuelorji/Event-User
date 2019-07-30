package com.soundcloud.maze
package core.util

import core.TestServiceT

class MazeUtilSpec extends TestServiceT {
  "The maze util's functions  " should "behave as expected " in {
    MazeUtil.parseInt("34") should be(Some(34))
    MazeUtil.parseInt("3r") should be(None)
  }

}
