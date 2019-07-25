package com.soundcloud.maze.socket

import akka.actor.{Actor, ActorLogging}
import akka.io.Tcp

import com.soundcloud.maze.inbound.UserInbound

class UserSocketService extends Actor with ActorLogging {

  override def receive = {
    case Tcp.CommandFailed(_ : Tcp.Bind) =>
      context stop self

    case Tcp.Connected(remote, _) =>
      //log.info(s"Tcp.Connected Service has been connected [$remote]. Creating a new handler")
      val connection = sender
      val handler    = context.actorOf(UserInbound.props(
        connection
      ))
      connection ! Tcp.Register(handler)
  }

}
