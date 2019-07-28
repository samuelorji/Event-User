package com.soundcloud.maze.dubs

//import com.soundcloud.maze.actors.EventSocketHandlerR
import com.soundcloud.maze.dubs.actors.{ActorLike, ActorSystemLike, ClientSocketHandler, EventSocketHandler, Router}
import com.soundcloud.maze.util.MazeLogger

import scala.io.StdIn


object ServerR extends App with MazeLogger  {
  log.info("Starting server")

  implicit val system = new ActorSystemLike

  val router = system.execute(new Router)
  //At Startup ...start dequeueing the each actorLike's mailbox similar to actors
  val clientHandler = system.execute(new ClientSocketHandler(router))
  val eventHandler  = system.execute(new EventSocketHandler(router))

  clientHandler ! ClientSocketHandler.AcceptConnections
  eventHandler  ! EventSocketHandler.StartSocket

  StdIn.readLine()

  clientHandler ! ActorLike.Shutdown
  eventHandler ! ActorLike.Shutdown
  router       ! ActorLike.Shutdown

  system.shutdownSystem()

}