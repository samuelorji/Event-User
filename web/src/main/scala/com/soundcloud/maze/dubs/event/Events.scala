package com.soundcloud.maze.dubs.event

import java.io.PrintWriter

object Events {

  val EventQueueOrdering: Ordering[Event] = Ordering.by(_.seqId)

  case object StartRouter

  type UserId = Int

  type SeqID = Int

  type RawMessage = String

  sealed trait Event {
    val seqId: Int
  }

  case class Follow(seqId: Int, from: Int, to: Int,payload : String) extends Event

  case class UnFollow(seqId: Int, from: Int, to: Int,payload : String) extends Event

  case class Broadcast(seqId: Int, payload : String) extends Event

  case class Private(seqId: Int,from: Int, to: Int,payload : String) extends Event

  case class StatusUpdate(seqId: Int , from: Int,payload : String) extends Event

  case class NewClientConnection(id: Int, outputStream: PrintWriter)

  object Event {

    def apply(msg: String): Option[Event] = msg.split('|') match {
      case Array(AsInt(seq), "F", AsInt(from), AsInt(to)) => Some(Follow(seq, from, to,msg))
      case Array(AsInt(seq), "U", AsInt(from), AsInt(to)) => Some(UnFollow(seq, from, to,msg))
      case Array(AsInt(seq), "B") => Some(Broadcast(seq, msg))
      case Array(AsInt(seq), "P", AsInt(from), AsInt(to)) => Some(Private(seq, from, to,msg))
      case Array(AsInt(seq), "S", AsInt(from)) => Some(StatusUpdate(seq, from,msg))
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
}
