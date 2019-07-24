package com.soundcloud.maze.util

import com.soundcloud.maze.action.users.User
import com.soundcloud.maze.action.events._

object EventParser extends EventParserT
private[util] trait EventParserT {
  def parse(msg : String) : List[Event] = {
    try {
      val events = msg.split("\n")
     // println(events.mkString("***"))
      events.map { event =>
        val eventParts = event.split(s"""\\|""")
        eventParts(1) match {

          case "F" =>
            Follow(eventParts(2), eventParts(3))
          case "B" =>
            Broadcast

          case "U" =>
            UnFollow(eventParts(2), eventParts(3))

          case "P" =>
            PrivateMessage(eventParts(2), eventParts(3))

          case "S" =>
            StatusUpdate(eventParts(2))


          case _ => InvalidEvent
        }
      }.toList
    } catch {
      case _: ArrayIndexOutOfBoundsException =>
        List(InvalidEvent)
    }
  }

}

private[util] trait UserParserT{

  def parse(msg : String) : User ={

    User("hello")
  }
}
