package com.soundcloud.maze.outbound

import akka.actor.{ActorRef, Props}

import com.soundcloud.maze.util.EventSocketBase

object EventSocketHandler {
  def props(connection : ActorRef) : Props = Props(new EventSocketHandler(connection))
}
class EventSocketHandler(
  val connection : ActorRef
  ) extends EventSocketBase {
  /**
    * All Messages are dealt within the socket message Handler */
  override protected def eventHandler: Receive = Map.empty
  override val userHandler                     = context.actorOf(EventUserPipe.props)
}
