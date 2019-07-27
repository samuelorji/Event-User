//package com.soundcloud.maze.socket
//
//import akka.actor.{Actor, ActorLogging, ActorRef, Props}
//import akka.io.Tcp
//import com.soundcloud.maze.actors.{EventSocketHandlerR, MessageDispatcherActor}
//import com.soundcloud.maze.outbound.{EventSocketHandler, EventUserPipe}
//
//
//class EventSocketService(dispatcher : ActorRef) extends Actor with ActorLogging{
//
//  //val dispatcher = context.actorOf(Props[MessageDispatcherActor])
//  override def receive = {
//    case Tcp.CommandFailed(_ : Tcp.Bind) =>
//      context stop self
//
//    case Tcp.Connected(remote, local) =>
//      val connection = sender
//      val handler    = context.actorOf(Props(new EventSocketHandlerR(dispatcher)))
//      connection ! Tcp.Register(handler)
//  }
//
//}
