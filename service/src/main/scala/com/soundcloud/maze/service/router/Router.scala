package com.soundcloud.maze
package service.router

import java.io.PrintWriter

import core.action.events.Events._
import core.config.{ ActorLike, ActorSystemLike }
import core.util.MazeLogger

import service.registry.{FollowerRegistry, UserRegistry}
import service.writer.SocketConnectionWriter

object Router{
  def getRouterInstance(implicit system : ActorSystemLike) = new Router

}
/**
  * This Actor is solely responsible for routing incoming events to the relevant Client connections  */
private[service] class Router(implicit val system : ActorSystemLike ) extends ActorLike with MazeLogger {

  override protected def receive: PartialFunction[Any, Unit] = {

    case Follow(_, from, to,payload) =>
      FollowerRegistry.addFollow(to ,from)
      UserRegistry.findUser(to).foreach(writeToSocket(to,payload,_))


    case UnFollow(_,from, to,_) =>
      FollowerRegistry.removeFollow(to,from)

    case Broadcast(_, payload) =>
      UserRegistry.getAllUsers.foreach(user => writeToSocket(user._1,payload,user._2))

    case PrivateMessage(_, _, to,payload) =>
      UserRegistry.findUser(to).foreach(writeToSocket(to ,payload,_))

    case StatusUpdate(_, from,payload) =>
       FollowerRegistry.getFollow(from).foreach(
        _.foreach(UserRegistry.findUser(_).foreach(writeToSocket(from, payload,_)))
      )

  }

  private def writeToSocket(id : Int , msg: String,ref : ActorLike) = {
    ref ! SocketConnectionWriter.WriteToSocket(msg)
    log.info(s"Writing $msg to client [$id]")
  }

  override protected def shutdownActorLike(): Unit = {
   UserRegistry.getAllUsers.foreach(user => user._2 ! ActorLike.Shutdown)
    super.shutdownActorLike()
  }
  implicit def `toActorLike`(writer : PrintWriter)  : ActorLike = {
    system.execute(SocketConnectionWriter(writer))
  }
}
