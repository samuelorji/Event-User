package com.soundcloud.maze.socket

import akka.actor.{ Actor, ActorLogging }
import akka.io.Tcp


class EventSocketService extends Actor with ActorLogging{

  override def receive = {
    case Tcp.CommandFailed(_ : Tcp.Bind) =>
      context stop self

    case Tcp.Connected(remote, local) =>
      log.info(s"Tcp.Connected Service has been connected [$remote]. Creating a new handler")
      println(s"Tcp.Connected Service has been connected [$remote]. Creating a new handler")
      val connection = sender
//      val handler    = context.actorOf(FSInboundEventSocket.props(
//        connection,
//        InboundCallEventHandlerProps
//      ))
//      connection ! Tcp.Register(handler)
  }

}
