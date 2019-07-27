package com.soundcloud.maze.dubs

//import com.soundcloud.maze.actors.EventSocketHandlerR
import com.soundcloud.maze.dubs.actors.{ActorLike, ActorSystemLike, ClientSocketHandler, EventSocketHandler, Router}

import scala.io.StdIn


object ServerR extends App {

  implicit val system = new ActorSystemLike

  val router = system.execute(new Router)
  val clientHandler = system.execute(new ClientSocketHandler(router,9099))
  val eventHandler  = system.execute(new EventSocketHandler(router,9090))

  clientHandler ! ClientSocketHandler.AcceptConnections
  eventHandler  ! EventSocketHandler.StartSocket

  StdIn.readLine()

  clientHandler ! ActorLike.Shutdown
  eventHandler ! ActorLike.Shutdown
  router       ! ActorLike.Shutdown

  system.shutdownSystem()

}

//def main(args: Array[String]): Unit = {
//
//  import com.soundcloud.maze.dubs.actors.ActorSystemLike
//
//  println("follower-maze server started \\O/")
//
//  implicit val actorSystem = new ActorSystem
//  val dispatcherActor = actorSystem.materialize(new MessageDispatcherActor)
//  val clientsActor = actorSystem.materialize(ClientsActor(dispatcherActor, 9099))
//  val eventSourceActor = actorSystem.materialize(EventSourceActor(dispatcherActor, 9090))
//
//  clientsActor ! StartSocketServer
//  eventSourceActor ! StartSocketServer
//
//  println("After receiving all messages, press enter to terminate...")
//  System.in.read()
//
//  eventSourceActor ! `R.I.P`
//  clientsActor ! `R.I.P`
//  dispatcherActor ! `R.I.P`
//  actorSystem.shutdown()
//
//  println("follower-maze server exited \\O/")
//}
