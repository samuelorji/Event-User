package com.soundcloud.maze.service.router

import java.io.PrintWriter

import com.soundcloud.maze.core.action.events.Events._
import com.soundcloud.maze.core.config.{ActorLike, ActorSystemLike}
import com.soundcloud.maze.core.util.MazeLogger
import com.soundcloud.maze.service.registry.{FollowerRegistry, UserRegistry}
import com.soundcloud.maze.service.writer.SocketConnectionWriter

object Router{
  case class RegisterNewClient(userId : Int, client : PrintWriter)
}
class Router(implicit val system : ActorSystemLike ) extends ActorLike with MazeLogger {
  import Router._

  override protected def receive: PartialFunction[Any, Unit] = {
    case RegisterNewClient(id, out) =>
      log.info(s"Registering New Client : {}",id)
      UserRegistry.addUser(id,system.execute(SocketConnectionWriter(out)))

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
