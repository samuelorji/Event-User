package com.soundcloud.maze
package service

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.io.{IO, Tcp}
import akka.stream.ActorMaterializer

import com.soundcloud.maze.config.MazeConfig
import com.soundcloud.maze.socket.{EventSocketService, UserSocketService}
import com.soundcloud.maze.util.EventParser

object Server extends App {
//  implicit val system       = ActorSystem("FollowerMaze")
//  implicit val materializer = ActorMaterializer()
//
//  val eventSocketService = system.actorOf(Props[EventSocketService],"event-socket-service")
//  val userSocketService  = system.actorOf(Props[UserSocketService], "user-socket-service")
//
//  IO(Tcp) ! Tcp.Bind(
//    eventSocketService,
//    new InetSocketAddress(
//      MazeConfig.mazeEventsHost,
//      MazeConfig.mazeEventPort
//    )
//  )
//  IO(Tcp) ! Tcp.Bind(
//    userSocketService,
//    new InetSocketAddress(
//      MazeConfig.mazeUsersHost,
//      MazeConfig.mazeUsersPort
//    )
//  )

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val ev = system.actorOf(Props[EventServer])
  val us = system.actorOf(Props[UserServer])



}

class EventServer extends Actor {
  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 9090))

  def receive = {
    case b @ Bound(localAddress) =>
      context.parent ! b

    case CommandFailed(_: Bind) => context.stop(self)

    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[EventHandler])
      val connection = sender()
      connection ! Register(handler)
  }

}

class EventHandler extends Actor {

  //  def specificReceive : Receive
  //
  //  def genericReceive : Receive

  import Tcp._
  override def receive: Receive = {

    case Received(data) =>
      Thread.sleep(1000)
      println(s"Message received from Event source is [${EventParser.parse(data.utf8String)}] *")
  }
}
class UserServer extends Actor {
  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 9099))

  def receive = {
    case b @ Bound(localAddress) =>
      context.parent ! b

    case CommandFailed(_: Bind) => context.stop(self)

    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[UserHandler])
      val connection = sender()
      connection ! Register(handler)
  }

}

class UserHandler extends Actor {

  //  def specificReceive : Receive
  //
  //  def genericReceive : Receive

  import Tcp._
  override def receive: Receive = {

    case Received(data) =>
     // Thread.sleep(1000)
    //  println(s"Message received from User source is [${data.utf8String}] *")
  }



}
