package com.soundcloud.maze.action.events


sealed trait Event {
  val seqId : Int
  def isValid : Boolean
}

object EventOrdering {
  val eventOrdering: Ordering[Event] = Ordering.by(_.seqId)
}


case class Follow(seqId : Int ,from : Int, to : Int,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class UnFollow(seqId : Int ,from : Int, to : Int,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class Broadcast(seqId : Int ,payload : String) extends Event {
  override def isValid: Boolean = true
}

case class PrivateMessage(seqId : Int ,from : Int, to : Int,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class StatusUpdate(seqId : Int ,from : Int,payload: String) extends Event {
  override def isValid: Boolean = true
}

case object InvalidEvent extends Event {
  override val seqId: Int = 9 // arbitrary number for invalid event
  override def isValid: Boolean = false
}
