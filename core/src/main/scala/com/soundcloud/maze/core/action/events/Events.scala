package com.soundcloud.maze.core.action.events

import com.soundcloud.maze.core.util.MazeUtil.parseInt

object Events {

  val eventOrdering = Ordering.fromLessThan[Event](_.seqId > _.seqId)

  sealed trait Event {
    val seqId: Int
  }

  case class Follow(seqId: Int, from: Int, to: Int, payload: String) extends Event

  case class UnFollow(seqId: Int, from: Int, to: Int, payload: String) extends Event

  case class Broadcast(seqId: Int, payload: String) extends Event

  case class PrivateMessage(seqId: Int, from: Int, to: Int, payload: String) extends Event

  case class StatusUpdate(seqId: Int, from: Int, payload: String) extends Event

  def parseEvent(msg: String): Option[Event] = {
    val entries = msg.split('|')
    try {
      entries(1) match {
        case "F" =>
          Some(Follow(entries(0).toInt, parseInt(entries(2)).get, parseInt(entries(3)).get, msg))
        case "B" =>
          Some(Broadcast(entries(0).toInt, msg))

        case "U" =>
          Some(UnFollow(entries(0).toInt, parseInt(entries(2)).get, parseInt(entries(3)).get, msg))

        case "P" =>
          Some(PrivateMessage(entries(0).toInt, parseInt(entries(2)).get, parseInt(entries(3)).get, msg))

        case "S" =>
          Some(StatusUpdate(entries(0).toInt, parseInt(entries(2)).get, msg))
        case _ => None

      }
    } catch {
      case _: ArrayIndexOutOfBoundsException =>
        None
      case _: NoSuchElementException => //in case a userID is represented as a non Integer
        None
    }
  }
}

