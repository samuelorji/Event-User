//package com.soundcloud.maze.socket
//
//import akka.actor.{Actor, ActorLogging, ActorRef, Props}
//import akka.io.Tcp
//import com.soundcloud.maze.actors.UserSocketHandler
//import com.soundcloud.maze.inbound.UserInbound
//import com.soundcloud.maze.util.UserParser
//
//class UserSocketService(dispatcher : ActorRef) extends Actor with ActorLogging {
//
//  override def receive = {
//    case Tcp.CommandFailed(_ : Tcp.Bind) =>
//      context stop self
//
//    case Tcp.Connected(remote, _) =>
//      //log.info(s"Tcp.Connected Service has been connected [$remote]. Creating a new handler")
//      val connection = sender
//      val handler    = context.actorOf(Props(new UserSocketHandler(dispatcher,connection)))
//      connection ! Tcp.Register(handler)
//  }
//
//}
