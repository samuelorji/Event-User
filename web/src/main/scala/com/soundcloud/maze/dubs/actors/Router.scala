package com.soundcloud.maze.dubs.actors

import java.io.PrintWriter

import com.soundcloud.maze.dubs.actors.registry.FollowerRegistry
import com.soundcloud.maze.dubs.event.Events._

//import com.soundcloud.maze.action.events._
import com.soundcloud.maze.dubs.actors.SocketConnectionWriter.WriteToSocket
import com.soundcloud.maze.dubs.actors.registry.UserRegistry


import scala.collection.mutable

object Router{
  case class RegisterNewClient(userId : Int, writer : PrintWriter)
}
class Router()(implicit val system : ActorSystemLike ) extends ActorLike {
  import Router._
 // import UserRegistry.{clientConnections,followers}
  override protected def receive: PartialFunction[Any, Unit] = {
    case RegisterNewClient(id, out) =>
      UserRegistry.addUser(id,system.execute(SocketConnectionWriter(out)))
      //clientConnections.put(id, system.execute(SocketConnectionWriter(out)))

    case Follow(_, from, to,payload) =>
      FollowerRegistry.addFollow(to ,from)
     // followers.addBinding(to, from)
      UserRegistry.findUser(to).foreach(write(payload))
     // clientConnections.get(to).foreach(write(rawMessage))

    case UnFollow(_,from, to,payload) =>
      FollowerRegistry.removeFollow(to,from)
     // followers.removeBinding(to, from)

    case Broadcast(_, payload) =>
      UserRegistry.getAllUsers.foreach(write(payload))
      //clientConnections.values.foreach(write(rawMessage))

    case Private(_, from, to,payload) =>
      UserRegistry.findUser(to).foreach(write(payload))
     // clientConnections.get(to).foreach(write(rawMessage))

    case StatusUpdate(_, from,payload) =>
      FollowerRegistry.getFollow(from)
      val followers = FollowerRegistry.getFollow(from)
      followers.foreach(
        _.foreach(UserRegistry.findUser(_).foreach(write(payload)))
      )
  }

  private def write(msg: RawMessage): ActorLike => Unit = _ ! SocketConnectionWriter.WriteToSocket(msg)

  override protected def onShutdown(): Unit = {
   UserRegistry.getAllUsers.foreach(_ ! ActorLike.Shutdown)
    super.onShutdown()
  }
  implicit def `toActorLike`(writer : PrintWriter)  : ActorLike = {
    system.execute(SocketConnectionWriter(writer))
  }
}
class UserToFollowers extends mutable.HashMap[Int, mutable.Set[Int]] with mutable.MultiMap[Int, Int] {
  override def default(key: Int) = makeSet
}
