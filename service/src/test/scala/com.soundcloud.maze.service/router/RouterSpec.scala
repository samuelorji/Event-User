package com.soundcloud.maze
package service.router

import core.action.events.Events.{ Follow, UnFollow }

import service.TestServiceT
import service.registry.FollowerRegistry

class RouterSpec extends TestServiceT {
  val mockTo   = 35
  val mockFrom = 76

  val mockSeqId  = 234
  val testRouter = system.execute(Router.getRouterInstance)

  "The Router actor " should "add to followers Map when a follow message is sent " in {

    val followMsg  = s"$mockSeqId|F|$mockFrom|$mockTo"

    testRouter ! Follow(mockSeqId,mockFrom,mockTo,followMsg)

    eventually{
      assert(FollowerRegistry.getFollow(mockTo).contains(Set(mockFrom))) //the user `from` who followed `to` must be added to the follower map
    }
  }

  it should "Remove a follower from the follower map when an Unfollow is sent " in {
    FollowerRegistry.addFollow(mockTo,mockFrom) //initially add to the follower map

    val unfollowMsg = s"$mockSeqId|U|$mockFrom|$mockTo"

    testRouter ! UnFollow(mockSeqId,mockFrom,mockTo, unfollowMsg)

    eventually{
      assert(FollowerRegistry.getFollow(mockTo).isEmpty)
    }
  }

}
