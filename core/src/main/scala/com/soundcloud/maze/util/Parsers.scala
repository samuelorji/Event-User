package com.soundcloud.maze.util

import com.soundcloud.maze.action.users.User
import com.soundcloud.maze.action.events._

object EventParser extends EventParserT {
  def apply(msg: String): Option[Event] = msg.split('|') match {
    case Array(AsInt(seq), "F", AsInt(from), AsInt(to)) => Some(Follow(seq, from, to, msg))
    case Array(AsInt(seq), "U", AsInt(from), AsInt(to)) => Some(UnFollow(seq, from, to, msg))
    case Array(AsInt(seq), "B") => Some(Broadcast(seq, msg))
    case Array(AsInt(seq), "P", AsInt(from), AsInt(to)) => Some(PrivateMessage(seq, from, to, msg))
    case Array(AsInt(seq), "S", AsInt(from)) => Some(StatusUpdate(seq, from, msg))
    case _ => None
  }
  object AsInt {
    def unapply(str: String): Option[Int] = try {
      Some(str.toInt)
    } catch {
      case _: NumberFormatException => None
    }
  }
}
private[util] trait EventParserT {
  def parse(event: String): Option[Event] = {
    try {

      val eventParts = event.split('|')
      eventParts(1) match {

        case "F" =>
          Some(Follow(eventParts(0).toInt, eventParts(2).toInt, eventParts(3).toInt, event))
        case "B" =>
          Some(Broadcast(eventParts(0).toInt, event))

        case "U" =>
          Some(UnFollow(eventParts(0).toInt, eventParts(2).toInt, eventParts(3).toInt, event))

        case "P" =>
          Some(PrivateMessage(eventParts(0).toInt, eventParts(2).toInt, eventParts(3).toInt, event))

        case "S" =>
          Some(StatusUpdate(eventParts(0).toInt, eventParts(2).toInt, event))


        case _ => None
      }
    } catch {
      case _: ArrayIndexOutOfBoundsException =>
        None
    }

  }

 // def parseNew(event : String) : Option[Event] =
}

//object UserParser extends UserParserT
//private[util] trait UserParserT {
//  def parse(msg: String): User = {
//    User(msg)
//  }
//}


