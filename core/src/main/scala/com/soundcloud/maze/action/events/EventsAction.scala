package com.soundcloud.maze.action.events

sealed trait Event {
  val seqId : Int
  def isValid : Boolean
}
case object Done extends Event{
  override val seqId: Int = 4
  override def isValid: Boolean = true
}

case class Follow(seqId : Int ,from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class UnFollow(seqId : Int ,from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class Broadcast(seqId : Int ,payload : String) extends Event {
  override def isValid: Boolean = true
}

case class PrivateMessage(seqId : Int ,from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class StatusUpdate(seqId : Int ,from : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case object InvalidEvent extends Event {
  override val seqId: Int = 9
  override def isValid: Boolean = false
}
