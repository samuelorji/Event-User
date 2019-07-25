package com.soundcloud.maze.socket

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp

import com.soundcloud.maze.outbound.{EventSocketHandler, EventUserPipe}


class EventSocketService extends Actor with ActorLogging{

  override def receive = {
    case Tcp.CommandFailed(_ : Tcp.Bind) =>
      context stop self

    case Tcp.Connected(remote, local) =>
      val connection = sender
      val handler    = context.actorOf(EventSocketHandler.props(
        connection
      ))
      connection ! Tcp.Register(handler)
  }

}
