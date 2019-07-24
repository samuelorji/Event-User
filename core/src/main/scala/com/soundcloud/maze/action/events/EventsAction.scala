package com.soundcloud.maze.action.events

sealed trait Event

case class Follow(from : String, to : String,payload: String) extends Event

case class UnFollow(from : String, to : String,payload: String) extends Event

case object Broadcast extends Event

case class PrivateMessage(from : String, to : String,payload: String) extends Event

case class StatusUpdate(from : String,payload: String) extends Event

case object InvalidEvent extends Event
