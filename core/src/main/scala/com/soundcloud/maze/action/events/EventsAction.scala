package com.soundcloud.maze.action.events

sealed trait Event {
  def isValid : Boolean
}
case object Done extends Event{
  override def isValid: Boolean = true
}

case class Follow(from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class UnFollow(from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class Broadcast(payload : String) extends Event {
  override def isValid: Boolean = true
}

case class PrivateMessage(from : String, to : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case class StatusUpdate(from : String,payload: String) extends Event {
  override def isValid: Boolean = true
}

case object InvalidEvent extends Event {
  override def isValid: Boolean = false
}
