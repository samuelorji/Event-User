package test

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.actor.{ Actor, ActorRef, Props }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

object Test extends App {

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
      println(s"Connection received from event source ${remote.getAddress}")
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
      println(s"Message received from Event source is ${data.utf8String}")
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
      println(s"Connection received from user ${remote.getAddress}")
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
      Thread.sleep(1000)
      println(s"Message received from USer source is ${data.utf8String}")
  }
}
