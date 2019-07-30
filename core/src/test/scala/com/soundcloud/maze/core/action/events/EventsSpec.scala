package com.soundcloud.maze
package core.action.events

import core.TestServiceT
import core.action.events.Events._

class EventsSpec extends TestServiceT {
  val mockTo    = 23
  val mockFrom  = 34
  val mockSeqId = 983
  val mockInvalidMsg   = s"$mockSeqId|M|$mockFrom|$mockTo"

  val mockFollow   = s"$mockSeqId|F|$mockFrom|$mockTo"
  val mockUnFollow = s"$mockSeqId|U|$mockFrom|$mockTo"

  val mockPrivateMessage = s"$mockSeqId|P|$mockFrom|$mockTo"
  val mockBroadcast      = s"$mockSeqId|B"
  val mockStatusUpdate   = s"$mockSeqId|S|$mockFrom"

  "The Events Object " should "Properly parse messages " in {
    Events.parseEvent(mockFollow) should be(Some(Follow(mockSeqId,mockFrom,mockTo,mockFollow)))
    Events.parseEvent(mockUnFollow) should be(Some(UnFollow(mockSeqId,mockFrom,mockTo,mockUnFollow)))
    Events.parseEvent(mockBroadcast) should be(Some(Broadcast(mockSeqId,mockBroadcast)))
    Events.parseEvent(mockPrivateMessage) should be(Some(PrivateMessage(mockSeqId,mockFrom,mockTo,mockPrivateMessage)))
    Events.parseEvent(mockStatusUpdate) should be(Some(StatusUpdate(mockSeqId,mockFrom,mockStatusUpdate)))
    Events.parseEvent(mockInvalidMsg) should be(None)
  }

}
